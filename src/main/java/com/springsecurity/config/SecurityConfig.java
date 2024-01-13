package com.springsecurity.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springsecurity.constant.Error;
import com.springsecurity.constant.RoleName;
import com.springsecurity.model.ErrorResponse;
import com.springsecurity.filter.AuthRequestFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
*prePostEnabled = true enables the use of annotations like @PreAuthorize, @PostAuthorize, @PreFilter and @PostFilter.
securedEnabled = true enables the use of the @Secured annotation.
jsr250Enabled = true enables the use of the @RolesAllowed annotation.
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final AuthRequestFilter authRequestFilter;

    /*
     * if we define Authority like this, it means Role and Authority is same but by default 'ROLE_'
     * role name is used like 'ADMIN','USER' etc.
     * authority name is used like 'READ','DELETE' etc.
     */
    @Bean
    static GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request ->
                        request.requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/public").permitAll()
                                .requestMatchers("/user/**").hasAuthority(RoleName.Fields.USER)
                                .requestMatchers("/admin/**").hasAuthority(RoleName.Fields.ADMIN)
                                .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(
                        exceptionHandling -> {
                            exceptionHandling.authenticationEntryPoint(this::checkAuthentication);
                            exceptionHandling.accessDeniedHandler(this::checkAccessDenied);
                        })
                .sessionManagement(
                        securitySessionManagement ->
                                securitySessionManagement
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y, 10);
    }

    private void checkAuthentication(HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse,
                                     AuthenticationException authenticationException)
            throws IOException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        var response = new ErrorResponse(Error.UNAUTHORIZED);
        httpServletResponse.getOutputStream().write(getByteArray(response));
    }

    private void checkAccessDenied(HttpServletRequest httpServletRequest,
                                   HttpServletResponse httpServletResponse,
                                   AccessDeniedException accessDeniedException)
            throws IOException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        var response = new ErrorResponse(Error.ACCESS_DENIED);
        httpServletResponse.getOutputStream().write(getByteArray(response));
    }

    private byte[] getByteArray(ErrorResponse errorResponse) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(errorResponse)
                .getBytes(StandardCharsets.UTF_8);
    }
}

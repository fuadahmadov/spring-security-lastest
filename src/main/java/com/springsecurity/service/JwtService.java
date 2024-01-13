package com.springsecurity.service;

import static com.springsecurity.constant.Constant.AUTH_HEADER;
import static com.springsecurity.constant.Constant.BEARER_AUTH_HEADER;
import static com.springsecurity.constant.TokenType.ACCESS;

import com.springsecurity.constant.Constant;
import com.springsecurity.constant.TokenType;
import com.springsecurity.domain.Role;
import com.springsecurity.domain.User;
import com.springsecurity.model.UserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;


@Service
public class JwtService {

    private static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor("fa8da98d-87b5-4a04-9f4f-235010092ff8".getBytes());

    public String createToken(User user, TokenType tokenType) {
        var accessTokenTtl = 5 * 60 * 1000L;
        var refreshTokenTtl = 60 * 60 * 1000L;
        var tokenTtl = tokenType == ACCESS ? accessTokenTtl : refreshTokenTtl;
        var roles = user.getRoles().stream().map(Role::getName).toList();
        return Jwts.builder()
                .claim(Constant.TOKEN_TYPE, tokenType)
                .claim(Constant.USER_ID, user.getId())
                .claim(Constant.USER_EMAIL, user.getEmail())
                .claim(Constant.USER_ROLES, roles)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .setExpiration(new Date(System.currentTimeMillis() + tokenTtl))
                .compact();
    }

    public Claims decodeToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Optional<Authentication> getAuthentication(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTH_HEADER))
                .filter(header -> header.startsWith(BEARER_AUTH_HEADER))
                .map(header -> header.substring(BEARER_AUTH_HEADER.length()))
                .map(this::decodeToken)
                .map(this::claimsToAuthentication);
    }


    @SuppressWarnings("unchecked")
    private Authentication claimsToAuthentication(Claims claims) {
        Long id = claims.get(Constant.USER_ID, Long.class);
        String email = claims.get(Constant.USER_EMAIL, String.class);
        List<String> roles = (List<String>) claims.get(Constant.USER_ROLES);
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new UsernamePasswordAuthenticationToken(
                new UserDetail(id, email), "", authorities);
    }
}

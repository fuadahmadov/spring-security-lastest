package com.springsecurity.service;

import com.springsecurity.constant.Constant;
import com.springsecurity.constant.Error;
import com.springsecurity.constant.RoleName;
import com.springsecurity.constant.TokenType;
import com.springsecurity.domain.Role;
import com.springsecurity.exception.CommonException;
import com.springsecurity.mapper.UserMapper;
import com.springsecurity.model.JwtTokenDto;
import com.springsecurity.model.LoginRequestDto;
import com.springsecurity.model.RegistrationRequestDto;
import com.springsecurity.repository.UserRepository;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private static final UserMapper userMapper = UserMapper.INSTANCE;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JwtTokenDto login(LoginRequestDto request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CommonException(Error.INVALID_USER_CREDENTIAL));

        var isPasswordMatched = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isPasswordMatched) {
            throw new CommonException(Error.INVALID_USER_CREDENTIAL);
        }

        var accessToken = jwtService.createToken(user, TokenType.ACCESS);
        var refreshToken = jwtService.createToken(user, TokenType.REFRESH);
        user.setRefreshToken(refreshToken);

        userRepository.save(user);
        return new JwtTokenDto(accessToken, refreshToken);
    }

    @Transactional
    public void registration(RegistrationRequestDto request) {
        var userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            throw new CommonException(Error.REGISTRATION_ALREADY_EXIST);
        }

        var encodedPassword = passwordEncoder.encode(request.getPassword());
        var user = userMapper.toUser(request, encodedPassword);
        var roles = new HashSet<Role>();
        roles.add(new Role(RoleName.USER, user));
        user.setRoles(roles);
        userRepository.save(user);
    }

    public JwtTokenDto refreshToken(String token) {
        var claims = jwtService.decodeToken(token);
        var email = claims.get(Constant.USER_EMAIL, String.class);

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CommonException(Error.INVALID_USER_CREDENTIAL));

        if (!user.getRefreshToken().equals(token)) {
            throw new CommonException(Error.INVALID_TOKEN);
        }

        var accessToken = jwtService.createToken(user, TokenType.ACCESS);
        var refreshToken = jwtService.createToken(user, TokenType.REFRESH);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return new JwtTokenDto(accessToken, refreshToken);
    }

}

package com.navya.employeemanagement.service;

import com.navya.employeemanagement.dto.request.LoginRequest;
import com.navya.employeemanagement.dto.request.RegisterRequest;
import com.navya.employeemanagement.dto.response.LoginResponse;
import com.navya.employeemanagement.dto.response.RegisterResponse;
import com.navya.employeemanagement.entity.User;
import com.navya.employeemanagement.exception.DuplicateUserEmailException;
import com.navya.employeemanagement.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl  implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserEmailException("Email is already registered");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(
                request.getEmail(),
                encodedPassword,
                "USER"
        );
        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return new LoginResponse(
                jwtService.generateToken(user),
                "Bearer",
                3600L,
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}

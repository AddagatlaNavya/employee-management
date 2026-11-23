package com.navya.employeemanagement.service;

import com.navya.employeemanagement.dto.request.LoginRequest;
import com.navya.employeemanagement.dto.request.RegisterRequest;
import com.navya.employeemanagement.dto.response.LoginResponse;
import com.navya.employeemanagement.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}

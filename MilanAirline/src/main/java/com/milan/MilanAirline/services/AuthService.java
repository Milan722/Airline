package com.milan.MilanAirline.services;


import com.milan.MilanAirline.dtos.LoginRequest;
import com.milan.MilanAirline.dtos.RegistrationRequest;
import com.milan.MilanAirline.dtos.Response;

public interface AuthService {
    Response<?> register(RegistrationRequest registrationRequest);
    Response<?> login(LoginRequest loginRequest);
}

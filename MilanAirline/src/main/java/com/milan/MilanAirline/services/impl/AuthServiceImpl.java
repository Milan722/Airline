package com.milan.MilanAirline.services.impl;


import com.milan.MilanAirline.dtos.LoginRequest;
import com.milan.MilanAirline.dtos.LoginResponse;
import com.milan.MilanAirline.dtos.RegistrationRequest;
import com.milan.MilanAirline.dtos.Response;
import com.milan.MilanAirline.entities.Role;
import com.milan.MilanAirline.entities.User;
import com.milan.MilanAirline.enums.AuthMethod;
import com.milan.MilanAirline.exceptions.BadRequestException;
import com.milan.MilanAirline.exceptions.NotFoundException;
import com.milan.MilanAirline.repo.RoleRepo;
import com.milan.MilanAirline.repo.UserRepo;
import com.milan.MilanAirline.security.JwtUtils;
import com.milan.MilanAirline.services.AuthService;
import com.milan.MilanAirline.services.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RoleRepo roleRepo;
    private final EmailNotificationService emailNotificationService;

    @Override
    public Response<?> register(RegistrationRequest registrationRequest) {
       log.info("Inside register()");
        if (userRepo.existsByEmail(registrationRequest.getEmail())) {
            throw new BadRequestException("Email already exist");
        }

        List<Role> userRoles;

        if(registrationRequest.getRoles()!=null&&!registrationRequest.getRoles().isEmpty()){
            userRoles=registrationRequest.getRoles().stream()
                    .map(roleName ->roleRepo.findByName(roleName.toUpperCase())
                            .orElseThrow( () -> new NotFoundException("Role"+roleName+"Not Found"))).toList();
        }
        else{
            Role defaultRole=roleRepo.findByName("CUSTOMER")
                    .orElseThrow(() -> new NotFoundException(("Role CUSTOMER doesn't exist")));
            userRoles=List.of(defaultRole);
        }
        User userToSave=new User();
        userToSave.setName(registrationRequest.getName());
        userToSave.setEmail(registrationRequest.getEmail());
        userToSave.setPhoneNumber(registrationRequest.getPhoneNumber());
        userToSave.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        userToSave.setRoles(userRoles);
        userToSave.setCreateAt(LocalDateTime.now());
        userToSave.setUpdatedAt(LocalDateTime.now());
        userToSave.setProvider(AuthMethod.LOCAL);
        userToSave.setActive(true);

        User savedUser=userRepo.save(userToSave);

        emailNotificationService.sendWelcomeEmail(savedUser);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("User registered successfully")
                .build();


    }

    @Override
    public Response<LoginResponse> login(LoginRequest loginRequest) {
        log.info("Inside login()");
        User user=userRepo.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Email Not Found"));

        if(!user.isActive()){
            throw new NotFoundException("Account is not active,please reach out to customer care...");
        }
        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            throw new BadRequestException("Invalid Password");
        }

        String token=jwtUtils.generateToken(user.getEmail());

        List<String> roleNames=user.getRoles().stream().map(Role::getName).toList();

        LoginResponse loginResponse=new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setRoles(roleNames);

        return Response.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login Successful")
                .data(loginResponse)
                .build();
    }
}

package com.milan.MilanAirline.controller;


import com.milan.MilanAirline.dtos.RegistrationRequest;
import com.milan.MilanAirline.dtos.Response;
import com.milan.MilanAirline.dtos.UserDTO;
import com.milan.MilanAirline.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserContoller {

    private final UserService userService;

    @PutMapping
    public ResponseEntity<Response<?>> updateMyAccount(@Valid @RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.updateMyAccount(userDTO));
    }

    @GetMapping("/pilots")
    @PreAuthorize("hasAnyAuthority('ADMIN','PILOT')")
    public ResponseEntity<Response<List<UserDTO>>> get(){
        return ResponseEntity.ok(userService.getAllPilots());
    }


    @GetMapping("/me")
    public ResponseEntity<Response<UserDTO>> getAccountDetails(){
        return ResponseEntity.ok(userService.getAccountDetails());
    }


}

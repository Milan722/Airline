package com.milan.MilanAirline.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.milan.MilanAirline.entities.Role;
import com.milan.MilanAirline.enums.AuthMethod;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;


    private String name;
    @Column(unique = true)
    private String email;

    private String phoneNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private boolean emailVerified;


    private AuthMethod provider;

    private String providerId;


    private List<Role> roles;

    private boolean active;


    private LocalDateTime createAt;

    private LocalDateTime updatedAt;
}

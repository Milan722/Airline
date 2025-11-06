package com.milan.MilanAirline.services;

import com.milan.MilanAirline.dtos.Response;
import com.milan.MilanAirline.dtos.UserDTO;
import com.milan.MilanAirline.entities.User;

import java.util.List;

public interface UserService {

    User currentUser();
    Response<?> updateMyAccount (UserDTO userDTO);
    Response<List<UserDTO>> getAllPilots();
    Response<UserDTO> getAccountDetails();

}

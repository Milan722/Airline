package com.milan.MilanAirline.services;

import com.milan.MilanAirline.dtos.Response;
import com.milan.MilanAirline.dtos.RoleDTO;

import java.util.List;

public interface RoleService {

    Response<?> createRole(RoleDTO roleDTO);
    Response<?> updateRole(RoleDTO roleDTO);
    Response<List<RoleDTO>> getAllRoles();
}

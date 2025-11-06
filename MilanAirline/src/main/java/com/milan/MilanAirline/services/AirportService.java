package com.milan.MilanAirline.services;

import com.milan.MilanAirline.dtos.AirportDTO;
import com.milan.MilanAirline.dtos.Response;


import java.util.List;

public interface AirportService {

    Response<?> createAirport(AirportDTO  airportDTO);
    Response<?> updateAirport(AirportDTO  airportDTO);
    Response<List<AirportDTO>> getAllAirports();
    Response<AirportDTO> getAirportById(Long id);
}

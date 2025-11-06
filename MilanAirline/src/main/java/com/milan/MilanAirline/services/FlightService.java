package com.milan.MilanAirline.services;

import com.milan.MilanAirline.dtos.CreateFlightRequest;
import com.milan.MilanAirline.dtos.FlightDTO;
import com.milan.MilanAirline.dtos.Response;
import com.milan.MilanAirline.enums.City;
import com.milan.MilanAirline.enums.Country;
import com.milan.MilanAirline.enums.FlightStatus;

import java.time.LocalDate;
import java.util.List;

public interface FlightService {

    Response<?> createFlight(CreateFlightRequest createFlightRequest);
    Response<FlightDTO> getFlightByID(Long id);
    Response<List<FlightDTO>> getAllFlights();
    Response<?> updateFlight(CreateFlightRequest createFlightRequest);
    Response<List<FlightDTO>> searchFlight(String departurePortIata, String arrivalPortIata, FlightStatus status, LocalDate departureDate);
    Response<List<City>> getAllCities();
    Response<List<Country>> getAllCountries();



}

package com.milan.MilanAirline.services.impl;

import com.milan.MilanAirline.dtos.AirportDTO;
import com.milan.MilanAirline.dtos.Response;
import com.milan.MilanAirline.entities.Airport;
import com.milan.MilanAirline.enums.City;
import com.milan.MilanAirline.enums.Country;
import com.milan.MilanAirline.exceptions.BadRequestException;
import com.milan.MilanAirline.exceptions.NotFoundException;
import com.milan.MilanAirline.repo.AirportRepo;
import com.milan.MilanAirline.services.AirportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportRepo airportRepo;
    private final ModelMapper modelMapper;

    @Override
    public Response<?> createAirport(AirportDTO airportDTO) {
        log.info("Inside createAirport()");

        Country country=airportDTO.getCountry();
        City city=airportDTO.getCity();

        if(!city.getCountry().equals(country)){

            throw new BadRequestException("City does not belong to the country");
        }
        Airport airport=modelMapper.map(airportDTO,Airport.class);
        airportRepo.save(airport);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Airport Created Successfully")
                .build();
    }

    @Override
    public Response<?> updateAirport(AirportDTO airportDTO) {
        Long id=airportDTO.getId();
        Airport existingAirport=airportRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Airport not found"));

        if(airportDTO.getCity()!=null){
            if(!airportDTO.getCity().getCountry().equals(existingAirport.getCountry())){
                throw new BadRequestException("City does not belong to the country");
            }
            existingAirport.setCity(airportDTO.getCity());
        }
        if(airportDTO.getName()!=null){
            existingAirport.setName(airportDTO.getName());
        }
        if(airportDTO.getIataCode()!=null){
            existingAirport.setIataCode(airportDTO.getIataCode());
        }
        airportRepo.save(existingAirport);
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Airport updated Successfully")
                .build();
    }

    @Override
    public Response<List<AirportDTO>> getAllAirports() {
        List<AirportDTO> airports=airportRepo.findAll().stream().map(airport -> modelMapper.map(airport,AirportDTO.class)).toList();
        return Response.<List<AirportDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(airports.isEmpty()?"No airports found":"Airports retrieved successfully")
                .data(airports)
                .build();
    }

    @Override
    public Response<AirportDTO> getAirportById(Long id) {
        Airport airport=airportRepo.findById(id).orElseThrow(() -> new NotFoundException("Airport not found"));

        AirportDTO airportDTO=modelMapper.map(airport,AirportDTO.class);
        return Response.<AirportDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Airports retrieved successfully")
                .data(airportDTO)
                .build();

    }
}

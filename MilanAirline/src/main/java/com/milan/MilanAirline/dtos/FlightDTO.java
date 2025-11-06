package com.milan.MilanAirline.dtos;


import com.milan.MilanAirline.enums.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {

    private Long id;


    private String flightNumber;


    private FlightStatus status;


    private AirportDTO departureAirport;


    private AirportDTO arrivalAirport;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    private BigDecimal basePrice;


    private UserDTO assignedPilot;


    private List<BookingDTO> bookings;

    private String departureAirportIataCode;

    private String arrivalAirportIataCode;
}

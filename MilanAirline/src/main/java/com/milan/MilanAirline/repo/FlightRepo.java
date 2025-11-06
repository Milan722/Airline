package com.milan.MilanAirline.repo;

import com.milan.MilanAirline.entities.Flight;
import com.milan.MilanAirline.enums.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepo extends JpaRepository<Flight, Long> {

    boolean existsByFlightNumber(String flightNumber);

    List<Flight> findByDepartureAirportIataCodeAndArrivalAirportIataCodeAndStatusAndDepartureTimeBetween(
            String departureIataCode, String arrivalIataCode, FlightStatus status, LocalDateTime startOfDay,LocalDateTime endOfDay);
}

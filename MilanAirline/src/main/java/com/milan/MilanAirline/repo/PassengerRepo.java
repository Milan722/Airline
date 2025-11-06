package com.milan.MilanAirline.repo;

import com.milan.MilanAirline.entities.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepo extends JpaRepository<Passenger, Long> {

}

package com.milan.MilanAirline.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {
    @NotNull(message = "Flight id cannot be null")
    private Long flightid;

    @NotEmpty(message = "At least 1 passenger must be provided")
    private List<PassengerDTO> passengers;

}

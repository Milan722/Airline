package com.milan.MilanAirline.dtos;

import com.milan.MilanAirline.enums.City;
import com.milan.MilanAirline.enums.Country;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportDTO {


    private Long id;

    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "city is required")
    private City city;

    @NotNull(message = "country is required")
    private Country country;

    @NotBlank(message = "iataCode is required")
    private String iataCode;
}

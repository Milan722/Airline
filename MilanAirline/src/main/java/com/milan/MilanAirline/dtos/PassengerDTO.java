package com.milan.MilanAirline.dtos;

import com.milan.MilanAirline.enums.PassengerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDTO {


    private Long id;

    @NotBlank(message = "First Name cannot be blank")
    private String firstname;

    @NotBlank(message = "Last Name cannot be blank")
    private String lastname;

    private String passportNumber;

    @NotNull(message = "Passenger type cannot be null")
    private PassengerType type;

    private String seatNumber;

    private String specialRequest;
}

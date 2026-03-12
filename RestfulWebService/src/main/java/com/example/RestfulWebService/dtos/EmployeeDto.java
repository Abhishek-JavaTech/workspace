package com.example.RestfulWebService.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    @Size(max = 15, message = "Firstname value is out of range, it should be less than or equal to 15")
    private String firstName;

    @Size(max = 30, message = "Firstname value is out of range, it should be less than or equal to 30")
    private String lastName;

    @Email
    private String email;

    @Pattern(regexp = "\\d{10}", message = "mobile number must be 10 digits")
    private String mobileNumber;

    @Size(min = 2, max = 2, message = "state should be of 2 characters long")
    private String state;

    @Size(min = 5, max = 50, message = "country should be of 50 characters long")
    private String country;

    @Pattern(regexp = "\\d{6}", message = "Zip code must be 6 digits")
    private String zipCode;
}

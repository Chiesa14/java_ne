package com.example.exam.dto;

import com.example.exam.entity.Employee;
import com.example.exam.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RegisterRequest {
    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$")
    @NotBlank
    private String mobile;

    @Past
    @NotNull
    private LocalDate dob;

    @NotNull
    private Role defaultRole;
}

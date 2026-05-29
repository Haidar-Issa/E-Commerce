package com.example.test.DTO;

import com.example.test.Enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String password;
    private String email;

}

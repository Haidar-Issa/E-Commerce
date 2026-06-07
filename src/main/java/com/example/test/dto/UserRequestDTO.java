package com.example.test.dto;

import lombok.Data;

@Data
public class UserRequestDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role ;
    private String phoneNumber;
    private String address;
    private boolean active;
}

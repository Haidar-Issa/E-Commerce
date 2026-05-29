package com.example.test.Service;

import com.example.test.DTO.ApiResponse;
import com.example.test.DTO.UserRequestDTO;
import com.example.test.DTO.UserResponseDTO;
import com.example.test.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    UserResponseDTO create(UserRequestDTO userRequestDTO);

    UserResponseDTO update(String id, UserRequestDTO userRequestDTO);

    void delete(String id);

    UserResponseDTO login(UserRequestDTO userRequestDTO);

    UserResponseDTO findById(String id);

    UserResponseDTO findByEmail(String email);

    Page<UserResponseDTO> findSomeUsers(Pageable pageable);

    Page<UserResponseDTO> findByRole(String role , Pageable pageable);

    List<UserResponseDTO> findAll();

    void updatePassword(String otp,String email , String password);

    void sendOtp(String email);
}


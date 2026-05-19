package com.example.test.Mapper;

import com.example.test.DTO.UserRequestDTO;
import com.example.test.DTO.UserResponseDTO;
import com.example.test.Entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toDto(User user);

    User toEntity(UserRequestDTO userRequestDTO);

    List<UserResponseDTO> toDto(List<User> users);


}

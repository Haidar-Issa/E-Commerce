package com.example.test.mapper;

import com.example.test.dto.UserRequestDTO;
import com.example.test.dto.UserResponseDTO;
import com.example.test.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id" , target= "id" )
    @Mapping(source = "firstName" , target = "firstName")
    @Mapping(source = "lastName" , target = "lastName")
    @Mapping(source = "password" , target = "password")
    @Mapping(source = "email" , target = "email")
    @Mapping(source = "role" , target = "role")
    @Mapping(source = "phoneNumber" , target = "phone")
    UserResponseDTO toDto(User user);


    @Mapping(source = "firstName" , target = "firstName")
    @Mapping(source = "lastName" , target = "lastName")
    @Mapping(source = "password" , target = "password")
    @Mapping(source = "email" , target = "email")
    @Mapping(source = "role" , target = "role")
    User toEntity(UserRequestDTO userRequestDTO);

    @Mapping(source = "id" , target= "id" )
    @Mapping(source = "firstName" , target = "firstName")
    @Mapping(source = "lastName" , target = "lastName")
    @Mapping(source = "password" , target = "password")
    @Mapping(source = "email" , target = "email")
    @Mapping(source = "role" , target = "role")
    @Mapping(source = "phoneNumber" , target = "phone")
    List<UserResponseDTO> toDto(List<User> users);


}

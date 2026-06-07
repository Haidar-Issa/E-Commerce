package com.example.test.service;

import com.example.test.dto.UserRequestDTO;
import com.example.test.dto.UserResponseDTO;
import com.example.test.entity.User;
import com.example.test.exception.UserAlreadyExistsException;
import com.example.test.exception.UserNotFoundException;
import com.example.test.mapper.UserMapper;
import com.example.test.repository.UserRepository;
import com.example.test.service.Otp.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder password;

    private final UserMapper userMapper;

    private final OtpService otpService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO create(UserRequestDTO userRequestDTO) {
        User user = userMapper.toEntity(userRequestDTO);

        if (userRepository.existsUserByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserResponseDTO update(String id, UserRequestDTO userRequestDTO) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with this id not found"));

        if (userRequestDTO.getFirstName() != null) {
            user.setFirstName(userRequestDTO.getFirstName());
        }

        if (userRequestDTO.getLastName() != null) {
            user.setLastName(userRequestDTO.getLastName());
        }

        if (userRequestDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userRequestDTO.getPhoneNumber());
        }

        if (userRequestDTO.getAddress() != null) {
            user.setAddress(userRequestDTO.getAddress());
        }

        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty()) {
            user.setPassword(password.encode(user.getPassword()));
        }

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public void delete(String id) {
        if (!userRepository.existsUserById(id)) {
            throw new UserNotFoundException("User id not Found !");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO login(UserRequestDTO userRequestDTO) {
        return null;
    }

    @Override
    public UserResponseDTO findById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User id not Found "));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDTO findByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Email is not found !"));

        return userMapper.toDto(user);
    }

    @Override
    public Page<UserResponseDTO> findSomeUsers(Pageable pageable) {

        Page<User> users = userRepository.findSomeUsers(pageable);
        return users.map(userMapper::toDto);
    }

    @Override
    public Page<UserResponseDTO> findByRole(String role, Pageable pageable) {
        if (!(role.equals("admin") || role.equals("user") || role.equals("GUEST"))) {
            throw new RuntimeException("Invalid role");
        }
        Page<User> users = userRepository.findUsersByRole(role, pageable);

        return users.map(userMapper::toDto);
    }

    @Override
    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();
        return userMapper.toDto(users);
    }

    @Override
    public void updatePassword(String otp, String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with this email not found"));

        if (otpService.isValidOtp(email, otp)) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        } else {
            throw new RuntimeException("Invalid OTP");
        }
    }

    @Override
    public void sendOtp(String email) {
        if (!userRepository.existsUserByEmail(email)) {
            throw new UserNotFoundException("User with this email not found");
        }
        otpService.sendOtp(email);
    }
}

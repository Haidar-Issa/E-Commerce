package com.example.test.Controller;

import com.example.test.DTO.ApiResponse;
import com.example.test.DTO.UserRequestDTO;
import com.example.test.DTO.UserResponseDTO;
import com.example.test.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final String path = "/api/users";

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAll() {

        List<UserResponseDTO> users = userService.findAll();

        ApiResponse<List<UserResponseDTO>> response = ApiResponse.create(
                HttpStatus.OK,
                "users retrieved  successfully !",
                users,
                path
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/some_users")
    public ResponseEntity<ApiResponse<Page<UserResponseDTO>>> getSomeUsers(Pageable pageable) {

        Page<UserResponseDTO> users = userService.findSomeUsers(pageable);

        ApiResponse<Page<UserResponseDTO>> response = ApiResponse.create(
                HttpStatus.OK,
                "Users retrieved  successfully !",
                users,
                path + "/some_users"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/role")
    public ResponseEntity<ApiResponse<Page<UserResponseDTO>>> getByRole(
            @RequestParam String role,
            Pageable pageable) {

        Page<UserResponseDTO> users = userService.findByRole(role, pageable);

        ApiResponse<Page<UserResponseDTO>> response = ApiResponse.create(
                HttpStatus.OK,
                "Users retrieved  successfully",
                users,
                path + "/role"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getOne(@PathVariable String id) {
        UserResponseDTO user = userService.findById(id);

        ApiResponse<UserResponseDTO> response = ApiResponse.create(
                HttpStatus.OK,
                "User found successfully!",
                user,
                path + "/" + id
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/email")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getByEmail(@RequestParam String email) {
        UserResponseDTO user = userService.findByEmail(email);
        ApiResponse<UserResponseDTO> response = ApiResponse.create(
                HttpStatus.OK,
                "User found successfully ",
                user,
                path + "/email"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> create(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.create(userRequestDTO);
        ApiResponse<UserResponseDTO> response = ApiResponse.create(
                HttpStatus.CREATED,
                "User created successfully",
                userResponseDTO,
                path
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> update(
            @PathVariable String id,
            @RequestBody UserRequestDTO userRequestDTO
    ) {
        UserResponseDTO userUpdated = userService.update(id, userRequestDTO);
        ApiResponse<UserResponseDTO> response = ApiResponse.create(
                HttpStatus.OK,
                "User updated successfully",
                userUpdated,
                path + "/" + id
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/forget-password/send-otp")
    public ResponseEntity<ApiResponse<Void>> sendOtp(@RequestParam String email) {
        userService.sendOtp(email);
        ApiResponse<Void> response = ApiResponse.create(
                HttpStatus.OK,
                "Otp send to email successfully",
                null,
                path + "/forget-password/send-otp"
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/forget-password/confirmed")
    public ResponseEntity<ApiResponse<?>> forgetPassword(@RequestParam String email,
                                                         @RequestParam String otp,
                                                         @RequestBody String password) {
        userService.updatePassword(otp,email, password);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.create(
                HttpStatus.OK,
                "Password is updated successfully",
                null,
                STR."\{path}/forget-password"
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable String id) {
        userService.delete(id);
        ApiResponse<String> response = ApiResponse.create(
                HttpStatus.OK,
                "User deleted successfully",
                null,
                path + "/" + id
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

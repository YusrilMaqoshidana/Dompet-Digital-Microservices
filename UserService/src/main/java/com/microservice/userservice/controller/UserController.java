package com.microservice.userservice.controller;


import com.microservice.userservice.DTO.ApiResponse;
import com.microservice.userservice.DTO.UserDTOResponse;
import com.microservice.userservice.models.UserModel;
import com.microservice.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Get
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserModel>>> getAllUsers() {
        try {
            List<UserModel> users = userService.getAll();
            if (users.isEmpty()){
                return new ResponseEntity<>(
                        new ApiResponse<>(
                                HttpStatus.OK.value(),
                                "No users found",
                                users
                        ),
                        HttpStatus.OK
                );
            }
            ApiResponse<List<UserModel>> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Successfully get all users",
                    users
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {

            ApiResponse<List<UserModel>> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An error occurred while fetching users. %s." + e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<ApiResponse<UserModel>> getDetailUser(
            @RequestParam("user_id") String userId
    ) {
        try {
            UserModel users = userService.getByUserId(userId);
            if (users == null){
                return new ResponseEntity<>(
                        new ApiResponse<>(
                                HttpStatus.NOT_FOUND.value(),
                                "No users found"
                        ),
                        HttpStatus.NOT_FOUND
                );
            }
            ApiResponse<UserModel> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Successfully get detail users",
                    users
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {

            ApiResponse<UserModel> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An error occurred while fetching users. %s." + e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Post
    @PostMapping
    public ResponseEntity<ApiResponse<UserModel>> createUser(
            @RequestBody UserDTOResponse newUser
    ) {
        try{
            UserModel user = userService.create(newUser);
            ApiResponse<UserModel> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "User created successfully",
                    user
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            ApiResponse<UserModel> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An error : " + e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserModel>> updateUsername(
            @RequestParam("user_id") String userId,
            @RequestParam("username") String newName
    ) {
        try {
            UserModel users = userService.updateUsername(userId, newName);
            ApiResponse<UserModel> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Successfully get detail users",
                    users
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {

            ApiResponse<UserModel> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An error occurred while fetching users. %s." + e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @RequestParam("user_id") String userId
    ) {
        try {
            userService.delete(userId);

            ApiResponse<Void> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "User successfully deleted"
            );
            return ResponseEntity.ok(response);
        } catch (HttpServerErrorException e) {
            ApiResponse<Void> error = new ApiResponse<>(
                    e.getStatusCode().value(),
                    "User deletion failed due to an external service error. Details: " + e.getResponseBodyAsString() // Hati-hati
            );
            return new ResponseEntity<>(error, e.getStatusCode());
        } catch (Exception e) {
            ApiResponse<Void> error = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage()
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

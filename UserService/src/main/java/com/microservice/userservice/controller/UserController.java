package com.microservice.userservice.controller;


import com.microservice.userservice.DTO.ApiResponse;
import com.microservice.userservice.DTO.LoginDTOResponse;
import com.microservice.userservice.DTO.ProfileDTOResponse;
import com.microservice.userservice.DTO.RegisterDTOResponse;
import com.microservice.userservice.models.UserModel;
import com.microservice.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

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
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserModel>> registerUser(
            @RequestBody RegisterDTOResponse newUser
    ) {
        try{
            UserModel user = userService.register(newUser);
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

   @PostMapping("/login")
    public ResponseEntity<ApiResponse<Boolean>> loginUser(
            @RequestBody LoginDTOResponse userLogin
    ) {
        try{
            boolean isSuccesLogin = userService.login(userLogin);
            if (!isSuccesLogin){
                return new ResponseEntity<>(
                        new ApiResponse<>(
                                HttpStatus.NOT_FOUND.value(),
                                "Login failed"
                        ),
                        HttpStatus.NOT_FOUND
                );
            } else {
                return new ResponseEntity<>(
                        new ApiResponse<>(
                                HttpStatus.OK.value(),
                                "Login success"
                        ),
                        HttpStatus.OK
                );
            }

        } catch (Exception e) {
            ApiResponse<Boolean> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An error : " + e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping
    public ResponseEntity<ApiResponse<UserModel>> updateProfile(
            @RequestParam("user_id") String userId,
            @RequestBody ProfileDTOResponse newProfile
    ) {
        try {
            UserModel users = userService.updateProfile(userId, newProfile);
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
}

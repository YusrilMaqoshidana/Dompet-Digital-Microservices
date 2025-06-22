package com.microservice.userservice.DTO;

import lombok.Data;

@Data
public class RegisterDTOResponse{
    public String fullName;
    public String email;
    public String phoneNumber;
    public String password;
}
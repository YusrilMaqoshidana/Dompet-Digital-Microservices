package com.microservice.userservice.repository;

import com.microservice.userservice.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {
    UserModel getUserModelByUserId(String id);
//    UserModel getUserModelByUserId(UUID id);
}

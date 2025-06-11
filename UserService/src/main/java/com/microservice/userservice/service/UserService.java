package com.microservice.userservice.service;

import com.google.common.hash.HashCode;
import com.microservice.userservice.DTO.UserDTOResponse;
import com.microservice.userservice.models.UserModel;
import com.microservice.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserModel> getAll() {
        return userRepository.findAll();
    }

    public UserModel getByUserId(String userId){
        return userRepository.getUserModelByUserId(userId);
    }

    public UserModel updateUsername(String userId, String newName){
        UserModel user = userRepository.getUserModelByUserId(userId);
        user.setUsername(newName);
        return userRepository.save(user);
    }

    public void delete(String userId){
        userRepository.deleteById(userId);
    }

    public UserModel create(UserDTOResponse newUser) {
        UserModel user = toUserModel(newUser);
        return userRepository.save(user);
    }

    private UserModel toUserModel(UserDTOResponse dto) {
        UserModel user = new UserModel();
//        long generatedId = ThreadLocalRandom.current().nextLong(1_000_000_000L, 9_999_999_999L);
        String generatedId = UUID.randomUUID().toString();
        printHashUserId(generatedId, dto.getUsername());
        user.setUserId(generatedId);
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        return user;
    }

    private void printHashUserId(String userId, String username){
        if (userId == null) {
            System.err.println("Error: User ID cannot be null.");
            return; // Exit the method if userId is null.
        }
        int userIdHash = Math.abs(userId.hashCode());
        System.out.println("User ID Hash: " + userIdHash);
        if (userIdHash % 2 == 0) {
            // If the hash is even
            System.out.println(username + " Masuk ke ds_0 (even hash)");
        } else {
            // If the hash is odd
            System.out.println(username + " Masuk ke ds_1 (odd hash)");
        }
    }

}

package com.microservice.userservice.service;

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

    public UserModel getByUserId(Long userId){
        return userRepository.getUserModelByUserId(userId);
    }
//    public UserModel getByUserId(UUID userId){
//        return userRepository.getUserModelByUserId(userId);
//    }


    public UserModel create(UserDTOResponse newUser) {
        UserModel user = toUserModel(newUser);
        return userRepository.save(user);
    }

    public void delete(Long userId){
        userRepository.deleteById(userId);
    }
//    public void delete(UUID userId){
//        userRepository.deleteById(userId);
//    }

    private UserModel toUserModel(UserDTOResponse dto) {
        UserModel user = new UserModel();
        long generatedId = ThreadLocalRandom.current().nextLong(1_000_000_000L, 9_999_999_999L);
//        UUID generatedId = UUID.randomUUID();
        user.setUserId(generatedId);
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        return user;
    }

}

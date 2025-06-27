package com.microservice.userservice.service;

import com.microservice.userservice.DTO.LoginDTOResponse;
import com.microservice.userservice.DTO.ProfileDTOResponse;
import com.microservice.userservice.DTO.RegisterDTOResponse;
import com.microservice.userservice.models.UserModel;
import com.microservice.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserPublisherService userPublisherService;

    public List<UserModel> getAll() {
        return userRepository.findAll();
    }

    public UserModel getByUserId(String userId){
        return userRepository.getUserModelByUserId(userId);
    }

    public UserModel updateProfile(String userId, ProfileDTOResponse newProfile){
        UserModel user = userRepository.getUserModelByUserId(userId);
        user.setFullName(newProfile.fullName);
        user.setEmail(newProfile.email);
        user.setPhoneNumber(newProfile.phoneNumber);
        return userRepository.save(user);
    }



    public boolean login(LoginDTOResponse userLogin){
        String hashPassword = String.valueOf(userLogin.password.hashCode());
        return userRepository.findByEmailAndPassword(userLogin.email, hashPassword) != null;
    }
    public UserModel register(RegisterDTOResponse newUser) {
        UserModel user = toUserModel(newUser);
//        mengirim user_id ke topic user.created
        userPublisherService.publishUserCreatedEvent(user);
        return userRepository.save(user);
    }

    private UserModel toUserModel(RegisterDTOResponse dto) {
        UserModel user = new UserModel();
        String generatedId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        String hashPassword = String.valueOf(dto.getPassword().hashCode());
        printHashUserId(generatedId, dto.getFullName());
        user.setUserId(generatedId);
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(hashPassword);
        user.setActive(true);
        return user;
    }

    public UserModel updateStatusUser(String userId){
        UserModel user = userRepository.getUserModelByUserId(userId);
        user.setActive(!user.isActive());
        return userRepository.save(user);
    }

     private void printHashUserId(String userId, String username) {
            if (userId == null) {
                System.err.println("Error: User ID cannot be null.");
                return;
            }

            int userIdHash = Math.abs(userId.hashCode());
            System.out.println("User ID Hash: " + userIdHash);

            int shardKey = userIdHash % 3;

            switch (shardKey) {
                case 0:
                    // Jika sisa baginya 0
                    System.out.println(username + " masuk ke ds_0 (hash % 3 == 0)");
                    break;
                case 1:
                    // Jika sisa baginya 1
                    System.out.println(username + " masuk ke ds_1 (hash % 3 == 1)");
                    break;
                case 2:
                    // Jika sisa baginya 2
                    System.out.println(username + " masuk ke ds_2 (hash % 3 == 2)");
                    break;
            }
        }

}

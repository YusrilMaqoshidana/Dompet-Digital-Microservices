package com.microservice.notificationservice.controllers;


import com.microservice.notificationservice.DTO.ApiResponse;
import com.microservice.notificationservice.models.NotificationModel;
import com.microservice.notificationservice.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;


    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationModel>>> getUserReport(
            @PathVariable String userId
    ) {
        List<NotificationModel> notif = notificationService.getUserNotification(userId);

        if (notif == null) {
            ApiResponse<List<NotificationModel>> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Notification for user with ID " + userId + " not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        ApiResponse<List<NotificationModel>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Successfully retrieved notification details",
                notif
        );
        return ResponseEntity.ok(response);
    }
}

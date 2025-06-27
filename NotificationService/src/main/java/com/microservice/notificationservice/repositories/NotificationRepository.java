package com.microservice.notificationservice.repositories;

import com.microservice.notificationservice.models.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationModel, String> {
    List<NotificationModel> findNotificationModelByUserId(String userId);
}

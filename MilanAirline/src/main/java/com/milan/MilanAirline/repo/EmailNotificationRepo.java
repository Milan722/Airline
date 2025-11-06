package com.milan.MilanAirline.repo;

import com.milan.MilanAirline.entities.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailNotificationRepo extends JpaRepository<EmailNotification,Long> {
}

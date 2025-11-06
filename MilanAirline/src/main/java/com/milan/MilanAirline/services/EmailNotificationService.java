package com.milan.MilanAirline.services;

import com.milan.MilanAirline.entities.Booking;
import com.milan.MilanAirline.entities.User;

public interface EmailNotificationService {

    void sendBookingTicketEmail(Booking booking);
    void sendWelcomeEmail(User user);
}

package com.milan.MilanAirline.services.impl;


import com.milan.MilanAirline.entities.Booking;
import com.milan.MilanAirline.entities.EmailNotification;
import com.milan.MilanAirline.entities.User;
import com.milan.MilanAirline.repo.EmailNotificationRepo;
import com.milan.MilanAirline.services.EmailNotificationService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private final EmailNotificationRepo emailNotificationRepo;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final RestTemplate restTemplate;

    @Value("${frontendLoginUrl}")
    private String frontendLoginUrl;

    @Value("${viewBookingUrl}")
    private String viewBookingUrl;

    @Value("${resend.api.key:}")
    private String resendApiKey;

    @Value("${resend.from.email:}")
    private String resendFromEmail;

    @Value("${resend.api.url:https://api.resend.com}")
    private String resendApiUrl;

    @Override
    @Transactional
    @Async
    public void sendBookingTicketEmail(Booking booking) {
        log.info("Inside sendBookingTicketEmail()");
        String recipientEmail=booking.getUser().getEmail();
        String subject="Your Flight Booking Ticket - Reference";
        String templateName="booking_ticket";

        Map<String, Object> templateVariables=new HashMap<>();
        templateVariables.put("userName", booking.getUser().getName());
        templateVariables.put("bookingReference", booking.getBookingReference());
        templateVariables.put("flightNumber", booking.getFlight().getFlightNumber());
        templateVariables.put("departureAirportIataCode", booking.getFlight().getDepartureAirport().getIataCode());
        templateVariables.put("departureAirportName", booking.getFlight().getDepartureAirport().getName());
        templateVariables.put("departureAirportCity", booking.getFlight().getDepartureAirport().getCity());
        templateVariables.put("departureTime", booking.getFlight().getDepartureTime());
        templateVariables.put("arrivalAirportIataCode", booking.getFlight().getArrivalAirport().getIataCode());
        templateVariables.put("arrivalAirportName", booking.getFlight().getArrivalAirport().getName());
        templateVariables.put("arrivalAirportCity", booking.getFlight().getArrivalAirport().getCity());
        templateVariables.put("arrivalTime", booking.getFlight().getArrivalTime());
        templateVariables.put("basePrice", booking.getFlight().getBasePrice());
        templateVariables.put("passengers", booking.getPassengers());
        templateVariables.put("viewBookingUrl", viewBookingUrl);


        //Render the template content

        Context context=new Context();
        templateVariables.forEach(context::setVariable);
        String emailBody=templateEngine.process(templateName,context);

        //send the actual email with the template

        sendMailout(recipientEmail,subject,emailBody, true,booking);

    }

    @Override
    @Transactional
    @Async
    public void sendWelcomeEmail(User user) {
        log.info("Sending welcome email to user: {}", user.getEmail());

        String recipientEmail = user.getEmail();
        String subject = "Welcome to Milan Airline!";
        String templateName = "welcome_user"; // Hardcoded template name for internal use

        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("userName", user.getName());
        templateVariables.put("frontendLoginUrl", frontendLoginUrl);

        // Render the template content
        Context context = new Context();
        templateVariables.forEach(context::setVariable);
        String emailBody = templateEngine.process(templateName, context);

        sendMailout(recipientEmail, subject, emailBody, true, null);


    }

    private void sendMailout(String recipientEmail,String subject,String body,boolean isHtml, Booking booking){
        boolean emailSent = false;
        
        // Ako je Resend API key podešen, koristi Resend HTTP API
        if (resendApiKey != null && !resendApiKey.isEmpty() && 
            resendFromEmail != null && !resendFromEmail.isEmpty()) {
            try {
                log.info("Sending email via Resend API to: {}", recipientEmail);
                sendViaResend(recipientEmail, subject, body);
                emailSent = true;
                log.info("Email successfully sent via Resend API to: {}", recipientEmail);
            } catch (Exception e) {
                log.error("Failed to send email via Resend API: {}", e.getMessage(), e);
                // Fallback na SMTP ako Resend ne radi
                log.info("Falling back to SMTP...");
            }
        }
        
        // Ako Resend nije podešen ili je fallback, koristi SMTP
        if (!emailSent) {
            try {
                log.info("Sending email via SMTP to: {}", recipientEmail);
                MimeMessage mimeMessage=javaMailSender.createMimeMessage();
                MimeMessageHelper helper=new MimeMessageHelper(
                        mimeMessage,
                        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                        StandardCharsets.UTF_8.name()
                );
                helper.setTo(recipientEmail);
                helper.setSubject(subject);
                helper.setText(body,isHtml);

                javaMailSender.send(mimeMessage);
                emailSent = true;
                log.info("Email successfully sent via SMTP to: {}", recipientEmail);

            } catch (Exception e) {
                log.error("Mail server connection failed. Failed messages: {}", e.getMessage(), e);
            }
        }

        //save to the notification database table

        EmailNotification emailNotification=new EmailNotification();
        emailNotification.setRecipientEmail(recipientEmail);
        emailNotification.setSubject(subject);
        emailNotification.setBody(body);
        emailNotification.setHtml(isHtml);
        emailNotification.setSentAt(LocalDateTime.now());
        emailNotification.setBooking(booking);

        emailNotificationRepo.save(emailNotification);
        
        if (!emailSent) {
            log.warn("Email notification saved to database but email was not sent successfully to: {}", recipientEmail);
        }
    }

    private void sendViaResend(String to, String subject, String htmlBody) {
        try {
            String url = resendApiUrl + "/emails";
            
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            headers.setBearerAuth(resendApiKey);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("from", resendFromEmail);
            requestBody.put("to", new String[]{to});
            requestBody.put("subject", subject);
            requestBody.put("html", htmlBody);
            
            org.springframework.http.HttpEntity<Map<String, Object>> request = 
                new org.springframework.http.HttpEntity<>(requestBody, headers);
            
            restTemplate.postForEntity(url, request, Map.class);
        } catch (Exception e) {
            log.error("Error sending email via Resend API: {}", e.getMessage(), e);
            throw e;
        }
    }
}

package com.milan.MilanAirline;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableAsync;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
@EnableAsync
public class MilanAirlineApplication {

//    @Autowired
//    private JavaMailSender javaMailSender;

    public static void main(String[] args) {
        SpringApplication.run(MilanAirlineApplication.class, args);
    }

//    @Bean
//    CommandLineRunner runner(){
//        return args -> {
//
//            try {
//                MimeMessage mimeMessage=javaMailSender.createMimeMessage();
//                MimeMessageHelper helper=new MimeMessageHelper(
//                        mimeMessage,
//                        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
//                        StandardCharsets.UTF_8.name()
//                );
//                helper.setTo("anicicmilan72@gmail.com");
//                helper.setSubject("Hello Testing");
//                helper.setText("testing email 123,hello world");
//
//                    System.out.println("About to send email...");
//                javaMailSender.send(mimeMessage);
//
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        };
//    }

}

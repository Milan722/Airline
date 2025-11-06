package com.milan.MilanAirline.controller;


import com.milan.MilanAirline.dtos.*;
import com.milan.MilanAirline.enums.BookingStatus;
import com.milan.MilanAirline.services.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;



    @PostMapping
    public ResponseEntity<Response<?>> createBooking(@Valid @RequestBody CreateBookingRequest createBookingRequest){

        return ResponseEntity.ok(bookingService.createBooking(createBookingRequest));
    }
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','PILOT')")
    public ResponseEntity<Response<List<BookingDTO>>> getAllBookings(){


        return ResponseEntity.ok(bookingService.getAllBookings());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Response<BookingDTO>> getBookingById(@PathVariable Long id){


        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<Response<List<BookingDTO>>> getMyBookings(){


        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','PILOT')")
    public ResponseEntity<Response<?>> updateBookingStatus(@PathVariable Long id, @RequestBody BookingStatus status){

        return ResponseEntity.ok(bookingService.updateBookingStatus(id,status));
    }




}

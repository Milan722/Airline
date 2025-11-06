package com.milan.MilanAirline.services;

import com.milan.MilanAirline.dtos.BookingDTO;
import com.milan.MilanAirline.dtos.CreateBookingRequest;
import com.milan.MilanAirline.dtos.Response;
import com.milan.MilanAirline.enums.BookingStatus;

import java.util.List;

public interface BookingService {

    Response<?> createBooking(CreateBookingRequest createBookingRequest);
    Response<BookingDTO> getBookingById(Long id);
    Response<List<BookingDTO>> getAllBookings();
    Response<List<BookingDTO>> getMyBookings();
    Response<?> updateBookingStatus(Long id, BookingStatus status);
}

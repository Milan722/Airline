package com.milan.MilanAirline.services.impl;


import com.milan.MilanAirline.dtos.BookingDTO;
import com.milan.MilanAirline.dtos.CreateBookingRequest;
import com.milan.MilanAirline.dtos.Response;
import com.milan.MilanAirline.entities.Booking;
import com.milan.MilanAirline.entities.Flight;
import com.milan.MilanAirline.entities.Passenger;
import com.milan.MilanAirline.entities.User;
import com.milan.MilanAirline.enums.BookingStatus;
import com.milan.MilanAirline.enums.FlightStatus;
import com.milan.MilanAirline.exceptions.BadRequestException;
import com.milan.MilanAirline.exceptions.NotFoundException;
import com.milan.MilanAirline.repo.BookingRepo;
import com.milan.MilanAirline.repo.FlightRepo;
import com.milan.MilanAirline.repo.PassengerRepo;
import com.milan.MilanAirline.services.BookingService;
import com.milan.MilanAirline.services.EmailNotificationService;
import com.milan.MilanAirline.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepo bookingRepo;
    private final UserService userService;
    private final FlightRepo flightRepo;
    private final PassengerRepo passengerRepo;
    private final ModelMapper modelMapper;
    private final EmailNotificationService emailNotificationService;



    @Override
    @Transactional
    public Response<?> createBooking(CreateBookingRequest createBookingRequest) {

        User user = userService.currentUser();

        Flight flight = flightRepo.findById(createBookingRequest.getFlightid())
                .orElseThrow(()-> new NotFoundException("Flight Not Found"));

        if (flight.getStatus() != FlightStatus.SCHEDULED){
            throw new BadRequestException("You can only book a flight that is scheduled");
        }

        Booking booking = new Booking();
        booking.setBookingReference(generateBookingReference());
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking savedBooking = bookingRepo.save(booking);

        if (createBookingRequest.getPassengers() != null && !createBookingRequest.getPassengers().isEmpty()){

            List<Passenger> passengers = createBookingRequest.getPassengers().stream()
                    .map(passengerDTO -> {
                        Passenger passenger = modelMapper.map(passengerDTO, Passenger.class);
                        passenger.setBooking(savedBooking);
                        return passenger;
                    }).toList();

            passengerRepo.saveAll(passengers);
            savedBooking.setPassengers(passengers);
        }

        //SEND EMAIL TICKER OUT
        emailNotificationService.sendBookingTicketEmail(savedBooking);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Booking created successfully")
                .build();

    }

    @Override
    public Response<BookingDTO> getBookingById(Long id) {
        Booking booking=bookingRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        BookingDTO bookingDTO=modelMapper.map(booking,BookingDTO.class);
        bookingDTO.getFlight().setBookings(null);

        return Response.<BookingDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Booking retrieved successfully")
                .data(bookingDTO)
                .build();
    }

    @Override
    public Response<List<BookingDTO>> getAllBookings() {
        List<Booking> allBookings=bookingRepo.findAll(Sort.by(Sort.Direction.DESC,"id"));
        List <BookingDTO> bookings=allBookings.stream()
                .map(booking -> {
                    BookingDTO bookingDTO=modelMapper.map(booking,BookingDTO.class);
                    bookingDTO.getFlight().setBookings(null);
                    return bookingDTO;

                }).toList();

        return Response.<List<BookingDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(bookings.isEmpty()?"No booking found":"Booking retrieved successfully")
                .data(bookings)
                .build();
    }

    @Override
    public Response<List<BookingDTO>> getMyBookings() {
        User user=userService.currentUser();
        List<Booking> userBookings=bookingRepo.findByUserIdOrderByIdDesc(user.getId());


        List <BookingDTO> bookings=userBookings.stream()
                .map(booking -> {
                    BookingDTO bookingDTO=modelMapper.map(booking,BookingDTO.class);
                    bookingDTO.getFlight().setBookings(null);
                    return bookingDTO;

                }).toList();

        return Response.<List<BookingDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(bookings.isEmpty()?"No booking found for this user":"User bookings retrieved successfully")
                .data(bookings)
                .build();
    }

    @Override
    @Transactional
    public Response<?> updateBookingStatus(Long id, BookingStatus status) {
        Booking booking=bookingRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        booking.setStatus(status);
        bookingRepo.save(booking);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Booking updated successfully")
                .build();

    }




    //implement to make sure the booking reference doesn't already exist

    private String generateBookingReference(){
        return UUID.randomUUID().toString().substring(0,8).toUpperCase();
    }
}

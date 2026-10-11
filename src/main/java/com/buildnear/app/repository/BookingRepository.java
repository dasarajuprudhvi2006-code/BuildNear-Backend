package com.buildnear.app.repository;

import com.buildnear.app.entity.Booking;
import com.buildnear.app.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingNumber(String bookingNumber);
    List<Booking> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<Booking> findByProfessionalIdOrderByCreatedAtDesc(Long professionalId);
    List<Booking> findByStatusOrderByCreatedAtDesc(BookingStatus status);
    List<Booking> findAllByOrderByCreatedAtDesc();
    long countByStatus(BookingStatus status);
}

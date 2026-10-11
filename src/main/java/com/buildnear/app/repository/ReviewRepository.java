package com.buildnear.app.repository;

import com.buildnear.app.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProfessionalIdOrderByCreatedAtDesc(Long professionalId);
    List<Review> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    Optional<Review> findByBookingId(Long bookingId);
    List<Review> findAllByOrderByCreatedAtDesc();
}

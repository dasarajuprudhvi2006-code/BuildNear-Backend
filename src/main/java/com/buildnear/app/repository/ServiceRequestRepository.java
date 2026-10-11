package com.buildnear.app.repository;

import com.buildnear.app.entity.RequestStatus;
import com.buildnear.app.entity.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<ServiceRequest> findByCategoryIgnoreCaseOrderByCreatedAtDesc(String category);
    List<ServiceRequest> findByTargetProfessionalIdOrderByCreatedAtDesc(Long targetProfessionalId);
    List<ServiceRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status);
    List<ServiceRequest> findAllByOrderByCreatedAtDesc();
}

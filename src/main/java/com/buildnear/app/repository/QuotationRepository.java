package com.buildnear.app.repository;

import com.buildnear.app.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    List<Quotation> findByServiceRequestIdOrderByCreatedAtDesc(Long serviceRequestId);
    List<Quotation> findByProfessionalIdOrderByCreatedAtDesc(Long professionalId);
}

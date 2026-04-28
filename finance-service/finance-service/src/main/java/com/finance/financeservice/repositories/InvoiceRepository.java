package com.finance.financeservice.repositories;

import com.finance.financeservice.entities.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {
    List<Invoice> findByStudentId(String studentId);
    List<Invoice> findByStudentIdAndStatus(String studentId, String status);
}

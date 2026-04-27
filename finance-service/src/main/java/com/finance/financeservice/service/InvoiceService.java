package com.finance.financeservice.service;

import com.finance.financeservice.dto.InvoiceRequestDTO;
import com.finance.financeservice.entities.Account;
import com.finance.financeservice.entities.Invoice;
import com.finance.financeservice.repositories.AccountRepository;
import com.finance.financeservice.repositories.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final AccountRepository accountRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, AccountRepository accountRepository) {
        this.invoiceRepository = invoiceRepository;
        this.accountRepository = accountRepository;
    }

    public Invoice createInvoice(InvoiceRequestDTO request) {
        Account account = accountRepository.findByStudentId(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Account not found for student"));

        String reference = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Invoice invoice = new Invoice();
        invoice.setReference(reference);
        invoice.setStudentId(request.getStudentId());
        invoice.setAmount(request.getAmount());
        invoice.setDueDate(request.getDueDate() != null ? request.getDueDate() : LocalDate.now().plusDays(30));
        invoice.setType(request.getType() != null ? request.getType() : "TUITION_FEES");
        invoice.setStatus("OUTSTANDING");
        invoice.setCreatedAt(LocalDateTime.now());

        Invoice savedInvoice = invoiceRepository.save(invoice);

        account.getInvoiceIds().add(savedInvoice.getId());
        accountRepository.save(account);

        return savedInvoice;
    }

    public List<Invoice> getInvoicesByStudentId(String studentId) {
        return invoiceRepository.findByStudentId(studentId);
    }

    public List<Invoice> getOutstandingInvoices(String studentId) {
        return invoiceRepository.findByStudentIdAndStatus(studentId, "OUTSTANDING");
    }

    public Invoice payInvoice(String invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if ("PAID".equals(invoice.getStatus())) {
            throw new RuntimeException("Invoice already paid");
        }

        invoice.setStatus("PAID");
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
}

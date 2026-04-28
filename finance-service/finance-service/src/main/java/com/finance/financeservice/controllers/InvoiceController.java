package com.finance.financeservice.controllers;

import com.finance.financeservice.dto.InvoiceRequestDTO;
import com.finance.financeservice.entities.Invoice;
import com.finance.financeservice.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
@CrossOrigin
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceRequestDTO request) {
        try {
            Invoice invoice = invoiceService.createInvoice(request);
            return ResponseEntity.ok(invoice);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/student/{studentId}")
    public List<Invoice> getInvoicesByStudentId(@PathVariable String studentId) {
        return invoiceService.getInvoicesByStudentId(studentId);
    }

    @GetMapping("/student/{studentId}/outstanding")
    public List<Invoice> getOutstandingInvoices(@PathVariable String studentId) {
        return invoiceService.getOutstandingInvoices(studentId);
    }

    @PutMapping("/{invoiceId}/pay")
    public ResponseEntity<?> payInvoice(@PathVariable String invoiceId) {
        try {
            Invoice invoice = invoiceService.payInvoice(invoiceId);
            return ResponseEntity.ok(invoice);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

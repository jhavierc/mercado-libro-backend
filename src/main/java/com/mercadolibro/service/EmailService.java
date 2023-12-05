package com.mercadolibro.service;

import com.mercadolibro.events.InvoiceCreated;
import org.springframework.context.event.EventListener;

import java.util.List;

public interface EmailService {
    void sendEmail(String to, String subject, String userName, List<String> messages);

    @EventListener
    void handleInvoiceCreatedEvent(InvoiceCreated invoiceCreated);
}

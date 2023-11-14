package com.mercadolibro.service;

import java.util.List;

public interface EmailService {
    void sendEmail(String to, String subject, String userName, List<String> messages);
}

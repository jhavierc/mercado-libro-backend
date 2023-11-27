package com.mercadolibro.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {
    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    void sendEmail_shouldSendEmail() {
        // GIVEN
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String userName = "TestUser";
        List<String> messages = Arrays.asList("Message 1", "Message 2");

        // WHEN
        when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));

        // THEN
        emailService.sendEmail(to, subject, userName, messages);
        verify(javaMailSender, times(1)).send((MimeMessage) any());

    }

}
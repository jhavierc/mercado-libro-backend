package com.mercadolibro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailServiceImpl implements com.mercadolibro.service.EmailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String userName, List<String> messages) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><style>" +
                "body { font-family: Arial, sans-serif; background-color: #8884ff; margin: 0; padding: 0; }" +
                "header { background-color: #8884ff; color: #fff; text-align: center; padding: 20px; }" +
                "h1 { color: #003844; font-size: 24px; }" +
                "p { color: #333; font-size: 16px; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #fff; border: 1px solid #ccc; border-radius: 5px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); }" +
                ".logo { display: block; margin: 0 auto; }" +
                ".greeting { text-align: center; font-size: 24px; margin-top: 20px; }" +
                ".message { font-size: 16px; margin-top: 20px; }" +
                ".signature { font-weight: bold; }" +
                "</style></head><body>" +
                "<header>" +
                "<img class='logo' src='https://i.ibb.co/XWbLRvL/Group.png' alt='logo' width='100' height='100'>" +
                "<h1>" + subject + "</h1>" +
                "</header>" +
                "<div class='container'>" +
                "<p class='greeting'>Estimado " + userName + ",</p>");
        for (String message: messages) {
            sb.append("<p class='message'>").append(message).append("</p>");
        }

        sb.append("<p class='signature'>Saludos atentamente,</p>" +
                "<p class='signature'>El equipo de MercadoLibro</p>" +
                "</div>" +
                "</body></html>");

        MimeMessage message = javaMailSender.createMimeMessage();
        String htmlContent = sb.toString();
        try {
            message.setContent(htmlContent, "text/html");
            message.setSubject(subject);
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

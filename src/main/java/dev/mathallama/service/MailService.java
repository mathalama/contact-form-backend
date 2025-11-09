package dev.mathallama.service;

import dev.mathallama.dto.ContactRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final String owner;

    public MailService(JavaMailSender mailSender,
                       @Value("${MAIL_USER}") String owner) {
        this.mailSender = mailSender;
        this.owner = owner;
    }

    @Async
    public void sendAutoReply(String to, String name) {
        SimpleMailMessage m = new SimpleMailMessage();
        m.setFrom(owner);
        m.setTo(to);
        m.setSubject("✅ Your message has been received");

        m.setText(
                "Hello " + name + ",\n\n" +
                        "Thank you for contacting Mathalama.\n" +
                        "We have successfully received your message.\n" +
                        "Our team will review it and get back to you as soon as possible.\n\n" +
                        "Best regards,\n" +
                        "Mathalama Support Team"
        );
        mailSender.send(m);
    }

    @Async
    public void notifyOwner(ContactRequest r) {
        SimpleMailMessage m = new SimpleMailMessage();
        m.setFrom(owner);
        m.setTo(owner);
        m.setSubject("New contact form message");

        m.setText(
                "From: " + r.name() + "\n" +
                        "Email: " + r.email() + "\n\n" +
                        r.message()
        );

        mailSender.send(m);
    }
}

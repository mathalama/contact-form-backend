package dev.mathallama.service;

import dev.mathallama.dto.ContactRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender mail;
    private final String owner;

    public MailService(JavaMailSender mail,
                       org.springframework.core.env.Environment env) {
        this.mail = mail;
        this.owner = env.getProperty("spring.mail.username");
    }

    public void sendAutoReply(String to, String name) {
        var m = new SimpleMailMessage();
        m.setFrom("Mathalama Support  <" + owner + ">");
        m.setTo(to);
        m.setSubject("✅ Your message has been received");

        m.setText(
                "Hello " + name + ",\n\n" +
                        "Thank you for contacting Mathalama.\n" +
                        "We have successfully received your message.\n" +
                        "Our team will review it and get back to you as soon as possible.\n\n" +
                        "Best regards,\n" +
                        "Mathalama Support Team     ♥\uFE0F"
        );

        mail.send(m);
    }


    public void notifyOwner(ContactRequest r) {
        var m = new SimpleMailMessage();
        m.setTo(owner);
        m.setSubject("New contact form message");
        m.setText("From: %s\nEmail: %s\n\n%s".formatted(r.name(), r.email(), r.message()));
        mail.send(m);
    }
}

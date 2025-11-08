package dev.mathallama.controller;

import dev.mathallama.dto.ContactRequest;
import dev.mathallama.service.MailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5500","http://127.0.0.1:5500","https://your-frontend-domain"})
public class ContactController {
    private final MailService mail;

    public ContactController(MailService mail) { this.mail = mail; }

    @PostMapping("/contact")
    public ResponseEntity<?> handle(@Valid @RequestBody ContactRequest req) {
        mail.notifyOwner(req);
        mail.sendAutoReply(req.email(), req.name());
        return ResponseEntity.ok().body("{\"ok\":true}");
    }
}

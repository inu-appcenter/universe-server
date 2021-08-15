package org.inu.universe.service;

import org.inu.universe.domain.Email;
import org.inu.universe.model.email.EmailSaveRequest;
import org.inu.universe.model.email.EmailRequest;

public interface EmailService {
    void sendEmail(EmailRequest request);

    Email authEmail(EmailSaveRequest request);
}

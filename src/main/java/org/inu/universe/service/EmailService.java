package org.inu.universe.service;

import org.inu.universe.domain.Email;
import org.inu.universe.model.email.EmailAuthRequest;
import org.inu.universe.model.email.EmailSaveRequest;

public interface EmailService {
    Email sendEmail(EmailSaveRequest request);

    Email authEmail(EmailAuthRequest request);
}

package org.inu.universe.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.inu.universe.domain.status.EmailStatus;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String address;

    @Enumerated(EnumType.STRING)
    private EmailStatus status;

    public static Email saveEmail(String address) {
        Email email = new Email();
        email.address = address;
        email.status = EmailStatus.NOTAUTH;
        return email;
    }

    public static Email saveAdminEmail(String address) {
        Email email = new Email();
        email.address = address;
        email.status = EmailStatus.AUTH;
        return email;
    }

    public void authEmail() {
        this.status = EmailStatus.AUTH;
    }
}

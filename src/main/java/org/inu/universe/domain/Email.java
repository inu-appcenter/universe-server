package org.inu.universe.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Email extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String address;

    public static Email saveEmail(String address) {
        Email email = new Email();
        email.address = address;
        return email;
    }

    public static Email saveAdminEmail(String address) {
        Email email = new Email();
        email.address = address;
        return email;
    }

}

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
public class IdealType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idealType_id")
    private Long id;

    private String region;

    private String gender;              // 여성, 남성, 무관

    private Integer age;

    private String college;

    public static IdealType saveIdealType(String region, String gender, Integer age, String college) {
        IdealType idealType = new IdealType();
        idealType.region = region;
        idealType.gender = gender;
        idealType.age = age;
        idealType.college = college;
        return idealType;
    }

    public void updateIdealType(String region, String gender, Integer age, String college) {
        this.region = region;
        this.gender = gender;
        this.age = age;
        this.college = college;
    }
}

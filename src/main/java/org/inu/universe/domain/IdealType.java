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

    private Integer age1;

    private Integer age2;
    
    public static IdealType saveIdealType(String region, String gender, Integer age1, Integer age2) {
        IdealType idealType = new IdealType();
        idealType.region = region;
        idealType.gender = gender;
        idealType.age1 = age1;
        idealType.age2 = age2;
        return idealType;
    }

    public void updateIdealType(String region, String gender, Integer age1, Integer age2) {
        this.region = region;
        this.gender = gender;
        this.age1 = age1;
        this.age2 = age2;
    }
}

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
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashTag_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public static HashTag saveHashTag(String name) {
        HashTag hashTag = new HashTag();
        hashTag.name = name;
        return hashTag;
    }
}

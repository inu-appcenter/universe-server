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
public class Recommend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private Long recommendProfileId;

    public static Recommend saveRecommend(Account account, Long recommendProfileId) {
        Recommend recommend = new Recommend();
        recommend.account = account;
        recommend.recommendProfileId = recommendProfileId;
        return recommend;
    }
}

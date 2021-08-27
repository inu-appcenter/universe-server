package org.inu.universe.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.inu.universe.domain.Profile;
import org.inu.universe.domain.QProfile;
import org.inu.universe.domain.QRecommend;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecommendQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Profile> findRecommendProfile(Long accountId) {
        List<Long> profileIds = queryFactory.select(QRecommend.recommend.recommendProfileId)
                .from(QRecommend.recommend)
                .where(QRecommend.recommend.account.id.eq(accountId))
                .fetch();

        return queryFactory.select(QProfile.profile)
                .from(QProfile.profile)
                .where(QProfile.profile.id.in(profileIds))
                .fetch();
    }

    public void dislikeProfile(Long accountId, Long profileId) {
        queryFactory.delete(QRecommend.recommend)
                .where(QRecommend.recommend.account.id.eq(accountId), QRecommend.recommend.recommendProfileId.eq(profileId))
                .execute();
    }
}

package org.inu.universe.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.inu.universe.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProfileQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Profile findWithProfileTags(Long profileId) {
        return queryFactory.select(QProfile.profile).distinct()
                .from(QProfile.profile)
                .leftJoin(QProfile.profile.profileTagList, QProfileTag.profileTag).fetchJoin()
                .leftJoin(QProfileTag.profileTag.hashTag, QHashTag.hashTag).fetchJoin()
                .where(QProfile.profile.id.eq(profileId))
                .fetchOne();
    }

    public List<Profile> findAllProfile() {
        return queryFactory.select(QProfile.profile).distinct()
                .from(QProfile.profile)
                .leftJoin(QProfile.profile.profileTagList, QProfileTag.profileTag).fetchJoin()
                .leftJoin(QProfileTag.profileTag.hashTag, QHashTag.hashTag).fetchJoin()
                .fetch();
    }
}

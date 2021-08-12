package org.inu.universe.repository;

import org.inu.universe.domain.Profile;
import org.inu.universe.domain.ProfileTag;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProfileTagRepository extends JpaRepository<ProfileTag, Long> {

    void deleteByProfile(Profile profile);
}

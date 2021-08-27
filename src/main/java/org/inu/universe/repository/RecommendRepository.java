package org.inu.universe.repository;

import org.inu.universe.domain.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecommendRepository extends JpaRepository<Recommend, Long> {
}

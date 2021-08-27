package org.inu.universe.repository;

import org.inu.universe.domain.Recommend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RecommendJdbcRepository{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int[] batchUpdate(final List<Recommend> recommendList) {
        return this.jdbcTemplate.batchUpdate(
                "insert into recommend (account_id, recommend_profile_id) values (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Recommend recommend = recommendList.get(i);
                        ps.setLong(1, recommend.getAccount().getId().longValue());
                        ps.setLong(2,recommend.getRecommendProfileId().longValue());
                    }
                    public int getBatchSize() {
                        return recommendList.size();
                    }
                });
    }
}

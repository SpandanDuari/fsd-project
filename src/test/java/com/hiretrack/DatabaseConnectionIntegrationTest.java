package com.hiretrack;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class DatabaseConnectionIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void databaseConnectionAndSeedDataAreAvailable() {
        Integer one = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertThat(one).isEqualTo(1);

        Integer companyCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM companies", Integer.class);
        Integer recruiterCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM recruiters", Integer.class);
        Integer candidateCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM candidates", Integer.class);
        Integer jobCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM jobs", Integer.class);
        Integer applicationCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM applications", Integer.class);

        assertThat(companyCount).isGreaterThanOrEqualTo(3);
        assertThat(recruiterCount).isGreaterThanOrEqualTo(3);
        assertThat(candidateCount).isGreaterThanOrEqualTo(3);
        assertThat(jobCount).isGreaterThanOrEqualTo(3);
        assertThat(applicationCount).isGreaterThanOrEqualTo(3);
    }
}

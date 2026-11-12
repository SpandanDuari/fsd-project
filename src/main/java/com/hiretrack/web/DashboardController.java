package com.hiretrack.web;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final JdbcTemplate db;
    public DashboardController(JdbcTemplate db) { this.db = db; }

    @GetMapping("/candidate")
    public Map<String, Object> candidate(Authentication auth) {
        Long candidateId = db.queryForObject("SELECT candidate_id FROM users WHERE email = ?", Long.class, auth.getName());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("applications", count("SELECT COUNT(*) FROM applications WHERE candidate_id = ?", candidateId));
        result.put("interviews", count("SELECT COUNT(*) FROM interviews i JOIN applications a ON a.id = i.application_id WHERE a.candidate_id = ?", candidateId));
        result.put("unread", count("SELECT COUNT(*) FROM notifications n JOIN users u ON u.id = n.user_id WHERE u.email = ? AND n.read_at IS NULL", auth.getName()));
        return result;
    }

    @GetMapping("/recruiter")
    public Map<String, Object> recruiter(Authentication auth) {
        Long recruiterId = db.queryForObject("SELECT recruiter_id FROM users WHERE email = ?", Long.class, auth.getName());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("activeRoles", count("SELECT COUNT(*) FROM jobs j JOIN recruiters r ON r.company_id = j.company_id WHERE r.id = ?", recruiterId));
        result.put("candidates", count("SELECT COUNT(*) FROM applications a JOIN jobs j ON j.id = a.job_id JOIN recruiters r ON r.company_id = j.company_id WHERE r.id = ?", recruiterId));
        result.put("unread", 0);
        return result;
    }

    @GetMapping("/admin")
    public Map<String, Object> admin() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("users", count("SELECT COUNT(*) FROM users"));
        result.put("jobs", count("SELECT COUNT(*) FROM jobs"));
        result.put("applications", count("SELECT COUNT(*) FROM applications"));
        result.put("candidates", count("SELECT COUNT(*) FROM candidates"));
        return result;
    }

    private int count(String sql, Object... args) {
        Integer value = db.queryForObject(sql, Integer.class, args);
        return value == null ? 0 : value;
    }
}

package com.hiretrack.web;

import com.hiretrack.model.User;
import com.hiretrack.repo.UserRepository;
import jakarta.transaction.Transactional;
import java.security.SecureRandom;
import java.util.*;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final JdbcTemplate db;
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final SecureRandom random = new SecureRandom();

    AdminController(JdbcTemplate db, UserRepository users, PasswordEncoder encoder) {
        this.db = db;
        this.users = users;
        this.encoder = encoder;
    }

    @GetMapping("/summary")
    Map<String, Object> summary() {
        Map<String, Object> result = new LinkedHashMap<>();
        for (String table : List.of("users", "companies", "recruiters", "candidates", "jobs", "applications", "interviews")) {
            result.put(table, db.queryForObject("select count(*) from " + table, Integer.class));
        }
        return result;
    }

    @GetMapping("/candidates")
    List<Map<String, Object>> candidates() {
        return db.queryForList("""
            select u.id as user_id, u.email, u.is_active as active, u.created_at as account_created_at,
                   c.id, c.first_name, c.last_name, c.city, c.skills,
                   count(a.id) as application_count
            from users u join candidates c on c.id = u.candidate_id
            left join applications a on a.candidate_id = c.id
            where u.role = 'CANDIDATE'
            group by u.id, c.id
            order by u.created_at desc
            """);
    }

    @GetMapping("/recruiters")
    List<Map<String, Object>> recruiters() {
        return db.queryForList("""
            select u.id as user_id, u.email, u.is_active as active, u.created_at as account_created_at,
                   r.id, r.name, r.phone, c.name as company_name, c.location
            from users u join recruiters r on r.id = u.recruiter_id
            join companies c on c.id = r.company_id
            where u.role = 'RECRUITER'
            order by u.created_at desc
            """);
    }

    @PatchMapping("/accounts/{id}/status")
    ResponseEntity<?> setStatus(@PathVariable Long id, @RequestBody StatusRequest request, Authentication authentication) {
        Optional<User> account = users.findById(id);
        if (account.isEmpty() || "ADMIN".equals(account.get().getRole())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Managed account not found"));
        }
        if (authentication != null && authentication.getName().equalsIgnoreCase(account.get().getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "You cannot change your own account status"));
        }
        account.get().setActive(request.active());
        users.save(account.get());
        return ResponseEntity.ok(Map.of("id", id, "active", request.active()));
    }

    @PostMapping("/accounts/{id}/reset-password")
    ResponseEntity<?> resetPassword(@PathVariable Long id) {
        Optional<User> account = users.findById(id);
        if (account.isEmpty() || "ADMIN".equals(account.get().getRole())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Managed account not found"));
        }
        String temporaryPassword = temporaryPassword();
        User user = account.get();
        user.setPasswordHash(encoder.encode(temporaryPassword));
        user.setActive(true);
        users.save(user);
        return ResponseEntity.ok(Map.of("temporaryPassword", temporaryPassword, "message", "Share this temporary password securely and ask the user to change it."));
    }

    @DeleteMapping("/accounts/{id}")
    @Transactional
    ResponseEntity<?> deleteAccount(@PathVariable Long id, Authentication authentication) {
        Optional<User> account = users.findById(id);
        if (account.isEmpty() || "ADMIN".equals(account.get().getRole())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Managed account not found"));
        }
        if (authentication != null && authentication.getName().equalsIgnoreCase(account.get().getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "You cannot delete your own account"));
        }
        User user = account.get();
        users.delete(user);
        users.flush();
        if ("CANDIDATE".equals(user.getRole()) && user.getCandidateId() != null) {
            db.update("delete from candidates where id = ?", user.getCandidateId());
        } else if ("RECRUITER".equals(user.getRole()) && user.getRecruiterId() != null) {
            db.update("delete from recruiters where id = ?", user.getRecruiterId());
        }
        return ResponseEntity.noContent().build();
    }

    private String temporaryPassword() {
        String alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%";
        StringBuilder value = new StringBuilder(14);
        for (int i = 0; i < 14; i++) value.append(alphabet.charAt(random.nextInt(alphabet.length())));
        return value.toString();
    }

    record StatusRequest(boolean active) {}
}

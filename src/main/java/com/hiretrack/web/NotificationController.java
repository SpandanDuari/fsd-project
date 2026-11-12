package com.hiretrack.web;
import com.hiretrack.model.*; import com.hiretrack.repo.*; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import java.time.Instant; import java.util.*;
@RestController @RequestMapping("/api/notifications") public class NotificationController {final NotificationRepository repo;final UserRepository users;NotificationController(NotificationRepository r,UserRepository u){repo=r;users=u;}
 @GetMapping List<Notification> all(Authentication a){return repo.findByUserIdOrderByCreatedAtDesc(users.findByEmail(a.getName()).orElseThrow().getId());}
 @PatchMapping("/{id}/read") Notification read(@PathVariable Long id,Authentication a){Notification n=repo.findById(id).orElseThrow();n.readAt=Instant.now();return repo.save(n);}
}

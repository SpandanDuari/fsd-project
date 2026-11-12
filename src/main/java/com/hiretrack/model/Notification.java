package com.hiretrack.model;
import jakarta.persistence.*; import java.time.Instant;
@Entity @Table(name="notifications") public class Notification {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id; @Column(name="user_id") public Long userId; public String title,message;
 @Column(name="read_at") public Instant readAt; @Column(name="created_at") public Instant createdAt;
}

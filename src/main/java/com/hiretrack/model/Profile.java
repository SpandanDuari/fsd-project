package com.hiretrack.model;
import jakarta.persistence.*; import java.time.Instant;
@Entity @Table(name="profiles") public class Profile {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id; @Column(name="user_id") public Long userId; public String headline,bio;
 @Column(name="resume_url") public String resumeUrl; public String skills; @Column(name="updated_at") public Instant updatedAt;
 @Column(name="resume_name") public String resumeName; @Column(name="resume_content") public String resumeContent;
}

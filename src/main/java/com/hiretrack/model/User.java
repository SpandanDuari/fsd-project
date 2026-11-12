package com.hiretrack.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="users")
public class User {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(unique=true,nullable=false) private String email;
 @Column(name="password_hash",nullable=false) private String passwordHash;
 @Column(nullable=false) private String role="CANDIDATE";
 @Column(name="candidate_id") private Long candidateId;
 @Column(name="recruiter_id") private Long recruiterId;
 @Column(name="is_active",nullable=false) private boolean active=true;
 @Column(name="created_at") private Instant createdAt;
 public User() {}
 @PrePersist void onCreate(){if(createdAt==null)createdAt=Instant.now();}
 public Long getId(){return id;} public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String v){passwordHash=v;}
 public String getRole(){return role;} public void setRole(String v){role=v;} public Long getCandidateId(){return candidateId;} public void setCandidateId(Long v){candidateId=v;}
 public Long getRecruiterId(){return recruiterId;} public void setRecruiterId(Long v){recruiterId=v;}
 public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}

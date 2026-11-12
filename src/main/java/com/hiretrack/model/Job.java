package com.hiretrack.model;
import jakarta.persistence.*; import java.time.Instant;
@Entity @Table(name="jobs") public class Job {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id; @Column(name="company_id") public Long companyId;
 public String title,location; @Column(name="employment_type") public String employmentType;
 @Column(name="salary_min") public Integer salaryMin; @Column(name="salary_max") public Integer salaryMax; public String description; @Column(name="created_at") public Instant createdAt;
}

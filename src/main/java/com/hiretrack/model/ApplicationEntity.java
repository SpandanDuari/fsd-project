package com.hiretrack.model;
import jakarta.persistence.*; import java.time.Instant;
@Entity @Table(name="applications") public class ApplicationEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id; @Column(name="job_id") public Long jobId; @Column(name="candidate_id") public Long candidateId;
 public String status; @Column(name="cover_letter") public String coverLetter; @Column(name="applied_at") public Instant appliedAt;
}

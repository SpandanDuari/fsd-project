package com.hiretrack.model;
import jakarta.persistence.*; import java.time.Instant;
@Entity @Table(name="interviews") public class Interview {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id; @Column(name="application_id") public Long applicationId;
 @Column(name="scheduled_at") public Instant scheduledAt; public String mode,location,status,notes;
}

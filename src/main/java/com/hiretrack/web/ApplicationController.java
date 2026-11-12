package com.hiretrack.web;
import com.hiretrack.model.*; import com.hiretrack.repo.*; import org.springframework.http.*; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import java.time.Instant; import java.util.*;
@RestController @RequestMapping("/api/applications") public class ApplicationController {
 private final ApplicationRepository apps; private final UserRepository users; private final JobRepository jobs;
 public ApplicationController(ApplicationRepository a,UserRepository u,JobRepository j){apps=a;users=u;jobs=j;}
 @GetMapping public List<ApplicationEntity> mine(Authentication a){User u=users.findByEmail(a.getName()).orElseThrow();return "CANDIDATE".equals(u.getRole())?apps.findByCandidateId(u.getCandidateId()==null?-1:u.getCandidateId()):apps.findAll();}
 @PostMapping public ResponseEntity<?> apply(@RequestBody ApplicationEntity x,Authentication a){User u=users.findByEmail(a.getName()).orElseThrow();if(!"CANDIDATE".equals(u.getRole()))return ResponseEntity.status(403).body(Map.of("error","Candidates only"));if(!jobs.existsById(x.jobId))return ResponseEntity.notFound().build();if(u.getCandidateId()==null)return ResponseEntity.badRequest().body(Map.of("error","Account is not linked to a candidate profile"));if(apps.existsByJobIdAndCandidateId(x.jobId,u.getCandidateId()))return ResponseEntity.status(409).body(Map.of("error","Already applied"));x.id=null;x.candidateId=u.getCandidateId();x.status="applied";x.appliedAt=Instant.now();return ResponseEntity.status(201).body(apps.save(x));}
 @PatchMapping("/{id}/status") public ApplicationEntity status(@PathVariable Long id,@RequestParam String value){ApplicationEntity x=apps.findById(id).orElseThrow();x.status=value;return apps.save(x);}
}

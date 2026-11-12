package com.hiretrack.web; import org.springframework.web.bind.annotation.*; import java.time.Instant; import java.util.Map;
@RestController @RequestMapping("/api") public class HealthController { @GetMapping("/health") public Map<String,Object> health(){return Map.of("status","UP","service","hiretrack","timestamp", Instant.now());} }

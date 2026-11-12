package com.hiretrack.repo; import com.hiretrack.model.Job; import org.springframework.data.jpa.repository.JpaRepository;
public interface JobRepository extends JpaRepository<Job,Long>{}

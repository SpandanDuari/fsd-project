package com.hiretrack.repo; import com.hiretrack.model.ApplicationEntity; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface ApplicationRepository extends JpaRepository<ApplicationEntity,Long>{ List<ApplicationEntity> findByCandidateId(Long id); List<ApplicationEntity> findByJobId(Long id); boolean existsByJobIdAndCandidateId(Long j,Long c); }

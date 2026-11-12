package com.hiretrack.repo; import com.hiretrack.model.Profile; import org.springframework.data.jpa.repository.JpaRepository; import java.util.Optional;
public interface ProfileRepository extends JpaRepository<Profile,Long>{ Optional<Profile> findByUserId(Long id); }

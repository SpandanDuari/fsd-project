package com.hiretrack.repo; import com.hiretrack.model.Notification; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface NotificationRepository extends JpaRepository<Notification,Long>{List<Notification> findByUserIdOrderByCreatedAtDesc(Long id);}

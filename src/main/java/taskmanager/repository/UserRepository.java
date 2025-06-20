package taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanager.entity.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}

package obiady.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import obiady.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

	UserRole findByRole(String role);
}

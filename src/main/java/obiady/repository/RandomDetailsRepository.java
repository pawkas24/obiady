package obiady.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import obiady.RandomDetails;

public interface RandomDetailsRepository extends JpaRepository<RandomDetails, Long> {
	RandomDetails findByUser_Username(String username);
	RandomDetails findByUser_UsernameAndCategoryName(String username, String categoryName);
}

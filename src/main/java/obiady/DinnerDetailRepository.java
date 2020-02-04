package obiady;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DinnerDetailRepository extends JpaRepository<DinnerDetails, Long> {

	DinnerDetails findByDinnerNameIgnoreCase(String dinnerName);
}

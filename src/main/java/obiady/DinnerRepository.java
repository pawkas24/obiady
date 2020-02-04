package obiady;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DinnerRepository extends JpaRepository<Dinner, Long> {

	List<Dinner> findByUser_Username(String username);  //findByUser_UsernameOrderByAteAt
	long countByUser_UsernameAndAteAt(String username, LocalDate ateAt);  
	//List<Dinner> findDistinctByDinnersNameAndUser_Username(String username);
}

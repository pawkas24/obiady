package obiady;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DinnerRepository extends JpaRepository<Dinner, Long> {
	//wszystkie obiady danego usera
	List<Dinner> findByUser_Username(String username);  //findByUser_UsernameOrderByAteAt
	List<Dinner> findByUser_UsernameAndAteAtBetween(String username, LocalDate startDate, LocalDate endDate);
	Page<Dinner> findByUser_UsernameAndAteAtBeforeAndCategoriesIsNotNullOrderByAteAtDesc( String username, LocalDate before, Pageable pageable); // w historii
	
	@Query("SELECT DISTINCT d.dinnerDetail.dinnerName FROM Dinner AS d WHERE d.user.username=?1 ")
	List<String> findDistinctByUser_Username(String username); 
	//to samo co wyżej tylko dodatkowo kategoria  --------na samym dole?   Categories_Name
	//@Query("SELECT DISTINCT d.dinnerDetails.dinnerName FROM Dinner AS d WHERE d.user.username=?1 AND d.categories.name=?2") nie działa
	//List<String> findDistinctByUser_UsernameAndCategories_Name(String username, String categoryName); 

	//7 ostatnich obiadow danego usera sortowanie malejące
	List<Dinner> findTop7ByUser_UsernameAndAteAtBeforeAndCategoriesIsNotNullOrderByAteAtDesc(String username, LocalDate tomorrow);
	//pierwszy obiad ever
	Dinner findTop1ByUser_UsernameOrderByAteAtAsc(String username);
	//ilosc obiadów z konkretną datą
	long countByUser_UsernameAndAteAt(String username, LocalDate ateAt);
	//zwraca ilosc obiadow w bd danego usera
	long countByUser_Username(String username);
	//obiady zawierające w nazwie określony tekst 
	List<Dinner> findByUser_UsernameAndDinnerDetail_DinnerNameContainingOrderByAteAtDesc(String username, String dinnerName);
	//zlicza obiady o określonej nazwie dla danego usera albo wykorzystam jak wyżej
	long countByUser_UsernameAndDinnerDetail_DinnerNameContaining(String username, String dinnerName);
	//List<Object[]> countByUser_UsernameGroupByDinnerDetails_DinnerName(String username);
	
	@Query("SELECT d.dinnerDetail.dinnerName, COUNT(d.dinnerDetail.dinnerName) FROM Dinner AS d WHERE d.user.username=?1 GROUP BY d.dinnerDetail.dinnerName ORDER BY COUNT(d.dinnerDetail.dinnerName) DESC")
	List<Object[]> countDinners(String username);
	
	@Query("SELECT d.dinnerDetail.dinnerName, MAX(d.ateAt) FROM Dinner AS d WHERE d.user.username=?1 GROUP BY d.dinnerDetail.dinnerName ORDER BY MAX(d.ateAt) ASC")
	List<Object[]> findTheOldestDinners(String username); 
	
	List<Dinner> findByCategories_NameAndUser_Username(String categoryName, String username);
	long countByCategories_NameAndUser_UsernameAndAteAtAfter(String categoryName, String username, LocalDate atAt); //albo count
	long countByCategories_NameAndUser_Username(String categoryName, String username); //zlicza ilosc obiadow w kategorii, do getNumberOfUserDinners
	
	
	//                                zapytania plannera
	//
	//List<Dinner> findByUser_UsernameAndAteAtAfterAndCategoriesIsNullOrderByAteAtAsc(String username, LocalDate yesterday); // now-1 day, i dodatkowy warunek category = null
	//to samo co wyzej tylko samo categories is null
	List<Dinner> findByUser_UsernameAndCategoriesIsNullOrderByAteAtAsc(String username); // now-1 day, i dodatkowy warunek category = null
	// znajdz datę najświeższego obiadu w bazie
	Dinner findTopByUser_IdOrderByAteAtDesc(Long userId);

}

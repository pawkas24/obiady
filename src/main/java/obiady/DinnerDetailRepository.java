package obiady;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DinnerDetailRepository extends JpaRepository<DinnerDetails, Long> {

	DinnerDetails findByDinnerNameIgnoreCase(String dinnerName);
	boolean existsDinnerDetailsByDinnerName(String dinnerName);

	//List<DinnerDetails> findByIngredients_IngredientDetail_NameIn(List<String> searchParams); dzialalo ale nie potrzebne
	
	@Query("SELECT dd FROM DinnerDetails AS dd JOIN dd.ingredients AS i WHERE i.user.id = :userId AND i.ingredientDetail.name IN :searchParams GROUP BY dd HAVING COUNT(dd) = :numberOfParams")
	List<DinnerDetails> findByIngredients_IsIn(List<String> searchParams, Long numberOfParams, Long userId);
	

}

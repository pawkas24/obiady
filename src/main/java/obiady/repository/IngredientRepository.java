package obiady.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import obiady.DinnerDetails;
import obiady.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
	
	List<Ingredient> findByDinnerDetail_Id(long id);
	
	
	//jeszcze uzytkownik
	@Query("SELECT i.dinnerDetail.id FROM Ingredient AS i WHERE i.user.id=:userId AND i.ingredientDetail.name IN :searchParams")
	List<Long> findDinnerDetailsIdsWhereIngrIsInParams(@Param("searchParams") List<String> searchParams, @Param("userId") Long userId);
}

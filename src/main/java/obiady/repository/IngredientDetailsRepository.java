package obiady.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import obiady.IngredientDetails;

public interface IngredientDetailsRepository extends JpaRepository<IngredientDetails, Long> {
	IngredientDetails findByNameIgnoreCase(String name);
	IngredientDetails findByNameIgnoreCaseAndUser_Id(String name, Long userId);
	//long countByNameIgnoreCase(String name);
	
	@Query("SELECT id.name FROM IngredientDetails AS id WHERE user.id=null OR user.id=?1 ORDER BY id.name ASC")
	List<String> findAllIngredientNamesAsc(Long userId);
	
	@Query("SELECT id.name FROM IngredientDetails AS id WHERE id.receipe=?1 AND (user.id=null OR user.id=?2) ORDER BY id.name ASC")
	List<String> findAllIngredientNamesAscByIsReceipe(boolean receipe, Long userId);

	@Query("SELECT id.name FROM IngredientDetails AS id WHERE id.name=?1 AND (user.id=null OR user.id=?2)")
	List<String> findByIngrNameAndUserId(String ingrName, Long userId);
	
	@Query("SELECT id.shopSection.id FROM IngredientDetails AS id WHERE id.name=?1 AND (id.user.id=null OR id.user.id=?2)")
	Long findShopSectionByIngrNameAndUserId(String ingrName, Long userId);
	//do losowania skladnika przy uruchomieniu apki
	//List<IngredientDetails> findAll();
	
	
}

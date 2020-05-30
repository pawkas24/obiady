package obiady.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import obiady.Category;
import obiady.Dinner;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	List<Category> findAll();
	Category findByName(String name);
	Set<Dinner> findDinnersByName(String name);
	//Set<Dinner> findDinnersByDinners_User_Username(String username);
	Category findByNameAndDinners_User_Username(String name, String username); //to samo tylko liste stringow
	
}

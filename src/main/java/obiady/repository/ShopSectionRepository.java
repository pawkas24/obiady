package obiady.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import obiady.ShopSection;

public interface ShopSectionRepository extends JpaRepository<ShopSection, Long> {

	@Query("SELECT s.section FROM ShopSection AS s")
	List<String> findAllShopSections(); 
}

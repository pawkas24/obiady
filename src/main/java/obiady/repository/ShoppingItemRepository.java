package obiady.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import obiady.ShoppingItem;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, Long> {

	// byl blad ale chyba nie jest wykorzystywane @Query("SELECT si.id FROM ShoppingItem AS si WHERE si.shoppingCart.user.id=?1 AND si.ingrName=?2")
	//List<Long> findCartItemsIdByUserIdAndIngrDetail_Name(Long userId, String ingrName); 
	List<ShoppingItem> findByShoppingCart_User_IdAndIngrDetail_Name(Long userId, String ingrName);
	List<ShoppingItem> findByShoppingCart_User_IdAndIsBought(Long userId, boolean isBought);
	List<ShoppingItem> findShoppingItemByDinnerDetail_IdAndDinnerDetail_User_Id(Long dinnerDetailId, Long userId); 
	
	Page<ShoppingItem> findByShoppingCart_User_Username(String username, Pageable pageable);
	List<ShoppingItem> findByShoppingCart_User_Id(Long userId);
	
	@Query("SELECT si.ingrDetail.id, si.unit, SUM(si.quantity) FROM ShoppingItem AS si WHERE si.shoppingCart.user.id=?1 AND si.isBought=?2 GROUP BY si.ingrDetail.id, si.unit "
			+ "ORDER BY si.ingrDetail.shopSection.section ASC")
	List<Object[]> sumShoppingItems(Long userId, boolean isBought);
	
	@Query("SELECT DISTINCT si.dinnerDetail.id FROM ShoppingItem AS si WHERE si.shoppingCart.user.username=?1 ")
	List<Long> findDistinctByDinnerDetailId(String username); 
}

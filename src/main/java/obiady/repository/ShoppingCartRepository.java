package obiady.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import obiady.ShoppingCart;
import obiady.ShoppingItem;
import obiady.Utility.Unit;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
	//przeniesione do dinnerDetailRepository
	//@Query("SELECT DISTINCT sc.dinnerDetail.id FROM ShoppingCart AS sc WHERE sc.user.username=?1 ")
	//List<Long> findDistinctByDinnerDetailId(String username); 
	
	//zapytanie zwracające id itemów w shopping cart, shopping cart controller /delete
	//@Query("SELECT s.id FROM ShoppingCart AS s WHERE s.user.id=?1 AND s.dinnerDetail.id=?2")
	//List<ShoppingItem> findShoppingCartByDinnerDetail_IdAndUser_Id(Long dinnerDetailId, Long userId); przeniesiona do shoppingItem repository
	
	//@Query("SELECT sc.shoppingItems.id FROM ShoppingCart AS sc WHERE sc.user.id=?1 AND sc.ingrName=?2")
	//List<Long> findCartItemsIdByUserIdAndIngrName(Long userId, String ingrName); 

	//List<ShoppingItem> findByUser_Id(Long userId); wylaczona, zmieniona na samym dole zwracajaca ShoppingCart
	
	
//	Page<ShoppingItem> findByUser_Username(String username, Pageable pageable); przeniesione oba do shopitemrepo
	//List<ShoppingItem> findByUser_Username(String username);
	//query grupujace wg nazwy skladnika i ilosci
	/*@Query("SELECT sc.ingrName, sc.unit, SUM(sc.quantity) FROM ShoppingCart AS sc WHERE sc.user.id=?1 AND sc.isBought=?2 GROUP BY sc.ingrName, sc.unit")
	List<Object[]> sumShoppingItems(Long userId, boolean isBought);*/
	
	//@Query("SELECT sc.ingrName, sc.unit, SUM(sc.quantity) FROM ShoppingCart AS sc WHERE sc.user.username=?1 GROUP BY sc.ingrName, sc.unit")
	//Page<Object[]> sumShoppingItems(String username, Pageable pageable);
	ShoppingCart findByUser_Id(Long userId);
	
	
}

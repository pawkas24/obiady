package obiady.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import obiady.Dinner;
import obiady.DinnerDetails;
import obiady.IngredientDetails;
import obiady.ShoppingCart;
import obiady.ShoppingItem;
import obiady.Utility.Unit;
import obiady.repository.ShoppingCartRepository;
import obiady.repository.ShoppingItemRepository;

@Service
public class ShoppingCartService {

	@Autowired
	private ShoppingCartRepository shoppingCartRepo;
	@Autowired
	private ShoppingItemRepository shoppingItemRepo;
	@Autowired
	private IngredientService ingrService;
	@Autowired
	private DinnerService dinnerService;
	
	public void saveShoppingCart(ShoppingCart shoppingCart) {
		shoppingCartRepo.save(shoppingCart);
	}
	
	/*public List<Long> getDistinctDinnerDetailsIds(String username){
		return shoppingCartRepo.findDistinctByDinnerDetailId(username);
	} przeniesione do dinnerService*/  
	
	public List<ShoppingItem> findShoppingItemByDinnerDetail_IdAndUser_Id(Long dinnerDetailId, Long userId){
		return shoppingItemRepo.findShoppingItemByDinnerDetail_IdAndDinnerDetail_User_Id(dinnerDetailId, userId);
	}
	
	public void deleteShoppingCart(ShoppingCart shoppingCart) {
		shoppingCartRepo.delete(shoppingCart);
	}
	
	public Page<ShoppingItem> findAllByUser_Username(String username, Pageable pageable){
		return shoppingItemRepo.findByShoppingCart_User_Username(username, pageable);
	}
	
	public ShoppingCart getOne(Long id) {
		return shoppingCartRepo.getOne(id);
	}
	//ale nie bedzie wtedy page
	public List<ShoppingItem> getShoppingCartWithSummarizedIngrs(Long userId, boolean isBought){
		List<ShoppingItem> cartAfterSummingUp = new ArrayList<>();
		List<Object[]> rawCartData = shoppingItemRepo.sumShoppingItems(userId, isBought);

		
		for(Object[] rawItem : rawCartData) {
			ShoppingItem item = new ShoppingItem();
			//item.setIngrName((String) rawItem[0]);
			item.setIngrDetail(ingrService.getIngrDetailById((Long) rawItem[0]));
			item.setUnit((Unit) rawItem[1]);
			item.setQuantity((double)rawItem[2]);
			cartAfterSummingUp.add(item);
		}
		return cartAfterSummingUp;
	}
	
	public List<ShoppingItem> getShoppingCartItems(Long userId, String ingrName){
		return shoppingItemRepo.findByShoppingCart_User_IdAndIngrDetail_Name(userId, ingrName);
	}
	
	public List<ShoppingItem> getBoughtShoppingCartItems(Long userId, boolean isBought){
		return shoppingItemRepo.findByShoppingCart_User_IdAndIsBought(userId, isBought);
	}
	
	public ShoppingCart findByUserId(Long userId){
		return shoppingCartRepo.findByUser_Id(userId);
	}
	//////////////////////////////////////
	public void saveShoppingItem(ShoppingItem shoppingItem) {
		shoppingItemRepo.save(shoppingItem);
	}
	
	public void deleteShoppingItem(ShoppingItem shoppingItem) {
		shoppingItemRepo.delete(shoppingItem);
	}
	
	public List<Long> getDistinctDinnerDetailsIds(String username){
		return shoppingItemRepo.findDistinctByDinnerDetailId(username);
		}
	
	public List<ShoppingItem> findShoppingItemsByUserId(Long userId){
		return shoppingItemRepo.findByShoppingCart_User_Id(userId);
	}
	
	public void deleteShoppingItemsIfExists(long dinnerDetailId, Long userId, ShoppingCart shoppingCart) {
		//pobrać id dinnerDetail
		//Dinner dinner = dinnerService.getOneDinner(dinnerId);
		DinnerDetails dinnerDetail = dinnerService.getOneDinnerDetail(dinnerDetailId);
		//pobrać do listy składniki z dinnerDetail id
			removeBidirectionalAssociation(dinnerDetail, shoppingCart, userId);
	}
	
	/*public void deleteEveryShoppingItemFromShoppingList(List<ShoppingItem> shoppingItems, ShoppingCart shoppingCart) {
		for(ShoppingItem item : shoppingItems) {
			if(Objects.nonNull(item.getDinnerDetail())) {
				item.getDinnerDetail().removeShoppingItem(item);
			}
			shoppingCart.removeShoppingItem(item);
		}
	}*/
	
	public void removeBidirectionalAssociation(DinnerDetails dinnerDetail, ShoppingCart shoppingCart, Long userId) {
		List<ShoppingItem> shoppingItems = shoppingItemRepo.findShoppingItemByDinnerDetail_IdAndDinnerDetail_User_Id(dinnerDetail.getId(), userId);
		if(!shoppingItems.isEmpty()) {
			for(ShoppingItem item: shoppingItems) {
				dinnerDetail.removeShoppingItem(item);
				shoppingCart.removeShoppingItem(item);
				deleteShoppingItem(item);
			}
		}
	}
	
}

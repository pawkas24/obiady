package obiady.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import obiady.Ingredient;
import obiady.IngredientDetails;
import obiady.ShopSection;
import obiady.User;
import obiady.repository.IngredientDetailsRepository;
import obiady.repository.IngredientRepository;


@Service
public class IngredientService {

	@Autowired
	private IngredientDetailsRepository ingrDetailsRepo;
	@Autowired
	private IngredientRepository ingredientRepo;
	@Autowired
	private UserService userService;
	
	public List<String> getAllIngredientNamesAscByIsReceipe(long userId, boolean isReceipe){
		
		//nazwy majace usera null i usera zalogowanego
		List<String> list = ingrDetailsRepo.findAllIngredientNamesAscByIsReceipe(isReceipe, userId);
		//dodać do listy nazwy z user ingredients
		//userIngrRepo.findAllNames(userService.getUserId()).forEach(userIngr->list.add(userIngr));
		
		return list;
	}
	
	public List<String> findAllIngredientNamesAsc(Long userId){
		return ingrDetailsRepo.findAllIngredientNamesAsc(userId);
	}
	
	public List<String> findByIngrNameAndUserId(String ingrName, Long userId){
		return ingrDetailsRepo.findByIngrNameAndUserId(ingrName, userId);
	}
	
	public void saveIngredient(Ingredient ingredient) {
		ingredientRepo.save(ingredient);
	}
	
	public IngredientDetails setIngredientDetail(String ingrName, User user, boolean isRecipeOrigin, ShopSection shopSection) {
		//if(ingrDetailsRepo.findByIngrNameAndUserId(ingrName, user.getId()).size() == 0) {
			IngredientDetails ingrDetail = new IngredientDetails(ingrName, user, isRecipeOrigin, shopSection);
			//ingrDetailsRepo.save(ingrDetail);
			//user.addIngredientDetail(ingrDetail);
			return ingrDetail;
	//	}
	}
	
	public void saveIngrDetail(IngredientDetails ingrDetail) {
		ingrDetailsRepo.save(ingrDetail);
	}
	
	/*public void saveIngrIfNotExists(String ingrName, User user) {
		if (ingrService.countByNameIgnoreCase(ingrName) == 0) {
			//System.out.println("ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss long count ingrName: ");
		UserIngredient userIngr = new UserIngredient(ingrName, user);
		userIngrRepo.save(userIngr);
		user.addUserIngredient(userIngr);
		}
	}*/
	
	
	
	public IngredientDetails findByNameIgnoreCaseAndUser_Id(String ingredientName, Long userId) {
		return ingrDetailsRepo.findByNameIgnoreCaseAndUser_Id(ingredientName, userId);
	}
	
	/*public long countByNameIgnoreCase(String ingredientName) {
		long sum = 0;
		sum = ingrDetailsRepo.countByNameIgnoreCase(ingredientName) + userIngrRepo.countByIngrNameIgnoreCase(ingredientName);
		return sum;
	}*/
	public Ingredient getIngredientById(long id) {
		return ingredientRepo.getOne(id);
	}
	
	public void deleteIngredient(Ingredient ingredient) {
		ingredientRepo.delete(ingredient);
	}
	
	public List<Ingredient> findByDinnerDetail_Id(long id){
		return ingredientRepo.findByDinnerDetail_Id(id);
	}
	
	//do losowania skladnika przy starcie apki
	public List<IngredientDetails> findAll(){
		return ingrDetailsRepo.findAll();
	}
	
	public Long findShopSectionByIngrNameAndUserId(String ingrName, Long userId) {
		return ingrDetailsRepo.findShopSectionByIngrNameAndUserId(ingrName, userId);
	}
	
	public IngredientDetails getIngrDetailById(Long ingrDetailId) {
		return ingrDetailsRepo.getOne(ingrDetailId);
	}
	
	public boolean ingrDetailExists(String ingrDetailName, Long userId) {
		return Objects.nonNull(ingrDetailsRepo.findByNameIgnoreCaseAndUser_Id(ingrDetailName, userId));
		
	}
	
}

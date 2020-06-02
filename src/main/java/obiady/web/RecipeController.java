package obiady.web;

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import obiady.DinnerDetailRepository;
import obiady.DinnerDetails;
import obiady.Ingredient;
import obiady.IngredientDetails;
import obiady.ShopSection;
import obiady.User;
import obiady.Utility.Unit;
import obiady.repository.ShopSectionRepository;
import obiady.service.DinnerService;
import obiady.service.IngredientService;
import obiady.service.ShoppingCartService;
import obiady.service.StatsService;
import obiady.service.UserService;

@Controller
@RequestMapping("/recipes")
public class RecipeController {
	
	@Autowired
	private DinnerDetailRepository dinnerDetailRepo;
	@Autowired
	private DinnerService dinnerService;
	@Autowired
	private UserService userService;
	@Autowired
	private IngredientService ingrService;
	@Autowired
	private StatsService statsService;
	@Autowired
	private ShopSectionRepository shopSectionRepo;
	@Autowired
	private ShoppingCartService shoppingService;

	@GetMapping
	public String showRecipes(Model model) {
		User user = userService.getUser();
		//badge koszyka pokazujace ilosc skladnikow w koszyku
		shoppingService.getNumberOfItemsLeftToBuy(model, user.getId());
		
		model.addAttribute("dinners", dinnerService.getUserDistinctDinners(user.getUsername())); //datalist
		return "recipes";
	}
	
	//z formularza szukaj przepisu
	@GetMapping("/manage")
	public String showRecipeManager(Model model, @RequestParam String dinnerName, RedirectAttributes redirectAttr ) {
		User user = userService.getUser();
		//badge koszyka pokazujace ilosc skladnikow w koszyku
		shoppingService.getNumberOfItemsLeftToBuy(model, user.getId());
		
		//jeśli parametr dinnerName jest liczba (przejscie przyciskiem "przepis" z ekranu szukania przepisu
		if(statsService.isNumeric(dinnerName)) {
			dinnerName = dinnerDetailRepo.getOne(Long.parseLong(dinnerName)).getDinnerName();
		}
		
		if(dinnerDetailRepo.existsDinnerDetailsByDinnerName(dinnerName)) {
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! zdaza sie not unique, lepsze byloby getOne
			DinnerDetails dinnerDetail = dinnerDetailRepo.findByDinnerNameIgnoreCase(dinnerName);
			
			model.addAttribute("dinnerDetail", dinnerDetail);
			Ingredient ingr = new Ingredient();
			ingr.setDinnerDetail(dinnerDetail);
			
			model.addAttribute("newIngredient", ingr);
			model.addAttribute("newIngredientDetail", new IngredientDetails());
			model.addAttribute("dinners", dinnerService.getUserDistinctDinners(user.getUsername())); //datalist
			//dodać do modelu listę units  "units"
			model.addAttribute("units", Unit.values());
			//dodac do modelu liste skladnikow
			model.addAttribute("ingredients", ingrService.getAllIngredientNamesAscByIsReceipe(user.getId(), true));
			model.addAttribute("shopSections", shopSectionRepo.findAll());
			
			return "recipes-test";
		}else {
			redirectAttr.addFlashAttribute("dinnerNotFound", "Nie znaleziono obiadu o nazwie " + dinnerName + ".");
			return "recipes-test";
		}
	}
	
	//dodawanie skladnika w twoje przepisy
	@PostMapping("/ingredient")
	public String saveRecipe(@Valid @ModelAttribute("newIngredient") Ingredient newIngredient, Errors errors, Model model, RedirectAttributes redirectAttr) {
		if(newIngredient.getDinnerDetail().getDinnerName() == null){
			return "recipes-test";
		}
		DinnerDetails dinnerDetail = dinnerDetailRepo.findByDinnerNameIgnoreCase(newIngredient.getDinnerDetail().getDinnerName());
		
		if (errors.hasErrors()) {
			return getUrl(dinnerDetail);  //	return "recipes-test";
		}
		User user = userService.getUser();
		//badge koszyka pokazujace ilosc skladnikow w koszyku
		shoppingService.getNumberOfItemsLeftToBuy(model, user.getId());
		
		//jeśli nazwy skladnika nie ma w bazie
		IngredientDetails ingrDetail = ingrService.findByNameIgnoreCaseAndUser_Id(newIngredient.getIngredientDetail().getName(), user.getId());
		if (Objects.isNull(ingrDetail)) {
			ingrDetail = ingrService.setIngredientDetail(newIngredient.getIngredientDetail().getName(), user, true, newIngredient.getIngredientDetail().getShopSection());
			ShopSection shopSection = shopSectionRepo.getOne(newIngredient.getIngredientDetail().getShopSection().getId());
			user.addIngredientDetail(ingrDetail);
			shopSection.addIngredientDetail(ingrDetail);
		}else {
			ingrDetail = ingrService.findByNameIgnoreCaseAndUser_Id(newIngredient.getIngredientDetail().getName(), user.getId());
		}

		//newIngredient.setIngredientDetail(ingrDetail); to jest w addingredient
		//newIngredient.setDinnerDetail(dinnerDetail); to jest w dinnerdetail.addingredient
		ingrDetail.addIngredient(newIngredient);  
		dinnerDetail.addIngredient(newIngredient);
		user.addIngredient(newIngredient);
		
		ingrService.saveIngredient(newIngredient);
		redirectAttr.addFlashAttribute("saveIngrConfirmation", "Składnik '" + ingrDetail.getName() + "' został zapisany.");
		return getUrl(dinnerDetail);
	}
	
	@PostMapping("/dinner-detail")
	public String saveRecipe(@Valid @ModelAttribute DinnerDetails dinnerDetail, Errors errors, RedirectAttributes redirectAttr, Model model) {
		if(errors.hasErrors()) {
			return getUrl(dinnerDetail);
		}
		//badge koszyka pokazujace ilosc skladnikow w koszyku
		shoppingService.getNumberOfItemsLeftToBuy(model, userService.getUserId());
		
		DinnerDetails oldDinnerDetail = dinnerService.getDinnerDetailById(dinnerDetail.getId());
		
		oldDinnerDetail.setUrl(dinnerDetail.getUrl());
		oldDinnerDetail.setInstruction(dinnerDetail.getInstruction());

		dinnerDetailRepo.save(oldDinnerDetail);
		redirectAttr.addFlashAttribute("dinnerDetailConfirmation", "Zapisano z powodzeniem");
		return getUrl(dinnerDetail);
	}
	//delete ingredient
	@PostMapping("/ingredient/delete")
	public String deleteIngredient(@RequestParam long id, @RequestParam long dinnerDetailId, Model model, RedirectAttributes redirectAttr) {
		//badge koszyka pokazujace ilosc skladnikow w koszyku
		shoppingService.getNumberOfItemsLeftToBuy(model, userService.getUserId());
		
		DinnerDetails dinnerDetail = dinnerDetailRepo.getOne(dinnerDetailId);
		Ingredient ingredient = ingrService.getIngredientById(id);
		IngredientDetails ingrDetail = ingredient.getIngredientDetail();
	
		//uzywajac dinnerDetail.removeIngredient i ingrDet.removeIngredient nie usuwa samego ingredientu, nawet robiac po tym ingrServ.deleteIngr
		ingrService.deleteIngredient(ingredient);
		redirectAttr.addFlashAttribute("ingredientDeleteConfirmation", "Składnik '" + ingredient.getIngredientDetail().getName() + "' został usunięty z powodzeniem.");
		
		return getUrl(dinnerDetail);
	}
	
	private String getUrl(DinnerDetails dinnerDetail) {
		return  "redirect:/recipes/manage?dinnerName=" + dinnerDetail.getId();
	}
	
}

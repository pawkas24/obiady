package obiady.web;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import obiady.User;
import obiady.Utility.Unit;
import obiady.repository.ShopSectionRepository;
import obiady.service.DinnerService;
import obiady.service.IngredientService;
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

	@GetMapping
	public String showRecipes(Model model) {
		model.addAttribute("dinners", dinnerService.getUserDistinctDinners(userService.getUsername())); //datalist
		//System.out.println("to jest glowna //////////////////////////////////recipes");
		return "recipes";
	}
	
	@GetMapping("/manage")
	public String showRecipeManager(Model model, @RequestParam String dinnerName, RedirectAttributes redirectAttr ) {
		//jeśli parametr dinnerName jest liczba (przejscie przyciskiem "przepis" z ekranu szukania przepisu
		if(statsService.isNumeric(dinnerName)) {
			dinnerName = dinnerDetailRepo.getOne(Long.parseLong(dinnerName)).getDinnerName();
		}
		
		if(dinnerDetailRepo.existsDinnerDetailsByDinnerName(dinnerName)) {
			//System.out.println("222222222222222222222222  to jest recipes z pętli jesli istnieje dinnerdetail o danym dinername");
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! zdaza sie not unique, lepsze byloby getOne
			DinnerDetails dinnerDetail = dinnerDetailRepo.findByDinnerNameIgnoreCase(dinnerName);
			
			
			//System.out.println("22222222222222222222 dinnerdetail.dinnerName: " + dinnerDetail.getDinnerName() + ", " + dinnerDetail.getIngredients().toString());
			model.addAttribute("dinnerDetail", dinnerDetail);
			Ingredient ingr = new Ingredient();
			ingr.setDinnerDetail(dinnerDetail);
			
			model.addAttribute("newIngredient", ingr);
			model.addAttribute("newIngredientDetail", new IngredientDetails());
			model.addAttribute("dinners", dinnerService.getUserDistinctDinners(userService.getUsername())); //datalist
			//dodać do modelu listę units  "units"
			model.addAttribute("units", Unit.values());
			//dodac do modelu liste skladnikow
			model.addAttribute("ingredients", ingrService.getAllIngredientNamesAscByIsReceipe(userService.getUserId(), true));
			model.addAttribute("shopSections", shopSectionRepo.findAll());
			
			return "recipes-test";
		}else {
			redirectAttr.addFlashAttribute("dinnerNotFound", "Nie znaleziono obiadu o nazwie " + dinnerName + ".");
			//System.out.println("3333333333333333333333333333333333 to jest recipes z else jesli nie znaleziono obiadu o danym dinner name");
			return "recipes";
		}
		//serwis pobiera obiad o konkretnej nazwie
		//konkretny obiad zapisuje w modelu
		//zapisanie w modelu pustego obiektu ingredient 
		//instrukcje w dinnerDetails.instruction
	}
	
	//
	@PostMapping("/ingredient")
	public String saveRecipe(@ModelAttribute Ingredient newIngredient, RedirectAttributes redirectAttr) {
		
		User user = userService.findByUsername(userService.getUsername());
		//jeśli nazwy skladnika nie ma w bazie
		IngredientDetails ingrDetail = ingrService.findByNameIgnoreCaseAndUser_Id(newIngredient.getIngredientDetail().getName(), user.getId());
		if (Objects.isNull(ingrDetail)) {
			ingrDetail = ingrService.setIngredientDetail(newIngredient.getIngredientDetail().getName(), user, true, newIngredient.getIngredientDetail().getShopSection());
			user.addIngredientDetail(ingrDetail);
		}else {
			ingrDetail = ingrService.findByNameIgnoreCaseAndUser_Id(newIngredient.getIngredientDetail().getName(), user.getId());
		}
				
		DinnerDetails dinnerDetail = dinnerDetailRepo.findByDinnerNameIgnoreCase(newIngredient.getDinnerDetail().getDinnerName()); 
		newIngredient.setIngredientDetail(ingrDetail);
		newIngredient.setDinnerDetail(dinnerDetail);
		
		System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii newIngredient.shopSection: " + newIngredient.getIngredientDetail().getShopSection().getSection());
		System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii newIngredient.shopSection.id: " + newIngredient.getIngredientDetail().getShopSection().getId());
		
		ingrDetail.addIngredient(newIngredient);  
		dinnerDetail.addIngredient(newIngredient);
		user.addIngredient(newIngredient);
		
		/*System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii ingredient id: "+newIngredient.getId());
		System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii ingredient quantity: "+newIngredient.getQuantity());
		System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii ingredient unit: "+newIngredient.getUnit());
		System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii ingredient ingredientDetail name: "+newIngredient.getIngredientDetail().getName());
		System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii ingredient ingredientDetail id: "+newIngredient.getIngredientDetail().getId());
		System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii ingredient dinnerDetail dinnerName: "+newIngredient.getDinnerDetail().getDinnerName());
		System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii ingredient dinnerDetail id: "+newIngredient.getDinnerDetail().getId());*/
		
		ingrService.saveIngredient(newIngredient);
		redirectAttr.addFlashAttribute("saveIngrConfirmation", "Składnik " + ingrDetail.getName() + " został zapisany.");
		//dinnerDetailRepo.save(dinnerDetail);
		
		return getUrl(dinnerDetail);
	}
	
	@PostMapping("/dinner-detail")
	public String saveRecipe(@ModelAttribute DinnerDetails dinnerDetail, RedirectAttributes redirectAttr) {
		DinnerDetails oldDinnerDetail = dinnerService.getDinnerDetailById(dinnerDetail.getId());
		
		oldDinnerDetail.setUrl(dinnerDetail.getUrl());
		oldDinnerDetail.setInstruction(dinnerDetail.getInstruction());

		/*System.out.println("dddddddddddddddddddddddddddddddddddddddd dinner id: " + dinnerDetail.getId());
		System.out.println("dddddddddddddddddddddddddddddddddddddddd dinner id: " + dinnerDetail.getDinnerName());
		System.out.println("dddddddddddddddddddddddddddddddddddddddd dinner id: " + dinnerDetail.getInstruction());
		System.out.println("dddddddddddddddddddddddddddddddddddddddd dinner id: " + dinnerDetail.getUrl());
		//System.out.println("dddddddddddddddddddddddddddddddddddddddd dinner id: " + dinnerDetail.getUser().getUsername());*/
		
		dinnerDetailRepo.save(oldDinnerDetail);
		redirectAttr.addFlashAttribute("dinnerDetailConfirmation", "Zapisano z powodzeniem");
		return getUrl(dinnerDetail);
	}
	//delete ingredient
	@PostMapping("/ingredient/delete")
	public String deleteIngredient(@RequestParam long id, @RequestParam long dinnerDetailId, Model model, RedirectAttributes redirectAttr) {
		
		//System.out.println("del_1_del_1_del_1_del_1_del_1_del_1_del_1_del_1_del_1_del_1_ long id "+ id);
		//System.out.println("del_1_del_1_del_1_del_1_del_1_del_1_del_1_del_1_del_1_del_1_ long dinnerDetailId "+ dinnerDetailId);
		DinnerDetails dinnerDetail = dinnerDetailRepo.getOne(dinnerDetailId);
		Ingredient ingredient = ingrService.getIngredientById(id);
		IngredientDetails ingrDetail = ingredient.getIngredientDetail();
		//System.out.println("deldeldeldeldeldeldeldeldeldeldeldeldeldeldeldeldeldel dinnerDetail ID " + dinnerDetail.getId());
		//System.out.println("deldeldeldeldeldeldeldeldeldeldeldeldeldeldeldeldeldel ingredient ID " + ingredient.getId());
		//System.out.println("deldeldeldeldeldeldeldeldeldeldeldeldeldeldeldeldeldel ingredient detail ID " + ingrDetail.getId());
	
		//uzywajac dinnerDetail.removeIngredient i ingrDet.removeIngredient nie usuwa samego ingredientu, nawet robiac po tym ingrServ.deleteIngr
		ingrService.deleteIngredient(ingredient);
		redirectAttr.addFlashAttribute("ingredientDeleteConfirmation", "Składnik '" + ingredient.getIngredientDetail().getName() + "' został usunięty z powodzeniem.");
		
		return getUrl(dinnerDetail);
	}
	
	private String getUrl(DinnerDetails dinnerDetail) {
		return  "redirect:/recipes/manage?dinnerName=" + dinnerDetail.getId();
	}
	
}

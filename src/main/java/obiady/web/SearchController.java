package obiady.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import obiady.Dinner;
import obiady.User;
import obiady.service.DinnerService;
import obiady.service.IngredientService;
import obiady.service.SearchService;
import obiady.service.ShoppingCartService;
import obiady.service.UserService;

@Controller
@RequestMapping("/search")
public class SearchController {

	@Autowired
	private DinnerService dinnerService;
	@Autowired
	private UserService userService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private IngredientService ingrService;
	@Autowired
	private ShoppingCartService shoppingService;

	
	@GetMapping
	public String showSearch(Model model) { 
		Long userId = userService.getUserId();
		//badge koszyka pokazujace ilosc skladnikow w koszyku
		shoppingService.getNumberOfItemsLeftToBuy(model, userId);
		addIngredientsListToTheModel(model);
		return "search";
	}
	
	
	@GetMapping("/result")
	public String showResult(Model model, @RequestParam String name) {
		User user = userService.getUser();
		//badge koszyka pokazujace ilosc skladnikow w koszyku
		shoppingService.getNumberOfItemsLeftToBuy(model, user.getId());
		
		List<Dinner> foundDinners = dinnerService.searchForADinner(user.getUsername(), name);
		model.addAttribute("searchingResult", foundDinners);
		model.addAttribute("searchFor", name);
		model.addAttribute("dinners", dinnerService.getUserDistinctDinners(user.getUsername()));
		addIngredientsListToTheModel(model);
		
				
		return "search";
	}
	
	@GetMapping("/ingredients")
	public String searchForIngredients(Model model, @RequestParam List<String> searchParams) {
		//badge koszyka pokazujace ilosc skladnikow w koszyku
		shoppingService.getNumberOfItemsLeftToBuy(model, userService.getUserId());
		//usuniecie z listy pustych argumentow
		List<String> params = searchService.getNotEmptyParams(searchParams);
		
		//do wyswietlenia params w inputs
		model.addAttribute("params", params);
		model.addAttribute("ingrSearchResult", dinnerService.findDinnersByIngredients(searchParams, params));
		addIngredientsListToTheModel(model);
			
		return "search";
	}
	
	private void addIngredientsListToTheModel(Model model) {
		model.addAttribute("ingredients", ingrService.getAllIngredientNamesAscByIsReceipe(userService.getUserId(), true));
	}
	

	
	
}

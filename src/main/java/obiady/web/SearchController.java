package obiady.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import obiady.Dinner;
import obiady.service.DinnerService;
import obiady.service.IngredientService;
import obiady.service.SearchService;
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

	
	@GetMapping
	public String showSearch(Model model) { 
		addIngredientsListToTheModel(model);
		return "search";
	}
	
	
	@GetMapping("/result")
	public String showResult(Model model, @RequestParam String name) {
		String username = userService.getUsername();
		List<Dinner> foundDinners = dinnerService.searchForADinner(username, name);
		model.addAttribute("searchingResult", foundDinners);
		model.addAttribute("searchFor", name);
		model.addAttribute("dinners", dinnerService.getUserDistinctDinners(username));
		addIngredientsListToTheModel(model);
		
				
		return "search";
	}
	
	@GetMapping("/ingredients")
	public String searchForIngredients(Model model, @RequestParam List<String> searchParams) {
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

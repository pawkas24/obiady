package obiady.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import obiady.User;
import obiady.service.ShoppingCartService;
import obiady.service.StatsService;
import obiady.service.UserService;

@Controller
@RequestMapping("/stats")
public class StatisticsController {
	
	@Autowired
	private StatsService statsService;
	@Autowired
	private UserService userService;
	@Autowired
	private ShoppingCartService shoppingService;
	
	

	@GetMapping()
	public String statistics(Model model, @RequestParam(value="option", required=true) String option) {//@RequestParam(value="days", required=true) String days
		
		User user = userService.getUser();
		
		//badge koszyka pokazujace ilosc skladnikow w koszyku
		shoppingService.getNumberOfItemsLeftToBuy(model, user.getId());
		
		switch(option) {
		case "oldest":
			model.addAttribute("theOldest", statsService.getTheOldestDinners(user.getUsername()));
			break;
		case "categorySum":
			model.addAttribute("sumOfDinnersByCategory", statsService.countDinnersByCategory(user.getUsername(), model)); 
			break;
		case "nameSum":
			model.addAttribute("countDinners", statsService.countDistinctDinners(user.getUsername()));
			break;
		}
		
		
		
		
		//List<String> dayOptions = statsService.getDayOptions();
		//int requestedDays = statsService.getDays(days, username);  //liczba dni z Listy dayOptions, lub jesli "wszystkie" to liczba dni miedzy 1 obiadem a now
		
		//model.addAttribute("dayOptions", dayOptions); 
		//model.addAttribute("intDays", requestedDays);  //do powiazania z polem wyboru; (days.equals(dayOptions.get(dayOptions.size()-1))) ? requestedDays : Integer.parseInt(days)
		//model.addAttribute("days", days);
		//model.addAttribute("sumOfDinnersByCategory", statsService.countDinnersByCategory(username, requestedDays));  //mapa <string, Long>
		
		//licznik obiadów
//		model.addAttribute("countDinners", statsService.countDistinctDinners(username)); //result
		//List<Dinner> dinnersByCategory = statsService.findDinnersByCategory();
		//model.addAttribute("dinnersByCategory", dinnersByCategory);
		//pobrać z serwisu top obiady
		//pobrać z serwisu bottom obiady
		//zapisać obiady top i bottom w modelu
		return "stats";
	}
	

}

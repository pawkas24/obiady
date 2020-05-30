package obiady.web;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import obiady.DinnerRepository;
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
	private DinnerRepository dinnerRepo;
	
	

	@GetMapping()
	public String statistics(Model model, @RequestParam(value="option", required=true) String option) {//@RequestParam(value="days", required=true) String days
		
		String username = userService.getUsername();
		
		switch(option) {
		case "oldest":
			model.addAttribute("theOldest", statsService.getTheOldestDinners(username));
			break;
		case "categorySum":
			model.addAttribute("sumOfDinnersByCategory", statsService.countDinnersByCategory(username, model)); 
			break;
		case "nameSum":
			model.addAttribute("countDinners", statsService.countDistinctDinners(username));
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

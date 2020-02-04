package obiady.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import obiady.Dinner;
import obiady.DinnerRepository;
import obiady.User;
import obiady.UserRepository;

@Controller
@RequestMapping("/test")
public class TestController {

	@Autowired
	UserRepository userRepo;
	@Autowired
	DinnerRepository dinnerRepo;
	
	@GetMapping
	public String showAddDinnerForm(Model model) {
		//dodanie i zapisanie userów
		List<User> addUsers = Arrays.asList(new User("pawkoo", "pkruk@gmail.com"), new User("aneczka", "annakruk@onet.pl"));
		userRepo.saveAll(addUsers);
		//dodanie i zapisanie obiadów
		User pawel = userRepo.findByUsername("pawkoo");
		User anna = userRepo.findByUsername("aneczka");
		
		//ze wzgledu na brak daty
		//List<Dinner> pawelDinners = Arrays.asList(new Dinner("zupa pomidorowa", pawel), new Dinner("kotlety mielone", pawel));
		//List<Dinner> annaDinners = Arrays.asList(new Dinner("indyk pieczony", anna), new Dinner("rosół", anna));
		//for(Dinner dinner : pawelDinners) {
		//	pawel.addDinner(dinner);
		//}
		//for(Dinner dinner : annaDinners) {
		//	anna.addDinner(dinner);
		//}
		userRepo.save(pawel);
		userRepo.save(anna);
		List<User> users = userRepo.findAll();
		List<Dinner> dinners1 = dinnerRepo.findByUser_Username("pawkoo");
		List<Dinner> dinners2 = dinnerRepo.findByUser_Username("aneczka");
		List<Dinner> allDinners = dinnerRepo.findAll();
		
		model.addAttribute("users", users);
		model.addAttribute("dinners1", dinners1);
		model.addAttribute("dinners2", dinners2);
		model.addAttribute("allDinners", allDinners);
		
		return "dinnerForm";
	}
}

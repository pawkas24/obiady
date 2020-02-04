package obiady.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import obiady.Dinner;
import obiady.DinnerRepository;
import obiady.DinnerDetails;
import obiady.DinnerDetailRepository;
import obiady.User;
import obiady.UserRepository;

@Controller
@RequestMapping("/dinner")
public class FormController {
	
	@Autowired
	DinnerRepository dinnerRepo;
	@Autowired
	UserRepository userRepo;
	@Autowired
	DinnerDetailRepository dinnerDetailRepo;
	private String loggedUser = "pawkoo";
	
	@GetMapping
	public String showAddDinnerForm(Model model) {
		//dodanie i zapisanie userów
		if(userRepo.findAll().size() == 0) {
		List<User> addUsers = Arrays.asList(new User("pawkoo", "pkruk@gmail.com"), new User("aneczka", "annakruk@onet.pl"));
		userRepo.saveAll(addUsers);
		}
		List<Dinner> sortedDinners = dinnerRepo.findByUser_Username(loggedUser);
		sortedDinners.sort(Comparator.comparing(Dinner::getAteAt));  //sprawdzic nulla?
		
		//przygotowanie listy dat
		List<LocalDate> filledDates = new ArrayList<>();
		sortedDinners.forEach(dinner->filledDates.add(dinner.getAteAt()));
		
		//dodanie do modelu listy obiadow danego uzytkowanika, najlepiej distinct
		model.addAttribute("userDinners", sortedDinners);
		//dodanie do modelu pustego obiektu typu dinner, który przyjmie dane z formularza
		model.addAttribute("newDinner", new Dinner());
		//dodanie do modelu dat z istniejących w bd obiadów
		model.addAttribute("filledDates", filledDates);
		//dodanie do modelu pustego obiektu Dinners w ktorym zostanie zapisana nazwa obiadu, id bedzie dodawane oddzielnie, po wyszukaniu, zeby nie bylo duplikatów
		model.addAttribute("dinnerDetail", new DinnerDetails());   //"dinner"
		//dodanie do modelu unikalnych nazw obiadu z Dinners
		model.addAttribute("dinners", dinnerDetailRepo.findAll()); //"distinctDinners"
		
		//przygotowanie i umieszczenie typów posiłków w modelu
		
		return "add-dinner";
	}
	
	@PostMapping  //w formularzu akcja=#, przegladarka wysle dane z formularza do tego samego kontrolera z ktorego przyszlo get
	public String newDinnerSubmit(@ModelAttribute Dinner dinner, Model model) {
		//ustawia usera 		
		dinner.setUser(userRepo.findByUsername(loggedUser));
		//pobiera nazwe obiadu
		String dinnerName = dinner.getDinnerDetails().getDinnerName();
		//jesli taki obiad jest na liscie pobierz jego id 
		
		if(dinnerDetailRepo.findByDinnerNameIgnoreCase(dinnerName)!=null) {
			dinner.setDinnerDetails(dinnerDetailRepo.findByDinnerNameIgnoreCase(dinnerName));
		};
		
		//dinner.setDinnersName(dinnersRepo.findByDinnerDistinctNameIgnoreCase(dinnerName)); //albo tylko to, jesli bedzie null to i tak juz jest null
		//sprawdzić czy obiad z danego dnia jest już w bazie
		if(dinnerRepo.countByUser_UsernameAndAteAt(loggedUser, dinner.getAteAt())==1) {
			//modal 
			
			return "confirm"; //
		}else {
		trimAndSave(dinner);
		//dinner.setType(type);
		
		return "redirect:/dinner";
		}
	}
	
	private void trimAndSave(Dinner dinner) {
		dinner.getDinnerDetails().getDinnerName().trim();
		dinnerDetailRepo.save(dinner.getDinnerDetails());
		dinner.setComment(dinner.getComment().trim());
		dinnerRepo.save(dinner);
	}
	
}

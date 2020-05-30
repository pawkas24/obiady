package obiady.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import obiady.Dinner;
import obiady.RandomDetails;
import obiady.User;
import obiady.repository.RandomDetailsRepository;

@Service
public class RandomService {
		
	private UserService userService;
	@Autowired
	DinnerService dinnerService;
	@Autowired
	RandomDetailsRepository randomDetailsRepo;
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	//zamienic user na username
	public void saveRandomDinnerAndDateToTheModel(Model model, String category) {
		User user = userService.findByUsername(userService.getUsername()); //przekazac usera jako arg metody a wyszukać go w randomController? 
		
		//jeśli jest przynajmniej jeden obiad w danej kategorii, lub wszystkie
		if(dinnerService.getNumberOfUserDinners(user.getUsername(), category) > 0) {
			
			//jeśli data poprzedniego losowania jest starsza od daty bieżącej
			//random details uzyskać przez serwis
			RandomDetails oldRandomDetails = randomDetailsRepo.findByUser_UsernameAndCategoryName(user.getUsername(), category);
			
			if(oldRandomDetails == null || oldRandomDetails.getDateOfRandom().compareTo(LocalDate.now()) < 0) {  //|| dateOfLastRandom.compareTo(today) < 0
				
				//losuje obiad dla username i category name, a co jesli dla kategorii name bedzie puste? 
				
				//zwraca "Brak obiadow w danej kategorii" wiec wywalam if poniżej
				String newRandomDinner = dinnerService.randomDistinctDinner(user.getUsername(), category); //losuje obiad, nie powinno byc w random service czyli tu?? wystarczy string
				//System.out.println("222222222222222222222222 po tym if isEmpty, newRandomDinner: " + newRandomDinner);
				//if(newRandomDinner.isEmpty()) {
				//	model.addAttribute("categoryWithoutDinners", "Brak obiadów w wybranej kategorii"); //!!!!!!!zmienić na properties albo db.errors
				//	return;
				//}
				
				RandomDetails newRandomDetails = new RandomDetails(category, newRandomDinner, user);
				//System.out.println("33333333333333333333 newRandomDetails " + newRandomDetails.getCategoryName() +" dinnerName: " + newRandomDetails.getDinnerName());
				
				randomDetailsRepo.save(newRandomDetails);  //zapisac jeszcze usera?
				model.addAttribute("randomDinner", newRandomDetails);
				//model.addAttribute("categoryWithoutDinners", "");
				
				countDaysSinceHavingRandomDinner(user.getUsername(), model, category);   // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! po co else??
			}else { //jeśli data losowania jest taka sama               
				countDaysSinceHavingRandomDinner(user.getUsername(), model, category);
				//dodac do modelu wiadomosc dzis juz losowales, sprobuj jutro?
			}
		}else {   //tu sie konczy if przynajmniej jeden obiad w kategorii
		model.addAttribute("categoryWithoutDinners", "Brak obiadów w wybranej kategorii");
		}
		
	}
	
	public void countDaysSinceHavingRandomDinner(String username, Model model, String categoryName) {
		//musze miec nazwe wylosowanego obiadu
		RandomDetails randomDetails = randomDetailsRepo.findByUser_UsernameAndCategoryName(username, categoryName);
		//muszę mieć ostatnie (najświeższe) wystąpienie wylosowanego obiadu 
		Dinner  randomDinner = dinnerService.getTheLatestHavingRandomDinner(username, randomDetails.getDinnerName());
		long daysSinceHavingRandomDinner = randomDinner.getAteAt().until(LocalDate.now(), ChronoUnit.DAYS);
		model.addAttribute("daysSinceHavingRandomDinner",daysSinceHavingRandomDinner);
		model.addAttribute("randomDinner", randomDetails);
		//return daysSinceHavingRandomDinner;
	}
	
}

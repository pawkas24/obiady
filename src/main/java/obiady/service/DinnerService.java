package obiady.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import constraint.group.HistoryValid;
import obiady.Category;
import obiady.Dinner;
import obiady.DinnerDetailRepository;
import obiady.DinnerDetails;
import obiady.DinnerRepository;
import obiady.User;
import obiady.repository.CategoryRepository;

@Service
public class DinnerService {

	@Autowired
	private final DinnerRepository dinnerRepo;
	@Autowired
	private final UserService userService;
	@Autowired
	private final DinnerDetailRepository dinnerDetailRepo;
	@Autowired
	private CategoryRepository catRepo;
	//@Autowired
	//private Validator validator;
	
	@Autowired
	public DinnerService(DinnerRepository dinnerRepo, UserService userService, DinnerDetailRepository dinnerDetailRepo) {
		this.dinnerRepo = dinnerRepo;
		this.userService = userService;
		this.dinnerDetailRepo = dinnerDetailRepo;
	}

	
	public List<Dinner> searchForADinner(String username, String dinnerName) {
		return dinnerRepo.findByUser_UsernameAndDinnerDetail_DinnerNameContainingOrderByAteAtDesc(username, dinnerName);
	}
	
	public String randomDistinctDinner(String username, String category) {
		Random random = new Random();
		//przygotowanie listy obiadów
		//jeśli categoria = wszystkie
		List<String> userDistinctDinners = new ArrayList<>();
		if (category.equals("wszystkie")) {
			userDistinctDinners = dinnerRepo.findDistinctByUser_Username(username);
						
		}else {
			//dodatkowy warunek szukania po kategorii, kategoria będzie zawsze, lista obiadów w kategorii może być pusta
			//userDistinctDinners = dinnerRepo.findDistinctByUser_UsernameAndCategories_Name(username, category); chyba ma status nie dziala w dinnerRepo
			Category foundCategory = catRepo.findByNameAndDinners_User_Username(category, username);
			//foundCategory.getDinners().forEach(dinner->System.out.println("################################ dinner z category: " + dinner.getDinnerDetails().getDinnerName()));
			//przygotowanie listy stringow z obiadami z danej kategorii, może być pusta
			if (foundCategory.getDinners().size() > 0) {
				for(Dinner dinner : foundCategory.getDinners()) {
					userDistinctDinners.add(dinner.getDinnerDetail().getDinnerName());
				}
			}else {
				return "";
			}
			
		}
		//wylosowanie
		//lista obiadów w danej kategorii może być pusta
		String randomDinnerName = userDistinctDinners.get(random.nextInt(userDistinctDinners.size()));
		//odnalezienie najwcześniejszego wystąpienia takiego obiadu
		return randomDinnerName;//getTheLatestHavingRandomDinner(username, randomDinnerName).getDinnerDetails().getDinnerName();
	}
	
	public long getNumberOfUserDinners(String username, String categoryName){
		//w zależności od kategorii
		if(categoryName.equals("wszystkie")) {
			return dinnerRepo.countByUser_Username(username);
		}else {
			return dinnerRepo.countByCategories_NameAndUser_Username(categoryName, username);
		}
		
	}
	// w historii
	public Page<Dinner> getListOfUserDinners(String username, Pageable pageable){
		return dinnerRepo.findByUser_UsernameAndAteAtBeforeAndCategoriesIsNotNullOrderByAteAtDesc(username, LocalDate.now().plusDays(1L), pageable);
	}
	//pobierz najwcześniejszy wylosowany obiad
	public Dinner getTheLatestHavingRandomDinner(String username, String randomDinnerName) {
		List<Dinner> selectionOfRandomDinners = searchForADinner(username, randomDinnerName);
		selectionOfRandomDinners.sort(Comparator.comparing(Dinner::getAteAt).reversed());
		return selectionOfRandomDinners.get(0);
		
	}
	public List<String> getUserDistinctDinners(String username){
		//List<String> list = new ArrayList<>();
		List<String> userDistinctDinners = dinnerRepo.findDistinctByUser_Username(username);
		//userDistinctDinners.forEach(d->list.add(d));
		return userDistinctDinners;
	}
	
	public void saveDinner(Dinner dinner) {
		User user = userService.findByUsername(userService.getUsername());
		//if(countDinnersAtSpecificDate(user.getUsername(), dinner.getAteAt()) == 0 ) {
			dinner.setUser(user);
			trimAndSave(dinner);
		//}
		
		
	}
	
	private void trimAndSave(Dinner dinner) {
		dinner.getDinnerDetail().getDinnerName().trim();
		dinnerDetailRepo.save(dinner.getDinnerDetail());
		dinner.setComment(dinner.getComment().trim());
		dinnerRepo.save(dinner);
	}
	
	public Long countDinnersAtSpecificDate(String username, LocalDate ateAt) {
		return dinnerRepo.countByUser_UsernameAndAteAt(username, ateAt);
	}
	
	public List<Dinner> getPlannedDinners(String username){
		List<Dinner> plannedDinners = dinnerRepo.findByUser_UsernameAndCategoriesIsNullOrderByAteAtAsc(username); //LocalDate.now().minusDays(1L) 
		return plannedDinners;
	}
	
	public DinnerDetails getDinnerDetailById(long id) {
		return dinnerDetailRepo.getOne(id);
	}
	
	public DinnerDetails getDinnerDetailByDinnerName(String dinnerName) {
		return dinnerDetailRepo.findByDinnerNameIgnoreCase(dinnerName);
	}
	
	public List<DinnerDetails> findDinnersByIngredients(List<String> searchParams, List<String> params){
		return dinnerDetailRepo.findByIngredients_IsIn(searchParams, Long.valueOf(params.size()), userService.getUserId());
	}
	
	public Dinner getOneDinner(Long dinnerId) {
		return dinnerRepo.getOne(dinnerId);
	}
	
	public DinnerDetails getOneDinnerDetail(Long dinnerDetailsId) {
		return dinnerDetailRepo.getOne(dinnerDetailsId);
	}
	
	public LocalDate getTheLatestDinnerDate(Long userId) {
		LocalDate date = LocalDate.now();
		Dinner dinner = dinnerRepo.findTopByUser_IdAndAteAtBeforeOrderByAteAtDesc(userId, LocalDate.now().plusDays(1));
		if(Objects.nonNull(dinner) && dinner.getAteAt().isBefore(date.plusDays(1))) {
			date = dinner.getAteAt().plusDays(1);
		}
		return date;
	}
	
}


package obiady.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import obiady.Category;
import obiady.Dinner;
import obiady.DinnerRepository;
import obiady.repository.CategoryRepository;

@Service
public class StatsService {
	
	private Long sumOfUserDinners = 30L;
	@Autowired
	private DinnerRepository dinnerRepo;
	@Autowired
	private CategoryRepository catRepo;
	@Autowired
	private CategoryService catService;
	//albo zamiast wszystkie, jako ostatnia pozycje, dodac liczbe dni miedzy 1 a ostatnim obiadem, i tak ją obliczam w getDays
	private List<String> dayOptions = Arrays.asList("30", "90", "180", "wszystkie"); //ilość obiadów decyduje o zakresach filtrowania? switch sumaObiadow od do ? x=ilestam  
	//pole String username?
	
	public List<String> getDayOptions() {
		return dayOptions;
	}
	public void setDayOptions(List<String> dayOptions) {
		this.dayOptions = dayOptions;
	}
	//getter 
	public Long getSumOfUserDinners() {
		return sumOfUserDinners;
	}
		//setter
	public void setSumOfUserDinners(Long sumOfUserDinners) {
		this.sumOfUserDinners = sumOfUserDinners;
	}



	public Map<String, Long> countDinnersByCategory(String username, int requestedDays){
		Map<String, Long> results = new TreeMap<>();
		//suma wszystkich obiadow konkretnego usera
		//sumOfUserDinners = dinnerRepo.countByUser_Username(username);
		
		//Lista dla sumy obiadów w poszczególnych kategoriach, powiedzmy tabela nr 1
		List<Long> dinnersByCategoryResults = new ArrayList<>();
		
		//lista nazw kategorii
		List<String> categoryList = catService.getCategoryNamesAscending();


		//jeśli ostatni obiad jest wczesniej niż requested to zmienić requested na date ostatniego obiadu
		LocalDate now = LocalDate.now();
		LocalDate theOldestDinnerDate = dinnerRepo.findTop1ByUser_UsernameOrderByAteAtAsc(username).getAteAt();
		if (theOldestDinnerDate.isAfter(now.minusDays(requestedDays))) {
			requestedDays = (int) ChronoUnit.DAYS.between(theOldestDinnerDate, now);
		}
		LocalDate startDate = now.minus(requestedDays, ChronoUnit.DAYS);
				
		//dla każdej pozycji z listy kategorii oblicz liczbę obiadów za x dni i wynik umieść w liście dinnersByCategoryResult
		categoryList.forEach(category -> dinnersByCategoryResults.add(dinnerRepo.countByCategories_NameAndUser_UsernameAndAteAtAfter(category, username, startDate)));
		
		//umiescic liste kategorii w kluczach a wynik w value
		for(int i = 0; i < categoryList.size(); i++) {
			if (dinnersByCategoryResults.get(i) != 0) {
			results.put(categoryList.get(i), dinnersByCategoryResults.get(i));
			}
		}
		return results;
	}

	
	public Map<String, Long> countDinnersByCategory(String username, Model model){
		Map<String, Long> results = new TreeMap<>();
		//suma wszystkich obiadow konkretnego usera
		//sumOfUserDinners = dinnerRepo.countByUser_Username(username);
		
		//Lista dla sumy obiadów w poszczególnych kategoriach, powiedzmy tabela nr 1
		List<Long> dinnersByCategoryResults = new ArrayList<>();
		
		//lista nazw kategorii
		List<String> categoryList = catService.getCategoryNamesAscending();


		//jeśli ostatni obiad jest wczesniej niż requested to zmienić requested na date ostatniego obiadu
		LocalDate now = LocalDate.now();
		LocalDate theOldestDinnerDate = dinnerRepo.findTop1ByUser_UsernameOrderByAteAtAsc(username).getAteAt();   // mogą być dwa obiady najstarsze jeśli były dwa dania
		
		int	requestedDays = (int) ChronoUnit.DAYS.between(theOldestDinnerDate, now);
		
		LocalDate startDate = now.minus(requestedDays, ChronoUnit.DAYS);
				
		//dla każdej pozycji z listy kategorii oblicz liczbę obiadów za x dni i wynik umieść w liście dinnersByCategoryResult
		categoryList.forEach(category -> dinnersByCategoryResults.add(dinnerRepo.countByCategories_NameAndUser_UsernameAndAteAtAfter(category, username, startDate)));
		
		//umiescic liste kategorii w kluczach a wynik w value
		for(int i = 0; i < categoryList.size(); i++) {
			if (dinnersByCategoryResults.get(i) != 0) {
			results.put(categoryList.get(i), dinnersByCategoryResults.get(i));
			}
		}
		model.addAttribute("intDays", requestedDays);
		return results;
	}
	
	
	
	public boolean isNumeric(String str) {
		if (str == null ) {
			return false;
		}
		try {
			int number = Integer.parseInt(str);
		}catch (NumberFormatException nfe){
			return false;
		}
		return true;
	}
	
	public int getDays(String days, String username){
		int requestedDays = 0;
		if(isNumeric(days)) {
			return requestedDays = Integer.parseInt(days);
		}else {
			LocalDate firstDate = dinnerRepo.findTop1ByUser_UsernameOrderByAteAtAsc(username).getAteAt();
			System.out.println("-----------------------------------to jest data pierwszego obiadu: " + firstDate.toString());
			requestedDays = (int) ChronoUnit.DAYS.between(firstDate, LocalDate.now());
			System.out.println("xxxxxxxxxxxxxxxxxxxxxx roznica dni miedzy pierwszym obiadem a now: " + requestedDays);
			return requestedDays;
		}
	}
	//może być null
	public Map<String, String> getTheOldestDinners(String username){
		// data w kluczu, a jesli dwa obiady rownie stare to mi nadpisze
		//findTheOldest sortuje po dacie więc może pierwsze na liście będą najstarsze dlatego zrobić pętle od i = 0; i++, do i<3
		Map<String, String> map = new LinkedHashMap<>();
		List<Object[]> resultList = dinnerRepo.findTheOldestDinners(username);
		for(Object[] dinner : resultList) {
			map.put(dinner[0].toString(), dinner[1].toString());
			//System.out.println("1111111111111111111111111111111111111111111111111111111111111 dinner[0] i dinner[1] " + dinner[0].toString() +"; " + dinner[1].toString());
		}
		return map;
	}
	
	public Map<String, Long> countDistinctDinners(String username){ 
		Map<String, Long> map = new LinkedHashMap<>();
		List<Object[]> dinners = dinnerRepo.countDinners(username);
		
		for(Object[] dinner : dinners) {
			map.put((String) dinner[0], (Long)dinner[1]);
		}
		return map;
	}
		
}



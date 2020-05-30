package obiady.web;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import obiady.Category;
import obiady.CategoryDetails;
import obiady.Dinner;
import obiady.DinnerRepository;
import obiady.Ingredient;
import obiady.IngredientDetails;
import obiady.User;
import obiady.DinnerDetails;
import obiady.DinnerDetailRepository;
import obiady.UserRepository;
import obiady.Utility.Unit;
import obiady.repository.CategoryRepository;
import obiady.service.DinnerService;
import obiady.service.IngredientService;
import obiady.service.UserService;

@Controller
@RequestMapping("/dinner")
//@SessionAttributes("myAdverts") informuje Springa jaki obiekt ma przechować w modelu na dłużej niż tylko żądanie, można usunąć go potem np sessionStatus.setComplete();
public class FormController {
	
	@Autowired
	private DinnerRepository dinnerRepo;
	@Autowired
	private DinnerDetailRepository dinnerDetailRepo;
	@Autowired
	private UserService userService;
	@Autowired
	private DinnerService dinnerService;
	@Autowired
	private CategoryRepository catRepo;
	@Autowired
	private IngredientService ingrService;
	
	
	@GetMapping  //@CookieValue(value = "dinnerName", defaultValue = "none") String dinnerName,  @CookieValue(value = "dinnerComment", defaultValue = "none") String dinnerComment
	public String showAddDinnerForm(Model model) { 
		//dodanie userów
		if(dinnerRepo.count() == 0 ) {
			addNewDinners();
		}
		Dinner dinner = new Dinner();
		
		dinner.setAteAt(dinnerService.getTheLatestDinnerDate(userService.getUserId()));
		
		// ustawić domyślną datę na dzień po ostatnim obiedzie w bazie, a jeśli baza jest pusta, localDate.now
		//dinner.setAteAt(LocalDate.of(1111,11, 11));
		/*if (!dinnerName.equals("none")) {
			dinner.getDinnerDetail().setDinnerName(dinnerName);
			dinner.setComment(dinnerComment);
			model.addAttribute("nullDate", "Wybierz datę");
		}*/
		
		String username = userService.getUsername();
		List<Dinner> sortedDinners = dinnerRepo.findTop7ByUser_UsernameAndAteAtBeforeAndCategoriesIsNotNullOrderByAteAtDesc(username, LocalDate.now().plusDays(1L)); //
		
		//dodanie do modelu listy obiadow danego uzytkowanika
		model.addAttribute("userDinners", sortedDinners);  //??????????????? to nie to samo co niżej "dinners" ???
		//dodanie do modelu pustego obiektu typu dinner, który przyjmie dane z formularza
				
		model.addAttribute("newDinner", dinner);
		//dodanie do modelu dat z istniejących w bd obiadów
		//model.addAttribute("filledDates", filledDates);
		//dodanie do modelu pustego obiektu Dinners w ktorym zostanie zapisana nazwa obiadu, id bedzie dodawane oddzielnie, po wyszukaniu, zeby nie bylo duplikatów
		model.addAttribute("dinnerDetail", new DinnerDetails());   //"dinner"
		//dodanie do modelu unikalnych nazw obiadu z Dinners
		model.addAttribute("dinners", dinnerService.getUserDistinctDinners(username));
		model.addAttribute("username", username);

		//przygotowanie i umieszczenie typów posiłków w modelu
		return "add-dinner"; //"add-dinner" 
	}
	
	//request z drugiego kroku
	@PostMapping  //w formularzu akcja=#, przegladarka wysle dane z formularza do tego samego kontrolera z ktorego przyszlo get
							//@ModelAttribute only binds values from post parameters.
	public String newDinnerSubmit(@Valid @ModelAttribute Dinner newDinner, Model model, Errors errors, @RequestParam(value="id", required=false) String id) { //był long id
		if (errors.hasErrors()) {
			return "add-dinner";
		}
		User user = userService.getUserById(userService.getUserId());
		//w dinner_category zapisac dinner id i category id
		if(!Objects.isNull(id)) {
			
			Dinner oldDinner = dinnerRepo.getOne(Long.parseLong(id));
			oldDinner.setAteAt(newDinner.getAteAt());
			oldDinner.setCategories(newDinner.getCategories());
			oldDinner.setComment(newDinner.getComment());
			//System.out.println("sssssssssssssssssssssssssssssssssssssssssssssssssssssssssss newDinner.getDinnerDetail.id: " + oldDinner.getDinnerDetail().getId());
			//System.out.println("sssssssssssssssssssssssssssssssssssssssssssssssssssssssssss newDinner.getDinnerDetail.url: " + oldDinner.getDinnerDetail().getUrl());
			oldDinner.getDinnerDetail().setDinnerName(newDinner.getDinnerDetail().getDinnerName());
			oldDinner.getDinnerDetail().setInstruction(newDinner.getDinnerDetail().getInstruction());
			oldDinner.getDinnerDetail().setUrl(newDinner.getDinnerDetail().getUrl());
			//oldDinner.setDinnerDetail(newDinner.getDinnerDetail());
			
			oldDinner.setUser(newDinner.getUser());
			dinnerService.saveDinner(oldDinner);
			return "redirect:/dinner";
		}else {
		setDinnerDetails(newDinner, user);
		dinnerService.saveDinner(newDinner);//dopiero po tym mozna pobrac dinner id
		return "redirect:/dinner";
			}
		}
	

	@GetMapping("/confirm") //@GetMapping("/confirm")
	private String showConfirmPage(@Valid @ModelAttribute("newDinner") Dinner dinner, Model model, @RequestParam(value="id", required=false) String id,
			RedirectAttributes redirectAttr, Errors errors, HttpServletResponse response) throws IOException { 
		if (errors.hasErrors()) {
			return "add-dinner";
		}
		//if (dinner.getAteAt().isEqual(LocalDate.of(1111,11, 11))) {
			
			//redirectAttr.addFlashAttribute("nullDate", "Wybierz datę");
			//model.addAttribute("newDinner", dinner);
			/*setCookie(response, "dinnerName", dinner.getDinnerDetail().getDinnerName());
			setCookie(response, "dinnerName", dinner.getComment());*/
			//return "redirect:/dinner";
		//}
		//jeśli data = null, wróć do formularza z attr message "wybierz date"
		/*if(Objects.isNull(dinner.getAteAt())) {
			redirectAttr.addFlashAttribute("nullDate", "Wybierz datę");
			return "add-dinner";
		}*/
		//warunek do edycji, dinner z modelu podmienia na dinner pobrany z bd
		//System.out.println("11111111111111111111111111111111 id: " +id);
		//jesli edytuje to znajdz stary dinner
		if(!Objects.isNull(id)) {
			dinner = dinnerRepo.getOne(Long.parseLong(id));
		}
		
		List<Category> categories = catRepo.findAll();
		//nie wykrywac kategorii jesli id !null, zostawić starą
		Category foundCategory = findCategory(dinner);
		dinner.addCategory(foundCategory);
				
		model.addAttribute("categories", categories);
		model.addAttribute("foundCategory", foundCategory);
		
		model.addAttribute("dinners", dinnerService.getUserDistinctDinners(userService.getUsername()));
		model.addAttribute("newDinner", dinner);
		return "add-dinner-step2";
	}
	
	@GetMapping("/edit")
	private String showEditDinner(Model model, @ModelAttribute Dinner dinner, @RequestParam String id) { //bez @ModelAttribute Dinner dinner wyskakiwal blad w add-dinner-step2 wiersz40
		//warunek do edycji, dinner z modelu podmienia na dinner pobrany z bd
		//System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111111111111111 id: " +id);
		dinner = dinnerRepo.getOne(Long.parseLong(id));
				System.out.println("22222222222222222222222222222222222222222222222222222222222222222222222222222222222222dinnerdetail dinnername i id " + dinner.getDinnerDetail().getDinnerName() +", " + dinner.getDinnerDetail().getId());
		List<Category> categories = catRepo.findAll();
		
		Category foundCategory = findCategory(dinner);
		dinner.addCategory(foundCategory);
		
		model.addAttribute("categories", categories);
		model.addAttribute("foundCategory", foundCategory);
		
		model.addAttribute("dinners", dinnerService.getUserDistinctDinners(userService.getUsername()));
		model.addAttribute("newDinner", dinner);
		return "add-dinner-step2";
	}
	
	
	@PostMapping("/delete")
	private String showDeteleDinner(@RequestParam long id, Model model, RedirectAttributes redirectAttr) {
		dinnerRepo.deleteById(id);
		//model.addAttribute("dinnerDeleteConfirmation", "Obiad został usunięty z powodzeniem.");
		redirectAttr.addFlashAttribute("dinnerDeleteConfirmation", "Obiad został usunięty z powodzeniem.");
		//System.out.println("1111111111111111111111111 " + model.getAttribute("dinnerDeleteConfirmation"));
		return "redirect:/dinner";
	}
	
	@GetMapping("/ingredients")
	private String showIngredients(Model model, @RequestParam long id) {
		DinnerDetails dinnerDetails = dinnerDetailRepo.getOne(id);

		model.addAttribute("ingredient", dinnerDetails.getIngredients());
		model.addAttribute("dinnerDetails", dinnerDetailRepo.getOne(id));
		model.addAttribute("units", Unit.values());
		//model.addAttribute("ingredientNames", ingredDetailsRepo.findStringNames());
		
		return "ingredients";
	}
	
	@PostMapping("/ingredients")
	private String saveIngredient(@ModelAttribute Ingredient ingredient, @RequestParam long id, Model model) {
		DinnerDetails dinnerDetail = dinnerDetailRepo.getOne(id);
		dinnerDetail.addIngredient(ingredient);
		dinnerDetailRepo.save(dinnerDetail);
		
		return "redirect:/ingredients";
	}
	
	
	
	
	
	
	
	
	// koniec mapping
	private void addNewDinners() {
		User user = userService.getUserById(userService.getUserId());
		List<String> dinnerNames = List.of("tymiankowe pałki z kurczaka", "kotlet schabowy", "kotlet mielony", "barszcz czerwony", "rosół", "spaghetti", "naleśniki", "leczo", "makaron z soczewicą", 
				"pierogi z soczewicą", "pierogi ruskie", "pierogi z kapustą", "pierogi z kaszą gryczaną",
				"kurczak tikka masala", "hamburgery", "placki ziemniaczane", "fasolka po bretońsku", "gulasz", "wątróbka z cebulką", "smażony filet z morszczuka", "zapiekanka makaronowa", 
				"sos mięsny", "zielone curry", "zupa pomidorowa", "gołąbki", "tuńczyk w sosie pomidorowym", "warzywa na patelnię", "karkówka w sosie pomidorowym", "żurek", "smażony filet z mintaja",
				"szpinak z makaronem", "warzywa na patelnię", "karkówka z selerem i pieczarkami", "pierś z kurczaka w sosie musztardowo-miodowym");
		/*for(String name : dinnerNames) {
			DinnerDetails dd = new DinnerDetails(name, "", "");
			dinnerDetailRepo.save(dd);
		}*/
		//dodanie Dinnerów
		Random random = new Random();
		//List<DinnerDetails> detailList = dinnerDetailRepo.findAll();
		//przygotowanie dat
		//List<LocalDate> daty = new List<>();
		for(int i = 1; i < 32; i++) {
			List<String> detailList = dinnerService.getUserDistinctDinners(user.getUsername());
			String dinnerName = dinnerNames.get(random.nextInt(dinnerNames.size()));
			DinnerDetails dd = new DinnerDetails();
			if (detailList.contains(dinnerName)) {
				dd = dinnerDetailRepo.findByDinnerNameIgnoreCase(dinnerName);
			}else{
				dd.setDinnerName(dinnerName);
				dd.setInstruction("");
				dd.setUrl("");
			}
			//DinnerDetails dd = new DinnerDetails(dinnerNames.get(random.nextInt(dinnerNames.size())), "", "");
			user.addDinnerDetail(dd);
			Dinner dinner = new Dinner(dd, user, LocalDate.of(2020,3,i), "");
			Category category = findCategory(dinner);
			dinner.addCategory(category);
			user.addDinner(dinner);
			dinnerService.saveDinner(dinner);
		}
		for(int i = 1; i < 31; i++) {
			List<String> detailList = dinnerService.getUserDistinctDinners(user.getUsername());
			String dinnerName = dinnerNames.get(random.nextInt(dinnerNames.size()));
			DinnerDetails dd = new DinnerDetails();
			if (detailList.contains(dinnerName)) {
				dd = dinnerDetailRepo.findByDinnerNameIgnoreCase(dinnerName);
			}else{
				dd.setDinnerName(dinnerName);
				dd.setInstruction("");
				dd.setUrl("");
			}
			user.addDinnerDetail(dd);
			Dinner dinner = new Dinner(dd, user, LocalDate.of(2020,4,i), "");
			Category category = findCategory(dinner);
			dinner.addCategory(category);
			user.addDinner(dinner);
			dinnerService.saveDinner(dinner);
			
		}
		//dodaj losowe skladniki do obiadow
		List<IngredientDetails> ingrDetList = ingrService.findAll();
		List<DinnerDetails> detailList = dinnerDetailRepo.findAll();
		for(DinnerDetails dinnerDetail : detailList) {
			//4 skladniki
			for(int i = 0 ; i < 5; i++) {
				Ingredient ingr = new Ingredient();
				//wylosuj nazwe skladnika
				Random random2 = new Random();
				ingr.setIngredientDetail(ingrDetList.get(random2.nextInt(ingrDetList.size())));
				user.addIngredientDetail(ingr.getIngredientDetail());
				double genDouble = (double) random.nextInt(5);
				ingr.setQuantity(genDouble);
				ingr.setUnit(Unit.values()[new Random().nextInt(Unit.values().length)]);
				ingr.setUser(userService.getUserById(userService.getUserId()));
				ingr.setDinnerDetail(dinnerDetail);
				dinnerDetail.addIngredient(ingr);
				user.addIngredient(ingr);
				ingrService.saveIngredient(ingr);
			}
		}
	}
	
	private void setDinnerDetails(Dinner dinner, User user) {
		String dinnerName = dinner.getDinnerDetail().getDinnerName();
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx dinnerDetail id z setDinnerDetail: " + dinner.getDinnerDetail().getId());
		//zastąpić if dinner.getdinnerdetail.getid nie jest null, po co zczytaywac z bazy jeszcze raz
		//nie tworzyc pustego obiektu dinnerdetail tylko zapisac go do starego obiadu
		//DinnerDetails dd = new DinnerDetails();
		if (Objects.nonNull(dinner.getDinnerDetail().getId())) {  
			 dinner.setDinnerDetail(dinnerDetailRepo.findByDinnerNameIgnoreCase(dinnerName));
		}else {
			dinner.getDinnerDetail().setDinnerName(dinnerName);
			dinner.getDinnerDetail().setInstruction("");
			dinner.getDinnerDetail().setUrl("");

		user.addDinnerDetail(dinner.getDinnerDetail());
		user.addDinner(dinner);
		
		}
		
		//dinner.setDinnerDetail(dd);
		dinnerService.saveDinner(dinner);
	}
	
	private Category findCategory(Dinner dinner) {
		List<Category> categories = catRepo.findAll();
		Category foundCategory = catRepo.getOne(1L);
		for(Category category : categories) {
			for(CategoryDetails detail : category.getDetails()) {
				//System.out.println("###########################################kategoria: "+ detail.getCategory().getName() + ", detal: " + detail.getDetail());
				if(dinner.getDinnerDetail().getDinnerName().contains(detail.getDetail())) {
					//System.out.println("-------------------------------------------Obiad: " + dinner.getDinnerDetails().getDinnerName()
					//		+ " to kategoria: "+category.getName() +", bo zawiera: " + detail.getDetail());
					foundCategory = category;
					//System.out.println("########## znaleziona kategoria: " + category.getName());
					return foundCategory;
				}//nie moge dac else bo już w pierwszym przypadku gdy nie znajdzie da bezmiesny
			}
		}
		return foundCategory;
	}
	
	private void setCookie( HttpServletResponse response, String key, String value)throws IOException { //int maxAge
	    Cookie cookie = new Cookie( key, URLEncoder.encode( value, "UTF-8" ) );
	    //cookie.setMaxAge( maxAge );
	    response.addCookie( cookie );
	}
}

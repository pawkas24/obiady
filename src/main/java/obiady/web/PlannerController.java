package obiady.web;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
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
import obiady.ShoppingCart;
import obiady.DinnerDetails;
import obiady.DinnerDetailRepository;
import obiady.repository.CategoryRepository;
import obiady.service.DinnerService;
import obiady.service.ShoppingCartService;
import obiady.service.UserService;

@Controller
@RequestMapping("/planer")
//@SessionAttributes("myAdverts") informuje Springa jaki obiekt ma przechować w modelu na dłużej niż tylko żądanie, można usunąć go potem np sessionStatus.setComplete();
public class PlannerController {
	
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
	private ShoppingCartService shoppingService;
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	
	@GetMapping
	public String showPlanerForm(Model model) { 

		String username = userService.getUsername();
		//dodanie do modelu pustego obiektu typu dinner i dinners na nazwe obiadu, który przyjmie dane z formularza
		model.addAttribute("newDinner", new Dinner());
		model.addAttribute("dinnerDetail", new DinnerDetails());   //"dinner"
		//dodanie do modelu unikalnych nazw obiadu z Dinners
		model.addAttribute("dinnerName", dinnerService.getUserDistinctDinners(username)); //dinnerDetailRepo.findAll())
		//lista obiadow "przyszlych" (już dodanych do planera)
		model.addAttribute("plannedList", dinnerService.getPlannedDinners(username));   //!!!!!!!!!!!!!!!przesniesc do plannerService
		//lista id obiadow ktorych skladniki zostaly juz dodane do listy zakupow
		List<Long> dinnerDetailsIds = shoppingService.getDistinctDinnerDetailsIds(username);
		//dinnerDetailsIds.forEach(d->System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL dinnerDetailsIds: " + d));
		model.addAttribute("dinnerDetailsIds", dinnerDetailsIds);
		
		return "planer";  
	}
	
	//request z drugiego kroku
	@PostMapping  //w formularzu akcja=#, przegladarka wysle dane z formularza do tego samego kontrolera z ktorego przyszlo get
							//@ModelAttribute only binds values from post parameters.
	public String newDinnerSubmit(@Valid @ModelAttribute Dinner dinner, Model model, Errors errors, @RequestParam(value="id", required=false) String id,
			RedirectAttributes redirectAttr) { //był long id
		if (errors.hasErrors()) {
			return "planer";
		}
		//jeśli data jest przeszła
		if(Objects.nonNull(dinner.getAteAt()) && dinner.getAteAt().isBefore(LocalDate.now())) {
			redirectAttr.addFlashAttribute("wrongDate", "Wybrałeś datę przeszłą. Spróbuj ponownie.");
			return "redirect:/planer";
		}
		
		//dodanie obiadu do planera, ktory juz jest zapisany w bazie
		if(!Objects.isNull(id)) {
			
			Dinner oldDinner = dinnerRepo.getOne(Long.parseLong(id));
			oldDinner.setAteAt(dinner.getAteAt());
			//oldDinner.setCategories(dinner.getCategories());
			oldDinner.setComment(dinner.getComment());
			oldDinner.setDinnerDetail(dinner.getDinnerDetail());
			dinnerService.saveDinner(oldDinner);
			/*System.out.println("id: " + id);
			System.out.println("old dinner " + oldDinner.getDinnerDetail().getDinnerName() + ", " +oldDinner.getComment());
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx obiad przekazany z formularza: " + dinner.getDinnerDetail().getDinnerName() + ", " + dinner.getComment());*/
			return "redirect:/planer";
		}else {
		setDinnerDetails(dinner);
		dinnerService.saveDinner(dinner);//dopiero po tym mozna pobrac dinner id
		//System.out.println("11111111111111111111111111111111111111111111111111111111 nowy obiad: " + dinner.getDinnerDetail().getDinnerName() + ", " + dinner.getAteAt());
		return "redirect:/planer";
			}
		}
	

	
	@GetMapping("/edit")
	private String showEditDinner(@ModelAttribute Dinner dinner, Model model, @RequestParam String id) { //long id
		//warunek do edycji, dinner z modelu podmienia na dinner pobrany z bd
		//System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111111111111111 id: " +id);
		dinner = dinnerRepo.getOne(Long.parseLong(id));
		//setDinnerDetails(dinner);
		//dinnerService.saveDinner(dinner);//dopiero po tym mozna pobrac dinner id
				System.out.println("2222222222222222222222222222222222222222222222222222  getMappint /edit " + dinner.getDinnerDetail().getDinnerName() +", " + dinner.getComment());
		//List<Category> categories = catRepo.findAll();
		
		//Category foundCategory = findCategory(dinner);
		//dinner.addCategory(foundCategory);
		
		//model.addAttribute("categories", categories);
		//model.addAttribute("foundCategory", foundCategory);
		
		model.addAttribute("dinners", dinnerService.getUserDistinctDinners(userService.getUsername()));
		model.addAttribute("newDinner", dinner);
		return "planer2";
	}
	
	
	@PostMapping("/delete")
	private String showDeteleDinner(@RequestParam long dinnerId, Model model, RedirectAttributes redirectAttr) { //zmienic na string id
		//usunąć składniki danego obiadu z koszyka zakupów jeśli się w nim znajdują
		//pobrać obiad
		Dinner dinner = dinnerService.getOneDinner(dinnerId);
		ShoppingCart shoppingCart = shoppingCartService.findByUserId(dinner.getUser().getId());
		DinnerDetails dinnerDetail = dinner.getDinnerDetail();
		shoppingService.removeBidirectionalAssociation(dinnerDetail, shoppingCart, dinner.getUser().getId());
		
		dinnerRepo.deleteById(dinnerId);
		//model.addAttribute("dinnerDeleteConfirmation", "Obiad został usunięty z powodzeniem.");
		redirectAttr.addFlashAttribute("dinnerDeleteConfirmation", "Obiad został usunięty z powodzeniem.");
		//System.out.println("1111111111111111111111111 " + model.getAttribute("dinnerDeleteConfirmation"));
		return "redirect:/planer";
	}
	

	private void addNewDinners() {
		List<String> dinnerNames = List.of("tymiankowe pałki z kurczaka", "kotlet schabowy", "kotlet mielony", "barszcz czerwony");
		for(String name : dinnerNames) {
			DinnerDetails dd = new DinnerDetails(name, "", "");
			dinnerDetailRepo.save(dd);
		}
		//dodanie Dinnerów
		Random random = new Random();
		List<DinnerDetails> detailList = dinnerDetailRepo.findAll();
		//przygotowanie dat
		//List<LocalDate> daty = new List<>();
		for(int i = 1; i<32; i++) {
			DinnerDetails dd = detailList.get(random.nextInt(dinnerNames.size()));
			Dinner dinner = new Dinner(dd, userService.getUserById(userService.getUserId()), LocalDate.of(2020,3,i), "");  //userRepo.getOne(1L)
			Category category = findCategory(dinner);
			//System.out.println("catcatcatcatcatcatcatcatcatcatcatcatcatcatcat" + category.getName());
		/*	if (Objects.isNull(category)) {
				category = catRepo.findByName("bezmięsny");
			}*/
			dinner.getCategories().add(category);
			//dinner.setComment("");
			dinnerService.saveDinner(dinner);
		}
	}
	
	private void setDinnerDetails(Dinner dinner) {
		String dinnerName = dinner.getDinnerDetail().getDinnerName();
		if (dinnerDetailRepo.existsDinnerDetailsByDinnerName(dinnerName)) {  
			DinnerDetails dinnerDetails = dinnerDetailRepo.findByDinnerNameIgnoreCase(dinnerName);
			dinner.setDinnerDetail(dinnerDetails);
		}
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
				
				//return foundCategory;
			}
		}
		
		/*System.out.println("aaaaaaaaaaaaaaaaa kategoria przed if: " + foundCategory.getName());
		if(Objects.isNull(foundCategory)) {
			foundCategory = catRepo.getOne(1L);
			System.out.println("bbbbbbbbbbbbbbbbb  w srodku if kategoria 1L " + catRepo.getOne(1L));
		}
		System.out.println("############## kategoria po warunku if, powinna byc bezmiesny "+ foundCategory.getName());*/
		return foundCategory;
	}
}

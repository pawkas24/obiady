package obiady.web;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import obiady.DinnerDetails;
import obiady.Ingredient;
import obiady.IngredientDetails;
import obiady.ShopSection;
import obiady.ShoppingCart;
import obiady.ShoppingItem;
import obiady.User;
import obiady.service.DinnerService;
import obiady.service.IngredientService;
import obiady.service.MailService;
import obiady.service.ShopSectionService;
import obiady.service.ShoppingCartService;
import obiady.service.UserService;

@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {
	
	@Autowired
	private DinnerService dinnerService;
	@Autowired
	private UserService userService;
	@Autowired
	private ShoppingCartService shoppingService;
	@Autowired
	private IngredientService ingrService;
	@Autowired
	private MailService mailService;
	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private ShopSectionService shopSectionService;
	 

	@GetMapping
	public String showShoppingCart(Model model, @RequestParam(value = "mailConfirmation", required = false) boolean mailConfirmation) {
		Long userId = userService.getUserId();
		//badge koszyka pokazujace ilosc skladnikow w koszyku
		shoppingService.getNumberOfItemsLeftToBuy(model, userId);
		
		//przygotować dwie listy isBought i notBought i przekazac do modelu
		List<ShoppingItem> itemsBought = shoppingService.getShoppingCartWithSummarizedIngrs(userId, true);
		List<ShoppingItem> itemsNotBought = shoppingService.getShoppingCartWithSummarizedIngrs(userId, false);
		
		model.addAttribute("isBought", itemsBought);
		model.addAttribute("notBought", itemsNotBought);
		
		//pusty obiekt na dane dodatkowego skladnika
		model.addAttribute("shoppingItem", new ShoppingItem());
		//lista nazw skladnikow do datalist
		model.addAttribute("shoppingItems", ingrService.findAllIngredientNamesAsc(userId));
		//notatka zakupowa
		ShoppingCart shoppingCart = shoppingService.findByUserId(userId);
		model.addAttribute("shoppingCart", shoppingCart); //bylo shoppingNote
		model.addAttribute("mailConfirmation", mailConfirmation);
		
		model.addAttribute("shopSections", shopSectionService.findAll()); 

		//model.addAttribute("page", shoppingCart);
		
		//wysłanie na maila
		
		return "shopping-cart";
	}
	
	/*@GetMapping("/edit")
	public String showEditShoppingItem(Model model, @RequestParam String ingrId) {
		Long userId = userService.getUserId();
		List<ShoppingCart> shoppingItems = shoppingService.getShoppingCartItems(userId, ingrName);
		model.addAttribute("shoppingItems", shoppingItems);
		
		return "shopping-cart-edit";
	}*/
	
	@PostMapping("/add-shopping-item")
	public String addShoppingItemToTheCart(@RequestParam(value = "dinnerDetailId", required = false) String dinnerDetailId, 
			@RequestParam(value = "randomDinnerName", required = false) String randomDinnerName, @Valid @ModelAttribute ShoppingItem shoppingItem, Errors errors, 
			Model model) {
		model.addAttribute("shopSections", shopSectionService.findAll());
		if (errors.hasErrors()) {
		
			return "shopping-cart";
		}
		User user = userService.findByUsername(userService.getUsername());

		
		ShoppingCart shoppingCart = shoppingService.findByUserId(user.getId());
		
		
		
		if(Objects.isNull(shoppingCart)) {
			shoppingCart = new ShoppingCart(user);
			shoppingService.saveShoppingCart(shoppingCart);
		}
		
		DinnerDetails dinnerDetail = new DinnerDetails();
		if(Objects.nonNull(dinnerDetailId) || Objects.nonNull(randomDinnerName)) {
			//zamienic string na long i pobrać one dinnerDetail
			
			if(Objects.isNull(dinnerDetailId)) {
				//zamienic na metode if isnumeric
				dinnerDetail = dinnerService.getDinnerDetailByDinnerName(randomDinnerName);
			} else {
				dinnerDetail = dinnerService.getDinnerDetailById(Long.parseLong(dinnerDetailId));
			}
			
			
			//pobrać listę składników danego obiadu
			List<Ingredient> dinnerIngrs = dinnerDetail.getIngredients();
			
			//dla kazdego skladnika utworzyc obiekt shopping cart i wypelnic danymi z danego skladnika
			for(Ingredient ingr : dinnerIngrs) {
				//ShopSection shopSection = shopSectionService.findShopSection(shoppingItem.getIngrDetail().getName(), user.getId());
				//List<Unit> smallUnits = Arrays.asList(Unit.LYZ_L, Unit.LYZ_S, Unit.ZB);
				ShoppingItem newShoppingItem = new ShoppingItem();
				IngredientDetails ingrDetail = ingr.getIngredientDetail();
				
				newShoppingItem.setQuantity(ingr.getQuantity());
				newShoppingItem.setUnit(ingr.getUnit());
				//jesli jednostka miary jest mala zamienic na sztuka
				//zamienic gramy i dag na kg, ml na litry itp niech zsumuje chociaż to
				
				ingrDetail.addShoppingItem(newShoppingItem);
				dinnerDetail.addShoppingItem(newShoppingItem);
				shoppingCart.addShoppingItem(newShoppingItem);
				//ingrService.saveIngrDetail(ingrDetail);
				shoppingService.saveShoppingItem(newShoppingItem);
				
				//shoppingService.saveShoppingCart(shoppingCart);   // z tym dublowalo mi skladniki w tabeli shopping_item
			}
			//badge koszyka pokazujace ilosc skladnikow w koszyku
			shoppingService.getNumberOfItemsLeftToBuy(model, user.getId());
			
			//jeśli randomDinnerName nie jest pusty, przeniesc id obiadu do plannera 
			/*if(!randomDinnerName.equals(null)) {
				jesli randomDinnerName nie jest pusty to 
				plannerserwis.dodaj obiad, 
				return "redirect:/planer?id=" + dinner.id;
			}else {
				
			}*/
			if(randomDinnerName != null) {
				return "redirect:/shopping-cart";
			}
			return "redirect:/planer";
			//}
		}else {
			//jesli skladnik nie pochodzi z przepisu
			//znajdz dla shoppingitem.name w tabeli ingrDetails shopSection.Id
			//n shopSection = shopSectionService.findShopSection(shoppingItem.getIngrDetail().getName(), user.getId());
			
			//IngredientDetails ingrDetail = shoppingItem.getIngrDetail();
			//czy składnik dodawany jako dodatkowy do listy zakupów jest w bazie danych
			
			IngredientDetails ingrDetail = ingrService.findByNameIgnoreCaseAndUser_Id(shoppingItem.getIngrDetail().getName(), user.getId());
			//jeżeli składnik nie istnieje jeszcze w tabeli ingrDetiails
			ShopSection shopSection = new ShopSection();
			if(Objects.isNull(ingrDetail)) { 
				//tworzy nowy obiekt ingrDetail i uzupelnia argumentami
				shopSection = shoppingItem.getIngrDetail().getShopSection();
				ingrDetail = ingrService.setIngredientDetail(shoppingItem.getIngrDetail().getName(), user, false, shopSection);

				//ingrDetail.addShoppingItem(shoppingItem); //26 nie przenos wyzej do if bo bedzie save transient instance before flushing
				user.addIngredientDetail(ingrDetail);
				//userService.saveUser(user); // !!!!!!!!! przez to zapisywało podwójnie nowy IngredientDetails
				ingrService.saveIngrDetail(ingrDetail);

				//shoppingService.saveShoppingCart(shoppingCart);
			}else {
				shopSection = shopSectionService.findShopSection(shoppingItem.getIngrDetail().getName(), user.getId());
			}
			//shop section jesli ma pobierac stary, chyba ze za kazdym razem user ma ustawiac
		
			//utworzyc shoppingItem (skleić)
			ingrDetail.addShoppingItem(shoppingItem);
			shoppingCart.addShoppingItem(shoppingItem);
			//ingrService.saveIngrDetail(ingrDetail);
			shoppingService.saveShoppingItem(shoppingItem);
			
			//badge koszyka pokazujace ilosc skladnikow w koszyku
			shoppingService.getNumberOfItemsLeftToBuy(model, user.getId());
				 
				//jesli dodaje skladnik spoza przepisu, istniejacy już w bazie
			return "redirect:/shopping-cart";
			//IngredientDetails ingrDetail = new IngredientDetails(shoppingItem.getIngrDetail().getName(), user, false, shoppingItem.getIngrDetail().getShopSection());
			//ingrDetail.addShoppingItem(shoppingItem); // nie przenos wyzej do if bo bedzie save transient instance before flushing
			//zamiast tego ingrService.saveIngredientDetail(ingrDetail.getName(), user, false, shopSectionService.getOne(shoppingItem.getIngrDetail().getShopSection().getId()));
			//utworzyc nowy obiekt ingrDetail, przypisac mu pola, potem to nizej
			//i dopiero ingrService.saveIngrDetail(ingrDetail);
			//ingrService.saveIngrDetail(ingrDetail);

			//userIngrService.saveUserIngrIfNotExists(shoppingItem.getIngrName(), user);
			//shoppingItem.setUser(user);   wywalone przy zmianie na shopping cart/item
			}
	}

	@PostMapping("/add-shopping-note")
	public String addShoppingNoteToTheCart(Model model, @RequestParam String shoppingNote, RedirectAttributes redirectAttr) {
		Long userId = userService.getUserId();
		
		//badge koszyka pokazujace ilosc skladnikow w koszyku
		shoppingService.getNumberOfItemsLeftToBuy(model, userId);
		
		ShoppingCart shoppingCart = shoppingService.findByUserId(userId);
		shoppingCart.setNote(shoppingNote);
		
		//user.setShoppingNote(shoppingNote);
		//userService.saveUser(user);
		shoppingService.saveShoppingCart(shoppingCart);
		redirectAttr.addFlashAttribute("saveShoppingNoteConfirmation", "Notatka została zapisana z powodzeniem");
		
		return "redirect:/shopping-cart";
		}
	
	@PostMapping("/delete")
	public String deleteFromShoppingCart(@RequestParam(value="dinnerDetailId", required=false)  String dinnerDetailId, Model model) {
		//planer usun skladniki obiadu 
		//lista zakupow button wyczysc
		Long userId = userService.getUserId();
		//badge koszyka pokazujace ilosc skladnikow w koszyku
		shoppingService.getNumberOfItemsLeftToBuy(model, userId);
		
		ShoppingCart shoppingCart = shoppingService.findByUserId(userId);
		
		if(Objects.isNull(dinnerDetailId)) {
			List<ShoppingItem> shoppingItems = shoppingService.findShoppingItemsByUserId(userId);
			for(ShoppingItem item : shoppingItems) {
				//to ifffff jest BEZ SENSU!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				if(Objects.nonNull(item.getDinnerDetail())) {
					item.getDinnerDetail().removeShoppingItem(item);
				}
				shoppingCart.removeShoppingItem(item);

			}
				shoppingService.deleteShoppingCart(shoppingCart);
			return "redirect:/shopping-cart";
		}else {
				DinnerDetails dinnerDetail = dinnerService.getOneDinnerDetail(Long.parseLong(dinnerDetailId));
				shoppingService.removeBidirectionalAssociation(dinnerDetail, shoppingCart,userId);

		}
		return "redirect:/planer";
	}
	

	
	@PostMapping("/change-status")
	public String removeFromShoppingCart(@RequestParam("ingrName") String cartIngrName, Model model) {
		Long userId = userService.getUserId();
		
		//badge koszyka pokazujace ilosc skladnikow w koszyku
		shoppingService.getNumberOfItemsLeftToBuy(model, userId);
		
		List<ShoppingItem> shoppingItem = shoppingService.getShoppingCartItems(userId, cartIngrName);
		for(ShoppingItem item : shoppingItem) {
			item.setBought(!item.isBought());
			shoppingService.saveShoppingItem(item);
		}
		return "redirect:/shopping-cart";
	}
	
	@GetMapping("/send-mail")
	public String sendMail(RedirectAttributes redirectAttr) throws MessagingException {
		Long userId = userService.getUserId();
		
		//List<ShoppingItem> shoppingList = shoppingService.findByUserId(user.getId()).getShoppingItems();
		ShoppingCart shoppingCart = shoppingService.findByUserId(userId);
		
		String to = "pawkas24@gmail.com";
		String subject = "Obiadomat - lista zakupów, " + LocalDate.now();
		
		Context context = new Context();
		context.setVariable("shoppingCart", shoppingCart);
		String body = templateEngine.process("email-template", context);
		try {
		mailService.sendMail(to, subject, body, true);
		}catch (MailSendException m) {
			System.out.println(m.getMessage());
		}
		
		boolean confirmation = true;
		redirectAttr.addAttribute("mailConfirmation", confirmation);
		return "redirect:/shopping-cart";
	}
	

	public void removeBidirectionalAssociation(DinnerDetails dinnerDetail, ShoppingItem shoppingItem, ShoppingCart shoppingCart) {
		dinnerDetail.removeShoppingItem(shoppingItem);
		shoppingCart.removeShoppingItem(shoppingItem);
		shoppingService.deleteShoppingItem(shoppingItem);
	}
	


	private ShoppingItem getTheSameItemFromTheShoppingCart (ShoppingItem newItem, List<ShoppingItem> cart) {
		for(ShoppingItem item : cart) {
			if (newItem.getIngrDetail().getName().equals(item.getIngrDetail().getName())){
				if(newItem.getUnit().equals(item.getUnit())) {
					return item;
					}
			}
		}
		return null;
	}
}
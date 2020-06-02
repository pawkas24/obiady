package obiady.web;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import obiady.Dinner;
import obiady.User;
import obiady.service.DinnerService;
import obiady.service.ShoppingCartService;
import obiady.service.UserService;

@Controller
@RequestMapping("/history")
public class HistoryController {

	@Autowired
	private DinnerService dinnerService;
	@Autowired
	private UserService userService;
	@Autowired
	private ShoppingCartService shoppingService;
	
	
	@GetMapping
	public String showHistory(Model model, @PageableDefault(page = 0, size = 14) Pageable pageable) {
		
		User user = userService.getUser();
		//badge koszyka pokazujace ilosc skladnikow w koszyku
		shoppingService.getNumberOfItemsLeftToBuy(model, user.getId());
		
		Page<Dinner> page = dinnerService.getListOfUserDinners(user.getUsername(), pageable);
		
		model.addAttribute("page", page);
		
		
		return "history";
	}
}

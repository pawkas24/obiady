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
import obiady.service.DinnerService;
import obiady.service.UserService;

@Controller
@RequestMapping("/history")
public class HistoryController {

	@Autowired
	private DinnerService dinnerService;
	@Autowired
	private UserService userService;

	
	
	@GetMapping
	public String showHistory(Model model, @PageableDefault(page = 0, size = 14) Pageable pageable) {
		
		String username = userService.getUsername();
		Page<Dinner> page = dinnerService.getListOfUserDinners(username, pageable);
		
		model.addAttribute("page", page);
		
		
		return "history";
	}
}

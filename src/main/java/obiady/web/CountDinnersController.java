package obiady.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import obiady.service.CountDinnerService;
import obiady.service.UserService;

@Controller
@RequestMapping("/count-dinners")
public class CountDinnersController {
	
	@Autowired
	private CountDinnerService countService;
	@Autowired
	private UserService userService;
	
	@GetMapping
	public String showResults(Model model) {
		Map<String, Long> result = countService.countDistinctDinners(userService.getUsername());
		model.addAttribute("result", result);
		return "count-dinners";

	}
}

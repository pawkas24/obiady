package obiady.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import obiady.User;
import obiady.service.UserService;

@Controller
@RequestMapping("/register")
public class UserRegistrationController {

	@Autowired
	private UserService userService;
	
	@GetMapping
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}
	
	@PostMapping
	public String registerUserAccount(@Valid @ModelAttribute User user, BindingResult result) {
		User existing = userService.findByUsername(user.getUsername());
		if(existing != null) {
			result.rejectValue("username", null , "Istnieje konto z podaną nazwą użytkownika");
		}
		
		if(result.hasErrors()) {
			return "register";
		}
		
		userService.addWithDefaultRole(user);
		return "registerSuccess";
	}
	
}

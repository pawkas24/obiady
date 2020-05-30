package obiady.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import obiady.User;
import obiady.service.UserService;

//@Controller
public class UserController {

private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping("/register")
	public String addUser(@ModelAttribute @Valid User user,
			BindingResult bindResult) {
		if(bindResult.hasErrors())
			return "register";
		else {
			userService.addWithDefaultRole(user);
			return "registerSuccess";
		}
	}
}

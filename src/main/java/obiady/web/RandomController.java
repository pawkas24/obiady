package obiady.web;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import obiady.service.CategoryService;
import obiady.service.RandomService;
import obiady.service.UserService;

@Controller
@RequestMapping("/random")
public class RandomController {

	@Autowired
	private RandomService randomService;
	@Autowired
	private CategoryService catService;
	@Autowired
	private UserService userService;
	
	@GetMapping
	public String showRandom(Model model, @RequestParam(value="category", required=false) String category) {
		if(Objects.isNull(category)) {
			category = "wszystkie";
		}
		model.addAttribute("category", category);
		model.addAttribute("categories", catService.getCategoryNamesAscending());
		randomService.saveRandomDinnerAndDateToTheModel(model, category, userService.getUserById(userService.getUserId()));
		return "random";
	}
}

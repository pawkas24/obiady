package obiady.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/suggestions")
public class SuggestionsController {

	@GetMapping
	public String showSuggestions(Model model) {
		return "suggestions";
	}
}

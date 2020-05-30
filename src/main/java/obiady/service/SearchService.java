package obiady.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SearchService {
	
	public String[] prepareIngrNames(String ingrNames) {
		String[] ingredients = ingrNames.split(",");
		for(int i = 0; i < ingredients.length; i++) {
			ingredients[i] = ingredients[i].trim();
		}
		return ingredients;
	}
	
/*	public List<DinnerDetails> findDinnersContainingIngredients(String[] searchIngr){
		//dla każdego składnika w tabeli znajdż obiad który ma ingredient jak pozycja w tabeli
		@Query("SELECT l1 FROM Location l1 WHERE l1.node.id IN :ids")
		List<Location> findLocationsByNodeIds(@Param("ids") Set<String> ids);
		
		List<DinnerDetails> dinnersList 
		return 
	}*/
	public List<String> getNotEmptyParams(List<String> searchParams){
		List<String> withoutEmptyParams = new ArrayList<>();
		for(String par : searchParams) {
			if(!par.trim().isEmpty()) {
				withoutEmptyParams.add(par);
			}
		}
		return withoutEmptyParams;
	}
	
}


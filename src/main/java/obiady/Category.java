package obiady;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Category {

	@Id
	private Long id;
	private String name;
	//PORK, POULTRY, BEAF, SOUP, FISH dodac w kontrolerze tymczasowo
}

package obiady;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Category {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;  //
	@OneToMany(mappedBy="category", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CategoryDetails> details = new ArrayList<>();
	@ManyToMany(mappedBy="categories")
	private Set<Dinner> dinners = new HashSet<>();
	
	public Category() {
	}
	
	public Category(Long id, String name, List<CategoryDetails> details) {
		this.id = id;
		this.name = name;
		this.details = details;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CategoryDetails> getDetails() {
		return details;
	}

	public void setDetails(List<CategoryDetails> details) {
		this.details = details;
	}

	public Set<Dinner> getDinners() {
		return dinners;
	}

	public void setDinners(Set<Dinner> dinners) {
		this.dinners = dinners;
	}

	
	
	//add detail  //zaczytam z bd

	//remove detail
	
		
	/*private boolean pork;
	private boolean poultry;
	private boolean beaf;
	private boolean seaFood;
	private boolean vension; //dziczyzna
	private boolean veal;  //cielęcina
	private boolean lamb; //jagnięcina
	private boolean mutton; //baranina
	private boolean noMeat; //bezmięsny
	private boolean soup;
	//private boolean fast; //szybki
	//private boolean special; //okazyjny*/
	

	

}

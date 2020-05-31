package obiady;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import constraint.group.HistoryValid;
import constraint.group.PlannerValid;

@Entity
@Table
public class DinnerDetails implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/*@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Size(min=2, max=50)
	private String dinnerName;
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Dinner> dinnersDistinct = new ArrayList<>();  //chyba nie distinct*/
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	//@NotNull //(groups = {HistoryValid.class, PlannerValid.class})
	//@Size(min=3, message = "{obiady.Dinner.dinnerDetail.dinnerName.Size}")
	private String dinnerName;
	//@Column (length=3000)   chyba nie ma co ograniczac
	private String instruction;
	private String url;
	

	
	@OneToMany(mappedBy="dinnerDetail", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Ingredient> ingredients = new ArrayList<>();
	
	@OneToMany(mappedBy="dinnerDetail", cascade = CascadeType.ALL, orphanRemoval = true)  //mappedBy nazwa pola w innej klasie
	List<Dinner> dinnersDistinct = new ArrayList<>();  //chyba nie distinct
	
	@OneToMany(mappedBy = "dinnerDetail", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ShoppingItem> shoppingItems = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	public DinnerDetails(String dinnerName, String instruction, String url) {
		this.dinnerName = dinnerName;
		this.instruction = instruction;
		this.url = url;
	}
	
	public DinnerDetails() {
	}
	
		
	public void addDinner(Dinner dinner) {
		dinnersDistinct.add(dinner);
		dinner.setDinnerDetail(this);
	}
	public void removeDinner(Dinner dinner) {
		dinnersDistinct.remove(dinner);
		dinner.setDinnerDetail(null);
	}
	
	public void addIngredient(Ingredient ingredient) {
		ingredients.add(ingredient);
		ingredient.setDinnerDetail(this);
	}
	public void removeIngredient(Ingredient ingredient){
		ingredients.remove(ingredient);
		ingredient.setDinnerDetail(null);
	}
	public void addShoppingItem(ShoppingItem shoppingItem) {
		shoppingItems.add(shoppingItem);
		shoppingItem.setDinnerDetail(this);
	}
	public void removeShoppingItem(ShoppingItem shoppingItem){
		shoppingItems.remove(shoppingItem);
		shoppingItem.setDinnerDetail(null);
	}

	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDinnerName() {
		return dinnerName;
	}

	public void setDinnerName(String dinnerName) {
		this.dinnerName = dinnerName;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public List<Dinner> getDinnersDistinct() {
		return dinnersDistinct;
	}

	public void setDinnersDistinct(List<Dinner> dinnersDistinct) {
		this.dinnersDistinct = dinnersDistinct;
	}

	
	
	public List<ShoppingItem> getShoppingItems() {
		return shoppingItems;
	}

	public void setShoppingItems(List<ShoppingItem> shoppingItems) {
		this.shoppingItems = shoppingItems;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dinnerName, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DinnerDetails other = (DinnerDetails) obj;
		return Objects.equals(dinnerName, other.dinnerName) && Objects.equals(user, other.user);
	}




}

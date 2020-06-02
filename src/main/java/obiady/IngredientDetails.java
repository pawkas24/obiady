package obiady;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table
public class IngredientDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank (message = "{obiady.Ingredient.ingredientDetail.name.NotBlank}") 
	private String name;
	boolean receipe;
	
	@OneToMany(mappedBy = "ingredientDetail", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ingredient> ingredients = new ArrayList<>();
	//private BigDecimal price;
	 
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	@OneToMany(mappedBy = "ingrDetail", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ShoppingItem> shoppingItems = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	private ShopSection shopSection;
	
	public IngredientDetails() {
		super();
	}

	public IngredientDetails(String name, User user, boolean isRecipeOrigin, ShopSection shopSection) {
		super();
		this.name = name;
		this.user = user;
		this.receipe = isRecipeOrigin;
		this.shopSection = shopSection;
	}

	
	public void addShoppingItem(ShoppingItem shoppingItem) {
		shoppingItems.add(shoppingItem);
		shoppingItem.setIngrDetail(this);
	}
	public void removeShoppingItem(ShoppingItem shoppingItem){
		shoppingItems.remove(shoppingItem);
		shoppingItem.setIngrDetail(null);
	}
	public void addIngredient(Ingredient ingredient) {
		ingredients.add(ingredient);
		ingredient.setIngredientDetail(this);
	}
	public void removeIngredient(Ingredient ingredient){
		ingredients.remove(ingredient);
		ingredient.setIngredientDetail(null);
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

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	public boolean getReceipe() {
		return receipe;
	}

	public void setReceipe(boolean receipe) {
		this.receipe = receipe;
	}

		
	public ShopSection getShopSection() {
		return shopSection;
	}

	public void setShopSection(ShopSection shopSection) {
		this.shopSection = shopSection;
	}

	
	
	public List<ShoppingItem> getShoppingItems() {
		return shoppingItems;
	}

	public void setShoppingItems(List<ShoppingItem> shoppingItems) {
		this.shoppingItems = shoppingItems;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IngredientDetails other = (IngredientDetails) obj;
		return Objects.equals(name, other.name) && Objects.equals(user, other.user);
	}


  
	
	
}

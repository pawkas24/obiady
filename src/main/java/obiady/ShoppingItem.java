package obiady;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import obiady.Utility.Unit;

@Table
@Entity
public class ShoppingItem {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)  //IDENTITY
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private IngredientDetails ingrDetail;
	
	private Unit unit;
	private double quantity;
	private boolean isBought;
	//private String shopSection;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private ShoppingCart shoppingCart;
	
	//@ManyToOne(fetch = FetchType.LAZY)
	//private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private DinnerDetails dinnerDetail;

	public ShoppingItem(IngredientDetails ingrDetail, Unit unit, int quantity, User user, DinnerDetails dinnerDetail, String shopSection) {
		super();
		//this.ingrName = ingrName;
		this.ingrDetail = ingrDetail;
		this.unit = unit;
		this.quantity = quantity;
		//this.user = user;
		this.dinnerDetail = dinnerDetail;
		this.isBought = false;
		//this.shopSection = shopSection;
	}

	public ShoppingItem() {
		super();
		this.isBought = false;
	}

	private List<String> getUnits(){ 
		List<String> units = new ArrayList<>();
		Arrays.asList(Unit.values()).forEach(unit->units.add(unit.toString()));
		return units;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*public String getIngrName() {
		return ingrName;
	}

	public void setIngrName(String ingrName) {
		this.ingrName = ingrName;
	}*/
	

	public Unit getUnit() {
		return unit;
	}

	public IngredientDetails getIngrDetail() {
		return ingrDetail;
	}

	public void setIngrDetail(IngredientDetails ingrDetail) {
		this.ingrDetail = ingrDetail;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

/*	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}*/
	
		
	public boolean isBought() {
		return isBought;
	}

	/*public String getShopSection() {
		return shopSection;
	}

	public void setShopSection(String shopSection) {
		this.shopSection = shopSection;
	}*/

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	public void setBought(boolean isBought) {
		this.isBought = isBought;
	}

	public DinnerDetails getDinnerDetail() {
		return dinnerDetail;
	}

	public void setDinnerDetail(DinnerDetails dinnerDetail) {
		this.dinnerDetail = dinnerDetail;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ingrDetail, quantity, unit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShoppingItem other = (ShoppingItem) obj;
		return Objects.equals(ingrDetail, other.ingrDetail)
				&& Double.doubleToLongBits(quantity) == Double.doubleToLongBits(other.quantity) && unit == other.unit;
	}


}

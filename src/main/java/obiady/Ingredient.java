package obiady;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import obiady.Utility.Unit;

@Entity
@Table
public class Ingredient {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)  //IDENTITY
	private Long id;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@Valid
	private IngredientDetails ingredientDetail;
	
	private double quantity;
	private Unit unit;
	//private String comment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private DinnerDetails dinnerDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	private List<String> getUnits(){ 
		List<String> units = new ArrayList<>();
		Arrays.asList(Unit.values()).forEach(unit->units.add(unit.toString()));
		return units;
	}
	
	public Ingredient() {
		this.quantity = 1.0;
}
	
	public Ingredient(double quantity, Unit unit, IngredientDetails ingredientDetail, DinnerDetails dinnerDetail) {
	this.quantity = 1.0;
	this.unit = unit;
	this.ingredientDetail = ingredientDetail;
	this.dinnerDetail = dinnerDetail;
}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public DinnerDetails getDinnerDetail() {
		return dinnerDetail;
	}

	public void setDinnerDetail(DinnerDetails dinnerDetail) {
		this.dinnerDetail = dinnerDetail;
	}

	public IngredientDetails getIngredientDetail() {
		return ingredientDetail;
	}

	public void setIngredientDetail(IngredientDetails ingredientDetail) {
		this.ingredientDetail = ingredientDetail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}



	@Override
	public int hashCode() {
		return Objects.hash(dinnerDetail, ingredientDetail);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ingredient other = (Ingredient) obj;
		return Objects.equals(dinnerDetail, other.dinnerDetail)
				&& Objects.equals(ingredientDetail, other.ingredientDetail);
	}
	
	
	
}

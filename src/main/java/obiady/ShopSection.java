package obiady;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class ShopSection {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)  //IDENTITY
	private Long id;
	private String section;
	
	@OneToMany(mappedBy = "shopSection", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<IngredientDetails> ingredientDetails = new ArrayList<>();
	
	public ShopSection(String section) {
		super();
		this.section = section;
	}
	public ShopSection() {
		super();
	}
	
	public void addIngredientDetail(IngredientDetails ingrDetail) {
		ingredientDetails.add(ingrDetail);
		ingrDetail.setShopSection(this);
	}
	public void removeIngrDetail(IngredientDetails ingrDetail){
		ingredientDetails.remove(ingrDetail);
		ingrDetail.setShopSection(null);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	
	
	public List<IngredientDetails> getIngredientDetails() {
		return ingredientDetails;
	}
	public void setIngredientDetails(List<IngredientDetails> ingredientDetails) {
		this.ingredientDetails = ingredientDetails;
	}
	@Override
	public int hashCode() {
		return Objects.hash(section);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShopSection other = (ShopSection) obj;
		return Objects.equals(section, other.section);
	}
	
	
	
	
}

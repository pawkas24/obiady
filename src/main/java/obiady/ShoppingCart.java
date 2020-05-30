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
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table
@Entity
public class ShoppingCart {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)  //IDENTITY
	private Long id;
	
	@OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ShoppingItem> shoppingItems = new ArrayList<>();
	
	private String note;
	
	@OneToOne(fetch = FetchType.LAZY)
    @MapsId
	private User user;

	public ShoppingCart() {
		super();
	}

	public ShoppingCart(User user) {
		super();
		this.user = user;
	}
	
	public void addShoppingItem(ShoppingItem shoppingItem) {
		shoppingItems.add(shoppingItem);
		shoppingItem.setShoppingCart(this);
	}
	public void removeShoppingItem(ShoppingItem shoppingItem) {
		shoppingItems.remove(shoppingItem);
		shoppingItem.setShoppingCart(null);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ShoppingItem> getShoppingItems() {
		return shoppingItems;
	}

	public void setShoppingItems(List<ShoppingItem> shoppingItems) {
		this.shoppingItems = shoppingItems;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return Objects.hash(note, shoppingItems, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShoppingCart other = (ShoppingCart) obj;
		return Objects.equals(note, other.note) && Objects.equals(shoppingItems, other.shoppingItems)
				&& Objects.equals(user, other.user);
	}
	
	
}

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


public class UserIngredient {  //dodatkowe składniki dodawane do listy zakupów, nie znajdujące się w przepisach

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)  //IDENTITY
	private Long id;
	
	private String ingrName;
		
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	//konstruktory
	public UserIngredient() {
		super();
	}
	
	public UserIngredient(String ingrName, User user) {
		super();
		this.ingrName = ingrName;
		this.user = user;
	}//koniec konstruktory
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIngrName() {
		return ingrName;
	}

	public void setIngrName(String ingrName) {
		this.ingrName = ingrName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ingrName, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserIngredient other = (UserIngredient) obj;
		return Objects.equals(ingrName, other.ingrName) && Objects.equals(user, other.user);
	}
	
	
	
}

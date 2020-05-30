package obiady;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table
public class User implements Serializable, UserDetails {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String username;
	@NotNull
	private String password;
	@NotNull
	@Email
	private String email;


	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Dinner> dinners = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DinnerDetails> dinnerDetails = new ArrayList<>();
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private Set<UserRole> roles = new HashSet<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RandomDetails> randomDetails = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ingredient> ingredients = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<IngredientDetails> ingrDetails = new ArrayList<>();
	
	//@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	//private List<ShoppingItem> shoppingCarts = new ArrayList<>();
	//a add random detail do List<randomDetails> ??????????????????????
	

	public void addDinner(Dinner dinner) {
		dinners.add(dinner);
		dinner.setUser(this);
	}

	public void removeDinner(Dinner dinner) {
		dinners.remove(dinner);
		dinner.setUser(null);
	}
	
	public void addDinnerDetail(DinnerDetails dinnerDetail) {
		dinnerDetails.add(dinnerDetail);
		dinnerDetail.setUser(this);
	}

	public void removeDinnerDetail(DinnerDetails dinnerDetail) {
		dinnerDetails.remove(dinnerDetail);
		dinnerDetail.setUser(null);
	}
	
	public void addIngredient(Ingredient ingr) {
		ingredients.add(ingr);
		ingr.setUser(this);
	}

	public void removeIngredient(Ingredient ingr) {
		ingredients.remove(ingr);
		ingr.setUser(null);
	}
	
	public void addIngredientDetail(IngredientDetails ingrDetail) {
		ingrDetails.add(ingrDetail);
		ingrDetail.setUser(this);
	}

	public void removeIngredientDetail(IngredientDetails ingrDetail) {
		ingrDetails.remove(ingrDetail);
		ingrDetail.setUser(null);
	}
	


	public User() {
	}

	public User(String username, String email, String password) { // dlaczego bez hasla?
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Dinner> getDinners() {
		return dinners;
	}

	public void setDinners(List<Dinner> dinners) {
		this.dinners = dinners;
	}

	public List<IngredientDetails> getIngrDetails() {
		return ingrDetails;
	}

	public void setIngrDetails(List<IngredientDetails> ingrDetails) {
		this.ingrDetails = ingrDetails;
	}

	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}
	
	public List<RandomDetails> getRandomDetails() {
		return randomDetails;
	}

	public void setRandomDetails(List<RandomDetails> randomDetails) {
		this.randomDetails = randomDetails;
	}
	
	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}



	public List<DinnerDetails> getDinnerDetails() {
		return dinnerDetails;
	}

	public void setDinnerDetails(List<DinnerDetails> dinnerDetails) {
		this.dinnerDetails = dinnerDetails;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(username, other.username);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}

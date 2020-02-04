package obiady;

import java.io.Serializable;
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
public class User implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String username;
	private String email;
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Dinner> dinners = new ArrayList<>();
	
	public void addDinner(Dinner dinner) {
		dinners.add(dinner);
		dinner.setUser(this);
	}
	public void removeDinner(Dinner dinner) {
		dinners.remove(dinner);
		dinner.setUser(null);
	}
	
	public User() {
	}
	public User(String username, String email) {
		this.username = username;
		this.email = email;
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
	
	
	
}

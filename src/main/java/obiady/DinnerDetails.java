package obiady;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class DinnerDetails implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String dinnerName;
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Dinner> dinnersDistinct = new ArrayList<>();
	
	public DinnerDetails(String dinnerName, Dinner dinner) {
		this.dinnerName = dinnerName;
	}

	public DinnerDetails() {
	}
	
	public void addDinner(Dinner dinner) {
		dinnersDistinct.add(dinner);
		dinner.setDinnerDetails(this);
	}
	public void removeDinner(Dinner dinner) {
		dinnersDistinct.remove(dinner);
		dinner.setDinnerDetails(null);
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

	@Override
	public int hashCode() {
		return Objects.hash(dinnerName);
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
		return Objects.equals(dinnerName, other.dinnerName);
	}


}

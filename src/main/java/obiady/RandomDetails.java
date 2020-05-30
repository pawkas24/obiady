package obiady;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table
public class RandomDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String categoryName;
	private String dinnerName;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate dateOfRandom;
	
	/*@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	private User user;*/
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	public RandomDetails() {
	}

	public RandomDetails(String categoryName, String dinnerName, User user) {  
		this.categoryName = categoryName;
		this.dinnerName = dinnerName;
		this.dateOfRandom = LocalDate.now();
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

	public LocalDate getDateOfRandom() {
		return dateOfRandom;
	}

	public void setDateOfRandom(LocalDate dateOfRandom) {
		this.dateOfRandom = dateOfRandom;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	
	
	
}

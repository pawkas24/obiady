	package obiady;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.springframework.format.annotation.DateTimeFormat;

import annotation.FutureOrPresentOrNull;
import annotation.PlanDinner;
import constraint.group.HistoryValid;
import constraint.group.PlannerValid;


@Entity
@Table
//@PlanDinner(groups = {HistoryValid.class, PlannerValid.class})
public class Dinner implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)  //IDENTITY
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@Valid
	private DinnerDetails dinnerDetail;  
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@NotNull(message = "{obiady.Dinner.ateAt.PastOrPresent}", groups = {HistoryValid.class})
	@PastOrPresent(message = "{obiady.Dinner.ateAt.PastOrPresent}", groups = {HistoryValid.class})
	//@FutureOrPresentOrNull(message = "{obiady.Dinner.ateAt.FutureOrPresentOrNull}", groups = {PlannerValid.class})
	@FutureOrPresent(message = "{obiady.Dinner.ateAt.FutureOrPresent}", groups = {PlannerValid.class})
	private LocalDate ateAt;

	@ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name="dinner_category", 
		joinColumns = @JoinColumn(name="dinner_id"),
		inverseJoinColumns = @JoinColumn(name="category_id"))
	private Set<Category> categories = new HashSet<>();
	
	private String comment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	/**
	@PrePersist
	void createdAt(){
		this.ateAt = LocalDate.now();
	}**/

	public Dinner(DinnerDetails dinnerDetail, User user, LocalDate ateAt, String comment) { //Type type //a comment??
		this.dinnerDetail = dinnerDetail;
		this.ateAt = ateAt;
		//this.type = type;
		this.user = user;
		this.comment = comment;
	}

	public Dinner() {
	}
	
	public void addCategory(Category category) {
		categories.add(category);
		category.getDinners().add(this);
	}
	
	public void removeCategory(Category category) {
		categories.remove(category);
		category.getDinners().remove(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DinnerDetails getDinnerDetail() {
		return dinnerDetail;
	}

	public void setDinnerDetail(DinnerDetails dinnerDetail) {
		this.dinnerDetail = dinnerDetail;
	}

	public LocalDate getAteAt() {
		return ateAt;
	}

	public void setAteAt(LocalDate ateAt) {
		this.ateAt = ateAt;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dinner other = (Dinner) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Dinner [id=");
		builder.append(id);
		builder.append(", dinnerDetails=");
		builder.append(dinnerDetail.getDinnerName());
		builder.append(", ateAt=");
		builder.append(ateAt);
		builder.append(", type=");
		builder.append(", user=");
		builder.append(user);
		builder.append("]");
		return builder.toString();
	}
	
	
}

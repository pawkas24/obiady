package obiady;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

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
public class Dinner implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)  //IDENTITY
	private Long id;
	//@OneToOne
	@ManyToOne(fetch = FetchType.LAZY)
	private DinnerDetails dinnerDetails;                 //private String dinnersName;		//wyodrebnic do nowej tabeli, zeby nie powtarzaly sie wpisy
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate ateAt;
	//private Type type;            //w przypadku spornym np kotlety mielone powinno poprosić o zaznaczenie użytego mięsa
	private String comment;
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	/**
	@PrePersist
	void createdAt(){
		this.ateAt = LocalDate.now();
	}**/

	public Dinner(DinnerDetails dinnerDetails, User user, LocalDate ateAt) { //Type type
		this.dinnerDetails = dinnerDetails;
		this.ateAt = ateAt;
		//this.type = type;
		this.user = user;
	}

	public Dinner() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DinnerDetails getDinnerDetails() {
		return dinnerDetails;
	}

	public void setDinnerDetails(DinnerDetails dinnerDetails) {
		this.dinnerDetails = dinnerDetails;
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
		builder.append(dinnerDetails.getDinnerName());
		builder.append(", ateAt=");
		builder.append(ateAt);
		builder.append(", type=");
		builder.append(", user=");
		builder.append(user);
		builder.append("]");
		return builder.toString();
	}
	
	
}

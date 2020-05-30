package obiady.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import obiady.Dinner;
import obiady.RandomDetails;
import obiady.User;
import obiady.UserRepository;
import obiady.UserRole;
import obiady.repository.UserRoleRepository;

@Service
public class UserService {
	//@Autowired
	//UserRepository userRepo;

	private static final String DEFAULT_ROLE = "ROLE_USER";
	private UserRepository userRepository;
	private UserRoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Autowired
	public void setRoleRepository(UserRoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}	

	public void addWithDefaultRole(User user) {
		UserRole defaultRole = roleRepository.findByRole(DEFAULT_ROLE);
		user.getRoles().add(defaultRole);
		String passwordHash = passwordEncoder.encode(user.getPassword());
		user.setPassword(passwordHash);
		userRepository.save(user);
	}
	
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	/*public User getUser() {
		String username = getUsername();
	return userRepository.findByUsername(username);
	}*/
	//pobiera username zalogowanego usera
	public String getUsername() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
		return ((UserDetails)principal).getUsername();
		} else {
		return principal.toString();
			  }
	}
	
	public Long getUserId() {
		String username = getUsername();
		User user = userRepository.findByUsername(username);
		System.out.println("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu user ID: " + user.getId());
		return user.getId();
		
	}
	
	public User getUserById(Long id) {
		return userRepository.getOne(id);
	}
	
	public void saveUser(User user) {
		userRepository.save(user);
	}
}

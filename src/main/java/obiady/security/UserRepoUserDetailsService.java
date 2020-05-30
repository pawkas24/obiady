package obiady.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import obiady.User;
import obiady.UserRepository;

//@Service   juz jest implementacja tego w customUserDetailsService
public class UserRepoUserDetailsService implements UserDetailsService {

	private UserRepository userRepo;
	
	@Autowired
	public UserRepoUserDetailsService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		if (user != null) {
			return user;
		}
		throw new UsernameNotFoundException("Użytkownik '"+ username + "' nie został znaleziony. ");
	}

}

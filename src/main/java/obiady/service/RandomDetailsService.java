package obiady.service;

import obiady.RandomDetails;
import obiady.User;
import obiady.repository.RandomDetailsRepository;

public class RandomDetailsService {

	
	private RandomDetailsRepository randomDetailsRepo;
	
	public RandomDetailsService(RandomDetailsRepository randomDetailsRepo) {
		this.randomDetailsRepo = randomDetailsRepo;
	}
	
	public RandomDetails getRandomDetails(String username) {
		return randomDetailsRepo.findByUser_Username(username);
	}
	
	public void saveRandomData(String categoryName, String dinnerName, User user) {
		randomDetailsRepo.save(new RandomDetails(categoryName, dinnerName, user));
	}
}

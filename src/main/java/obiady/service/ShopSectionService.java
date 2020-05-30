package obiady.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import obiady.ShopSection;
import obiady.repository.ShopSectionRepository;

@Service
public class ShopSectionService {
	
	@Autowired
	private IngredientService ingrService;
	@Autowired
	private ShopSectionRepository shopSectionRepo;
	
	
	public ShopSection findShopSection(String ingrName, Long userId) {
		Long shopSectionId = ingrService.findShopSectionByIngrNameAndUserId(ingrName, userId);
		return shopSectionRepo.getOne(shopSectionId);
	}
	
	public List<ShopSection> findAll(){
		return shopSectionRepo.findAll();
	}
	
	public ShopSection getOne(Long shopSectionId) {
		return shopSectionRepo.getOne(shopSectionId);
	}
}

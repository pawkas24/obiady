package obiady.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import obiady.DinnerRepository;

@Service
public class CountDinnerService {
	
	@Autowired
	private DinnerRepository dinnerRepo;
	
	public Map<String, Long> countDistinctDinners(String username){ 
		Map<String, Long> map = new LinkedHashMap<>();
		List<Object[]> dinners = dinnerRepo.countDinners(username);
		
		for(Object[] dinner : dinners) {
			map.put((String) dinner[0], (Long)dinner[1]);
		}
		return map;
	}
	
}
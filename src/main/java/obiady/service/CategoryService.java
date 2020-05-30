package obiady.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import obiady.Category;
import obiady.repository.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository catRepo;

	public List<String> getCategoryNamesAscending(){
		List<String> categoryList = new ArrayList<>();
		catRepo.findAll().stream().sorted(Comparator.comparing(Category::getName)).forEach(category -> categoryList.add(category.getName()));
		return categoryList;
	}
	
	public List<String> getCategoryNames(){
		List<String> categoryList = new ArrayList<>();
		catRepo.findAll().stream().forEach(category->categoryList.add(category.getName()));
		return categoryList;
	}
}

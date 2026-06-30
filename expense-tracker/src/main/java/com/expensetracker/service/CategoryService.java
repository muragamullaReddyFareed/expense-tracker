package com.expensetracker.service;
import com.expensetracker.dto.*;
import com.expensetracker.entity.*;
import com.expensetracker.exception.ResourceNotFoundException;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.security.AuthenticatedUserProvider;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final AuthenticatedUserProvider userProvider;
    public CategoryService(CategoryRepository cr, AuthenticatedUserProvider up) {
        this.categoryRepository=cr; this.userProvider=up;
    }
    public List<CategoryResponse> getAll() {
        Long uid = userProvider.getCurrentUser().getId();
        return categoryRepository.findByUserIdOrderByNameAsc(uid).stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName(), c.getType())).toList();
    }
    public List<CategoryResponse> getByType(TransactionType type) {
        Long uid = userProvider.getCurrentUser().getId();
        return categoryRepository.findByUserIdAndTypeOrderByNameAsc(uid, type).stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName(), c.getType())).toList();
    }
    public CategoryResponse create(CategoryRequest req) {
        User user = userProvider.getCurrentUser();
        Category category = Category.builder()
                .user(user).name(req.getName().trim()).type(req.getType()).build();
        Category saved = categoryRepository.save(category);
        return new CategoryResponse(saved.getId(), saved.getName(), saved.getType());
    }
    public void delete(Long id) {
        Long uid = userProvider.getCurrentUser().getId();
        Category category = categoryRepository.findByIdAndUserId(id, uid)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }
}
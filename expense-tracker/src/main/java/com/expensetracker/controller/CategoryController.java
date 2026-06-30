package com.expensetracker.controller;
import com.expensetracker.dto.*;
import com.expensetracker.entity.TransactionType;
import com.expensetracker.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) { this.categoryService=categoryService; }
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() { return ResponseEntity.ok(categoryService.getAll()); }
    @GetMapping("/type/{type}")
    public ResponseEntity<List<CategoryResponse>> getByType(@PathVariable TransactionType type) {
        return ResponseEntity.ok(categoryService.getByType(type));
    }
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(req));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id); return ResponseEntity.noContent().build();
    }
}
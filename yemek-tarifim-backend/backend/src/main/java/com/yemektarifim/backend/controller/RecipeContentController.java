package com.yemektarifim.backend.controller;

import com.yemektarifim.backend.model.RecipeContent;
import com.yemektarifim.backend.service.RecipeContentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/recipe-content")
public class RecipeContentController {

    private final RecipeContentService recipeContentService;

    /**
     * @return Veritabanındaki tüm yemek içerik listesini döndürür. (Yemek türünden bağımsız olarak.)
     */
    @GetMapping
    public ResponseEntity<List<RecipeContent>> getAll() {
        List<RecipeContent> recipeContentList = recipeContentService.getAll();
        return ResponseEntity.ok(recipeContentList);
    }

    /**
     * @return Id değerine göre bir yemek içeriğini döndürür.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeContent> getById(@PathVariable String id) {
        RecipeContent recipeContent = recipeContentService.getById(id);
        return ResponseEntity.ok(recipeContent);
    }

    /**
     * @return Recipe Id değerine göre yemek tarifi içeriklerini döndürür.
     */
    @GetMapping("/{recipeId}/contents")
    public ResponseEntity<List<RecipeContent>> getByRecipeId(@PathVariable String recipeId) {
        List<RecipeContent> recipeContentList = recipeContentService.getByRecipeId(recipeId);
        return ResponseEntity.ok(recipeContentList);
    }

    /**
     * Veritabanına yeni bir yemek tarifi içeriği nesnesi ekler.
     */
    @PostMapping
    public ResponseEntity<Boolean> add(@RequestBody List<RecipeContent> recipeContentList) {
        recipeContentService.add(recipeContentList);
        return ResponseEntity.ok(true);
    }

    /**
     * Veritabanındaki bir yemek tarifi içeriğini günceller.
     */
    @PutMapping
    public ResponseEntity<RecipeContent> update(@RequestBody RecipeContent recipeContent) {
        recipeContentService.update(recipeContent);
        return ResponseEntity.ok().build();
    }

    /**
     * Veritabanından bir yemek tarifi içeriğini siler.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<RecipeContent> deleteById(@PathVariable String id) {
        recipeContentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Veri tabanındaki bir yemek tarifine ait içerikleri siler.
     */
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<RecipeContent> deleteByRecipeId(@PathVariable String recipeId) {
        recipeContentService.deleteByRecipeId(recipeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

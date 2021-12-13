package com.yemektarifim.backend.controller;

import com.yemektarifim.backend.model.FriendPostListModel;
import com.yemektarifim.backend.model.Recipe;
import com.yemektarifim.backend.service.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    /**
     * @return Veritabanındaki tüm yemek tariflerinin listesini döndürür.
     */
    @GetMapping
    public ResponseEntity<List<Recipe>> getAll() {
        List<Recipe> recipeList = recipeService.getAll();
        return ResponseEntity.ok(recipeList);
    }

    /**
     * @return Id değerine göre bir yemek tarifini döndürür.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getById(@PathVariable String id) {
        Recipe recipe = recipeService.getById(id);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping("/{username}/me")
    public ResponseEntity<List<Recipe>> getByUsername(@PathVariable String username) {
        List<Recipe> recipeList = recipeService.getByUsername(username);
        return ResponseEntity.ok(recipeList);
    }

    /**
     * @return User name değerine göre, kullanıcının arkadaşlarının paylaştığı yemek tariflerini döndürür.
     */
    @GetMapping("/{username}/friend-recipe")
    public ResponseEntity<List<FriendPostListModel>> getFriendsRecipes(@PathVariable String username) {
        List<FriendPostListModel> friendPostListModelList = recipeService.getFriendRecipesByUsername(username);
        return ResponseEntity.ok(friendPostListModelList);
    }

    /**
     * Veritabanına yeni bir yemek tarifi nesnesi ekler.
     */
    @PostMapping("/{username}")
    public ResponseEntity<String> add(@PathVariable String username, @RequestBody Recipe recipe) {
        return ResponseEntity.ok(recipeService.add(username, recipe));
    }

    /**
     * Veritabanındaki bir yemek tarifini günceller.
     */
    @PutMapping
    public ResponseEntity<Recipe> update(@RequestBody Recipe recipe) {
        recipeService.update(recipe);
        return ResponseEntity.ok().build();
    }

    /**
     * Veritabanından bir yemek tarifini siler.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Recipe> deleteById(@PathVariable String id) {
        recipeService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Veri tabanındaki bir yemek tarifini user id değerine göre siler.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Recipe> deleteByUserId(@PathVariable String userId) {
        recipeService.deleteByUserId(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

package com.yemektarifim.backend.controller;

import com.yemektarifim.backend.model.LifeChainBlock;
import com.yemektarifim.backend.model.LifeChainRequest;
import com.yemektarifim.backend.model.Recipe;
import com.yemektarifim.backend.model.Score;
import com.yemektarifim.backend.service.ScoreService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/score")
public class ScoreController {

    private final ScoreService scoreService;

    /**
     * @return Veritabanındaki tüm score'ların listesini döndürür.
     */
    @GetMapping
    public ResponseEntity<List<Score>> getAll() {
        List<Score> scoreList = scoreService.getAll();
        return ResponseEntity.ok(scoreList);
    }

    /**
     * @return Id değerine göre bir score bilgisini döndürür.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Score> getById(@PathVariable String id) {
        Score score = scoreService.getById(id);
        return ResponseEntity.ok(score);
    }

    /**
     * @return User Id değerine göre score bilgisi döndürür.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Score> getByUserId(@PathVariable String userId) {
        Score score = scoreService.getByUserId(userId);
        return ResponseEntity.ok(score);
    }

    /**
     * Veritabanına yeni bir score nesnesi ekler.
     */
    @PostMapping
    public ResponseEntity<Score> add(@RequestBody Score score) {
        scoreService.add(score);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Veritabanındaki bir score bilgisini günceller.
     */
    @PutMapping
    public ResponseEntity<Score> update(@RequestBody Score score) {
        scoreService.update(score);
        return ResponseEntity.ok().build();
    }

    /**
     * Veritabanından bir score bilgisini siler.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Score> deleteById(@PathVariable String id) {
        scoreService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Veri tabanındaki bir score'u user id değerine göre siler.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Score> deleteByUserId(@PathVariable String userId) {
        scoreService.deleteByUserId(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("competition")
    public ResponseEntity<List<Recipe>> getRecipeListForCompetition() {
        List<Recipe> recipeList = scoreService.getRecipeListForCompetition();
        return ResponseEntity.ok(recipeList);
    }

    @PostMapping("{username}/lifechain")
    public ResponseEntity<List<LifeChainBlock>> setScore(@PathVariable String username, @RequestBody LifeChainRequest lifeChainRequest) {
        List<LifeChainBlock> lifeChainBlockList = scoreService.setScore(username, lifeChainRequest);
        return ResponseEntity.ok(lifeChainBlockList);
    }

    @GetMapping("/lifechain")
    public ResponseEntity<List<LifeChainBlock>> getLifeChainList() {
        List<LifeChainBlock> lifeChainBlockList = scoreService.getLifeChainList();
        return ResponseEntity.ok(lifeChainBlockList);
    }
}

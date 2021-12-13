package com.yemektarifim.backend.controller;

import com.yemektarifim.backend.model.Like;
import com.yemektarifim.backend.service.LikeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/like")
public class LikeController {

    private final LikeService likeService;

    /**
     * @return Tüm beğenilerin listesini döndürür.
     */
    @GetMapping
    public ResponseEntity<List<Like>> getAll() {
        List<Like> likeList = likeService.getAll();
        return ResponseEntity.ok(likeList);
    }

    /**
     * @return Id değerine göre bir beğeni durumu.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Like> getById(@PathVariable String id) {
        Like like = likeService.getById(id);
        return ResponseEntity.ok(like);
    }

    /**
     * Veritabanına yeni bir beğeni durumu ekler.
     */
    @PostMapping
    public ResponseEntity<Like> add(@RequestBody Like like) {
        likeService.add(like);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Veritabanındaki bir beğeni bilgisini günceller.
     */
    @PutMapping
    public ResponseEntity<Like> update(@RequestBody Like like) {
        likeService.update(like);
        return ResponseEntity.ok().build();
    }

    /**
     * Veritabanından bir beğeniyi siler.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Like> deleteById(@PathVariable String id) {
        likeService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Veritabanından kullanıcının tüm beğenilerini siler.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Like> deleteByUserId(@PathVariable String userId) {
        likeService.deleteByUserId(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

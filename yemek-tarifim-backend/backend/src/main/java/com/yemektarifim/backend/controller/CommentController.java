package com.yemektarifim.backend.controller;

import com.yemektarifim.backend.model.Comment;
import com.yemektarifim.backend.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    /**
     * @return Tüm yorumların listesini döndürür.
     */
    @GetMapping
    public ResponseEntity<List<Comment>> getAll() {
        List<Comment> commentList = commentService.getAll();
        return ResponseEntity.ok(commentList);
    }

    /**
     * @return Kullanıcının yemek tariflerine yaptığı belirli bir yorumu döndürür.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getById(@PathVariable String id) {
        Comment comment = commentService.getById(id);
        return ResponseEntity.ok(comment);
    }

    /**
     * Yemek tarifine yapılan yorumu veritabanına kaydeder.
     */
    @PostMapping
    public ResponseEntity<Comment> add(@RequestBody Comment comment) {
        commentService.add(comment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Yapılan yorumu günceller.
     */
    @PutMapping
    public ResponseEntity<Comment> update(@RequestBody Comment comment) {
        commentService.update(comment);
        return ResponseEntity.ok().build();
    }

    /**
     * Yorumu siler.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Comment> deleteById(@PathVariable String id) {
        commentService.deleteByUserId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

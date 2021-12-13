package com.yemektarifim.backend.service;

import com.yemektarifim.backend.mapper.CommentMapper;
import com.yemektarifim.backend.model.Comment;
import com.yemektarifim.backend.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * @return Tüm yorumların listesi
     */
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    /**
     * @param id Kullanıcının yorum id değeri
     * @return O id değerine sahip yorum.
     */
    public Comment getById(String id) {
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException(
                String.format("Cannot Find Comment by Id %s", id)
        ));
    }

    /**
     * Veritabanına yorumu kaydeder.
     */
    public void add(Comment comment) {
        commentRepository.insert(comment);
    }

    /**
     * Veritabanındaki bir yorumun bilgilerini günceller.
     */
    public void update(Comment comment) {
        Comment database = commentRepository.findById(comment.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot find Comment by Id %s", comment.getId())
                ));

        Comment result = CommentMapper.update(database, comment);

        commentRepository.save(result);
    }

    /**
     * @param userId Yorum yapan kullanıcının user id değeri
     */
    public void deleteByUserId(String userId) {
        commentRepository.deleteByCommentedUserId(userId);
    }

}

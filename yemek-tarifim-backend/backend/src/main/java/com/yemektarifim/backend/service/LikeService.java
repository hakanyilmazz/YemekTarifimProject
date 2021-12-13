package com.yemektarifim.backend.service;

import com.yemektarifim.backend.mapper.LikeMapper;
import com.yemektarifim.backend.model.Like;
import com.yemektarifim.backend.repository.LikeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;

    /**
     * @return Veritabanındaki beğenilerin listesi
     */
    public List<Like> getAll() {
        return likeRepository.findAll();
    }

    /**
     * @param id beğeninin id değeri
     * @return O id değerine sahip beğeni.
     */
    public Like getById(String id) {
        return likeRepository.findById(id).orElseThrow(() -> new RuntimeException(
                String.format("Cannot Find Like Object by Id %s", id)
        ));
    }

    /**
     * Veritabanına like nesnesini kaydeder.
     */
    public void add(Like like) {
        likeRepository.insert(like);
    }

    /**
     * Veritabanındaki bir like nesnesinin bilgilerini günceller.
     */
    public void update(Like like) {
        Like database = likeRepository.findById(like.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot find Like Object by Id %s", like.getId())
                ));

        Like result = LikeMapper.update(database, like);

        likeRepository.save(result);
    }

    /**
     * Veritabanından bir beğeni nesnesini siler.
     *
     * @param id Beğeni objesinin id değeri.
     */
    public void deleteById(String id) {
        likeRepository.deleteById(id);
    }

    /**
     * Kullanıcının beğenilerini siler. Fakat kullanıcının beğendiği veriler hala veritabanında duracaktır.
     *
     * @param userId Beğeniyi yapan kullanıcının user id değeri.
     */
    public void deleteByUserId(String userId) {
        likeRepository.deleteByLikedUserId(userId);
    }

}

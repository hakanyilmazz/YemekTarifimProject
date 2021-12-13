package com.yemektarifim.backend.service;

import com.yemektarifim.backend.mapper.RecipeContentMapper;
import com.yemektarifim.backend.model.RecipeContent;
import com.yemektarifim.backend.repository.RecipeContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeContentService {

    private RecipeContentRepository recipeContentRepository;

    @Autowired
    public void setRecipeContentRepository(RecipeContentRepository recipeContentRepository) {
        this.recipeContentRepository = recipeContentRepository;
    }

    /**
     * @return Veritabanındaki tüm yemek tarifi içeriklerinin listesi
     */
    public List<RecipeContent> getAll() {
        return recipeContentRepository.findAll();
    }

    /**
     * @param id yemek tarifi içeriğinin id değeri
     * @return O id değerine sahip yemek tarifi içeriği.
     */
    public RecipeContent getById(String id) {
        return recipeContentRepository.findById(id).orElseThrow(() -> new RuntimeException(
                String.format("Cannot Find RecipeContent by Id %s", id)
        ));
    }

    /**
     * Veritabanına message nesnesini kaydeder.
     */
    public void add(List<RecipeContent> recipeContentList) {
        for (RecipeContent recipeContent : recipeContentList) {
            recipeContentRepository.insert(recipeContent);
        }
    }

    /**
     * Veritabanındaki bir yemek tarifi içeriği nesnesinin bilgilerini günceller.
     */
    public void update(RecipeContent recipeContent) {
        RecipeContent database = recipeContentRepository.findById(recipeContent.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot find RecipeContent by Id %s", recipeContent.getId())
                ));

        RecipeContent result = RecipeContentMapper.update(database, recipeContent);

        recipeContentRepository.save(result);
    }

    /**
     * Veritabanından bir yemek tarifi içeriği nesnesini siler.
     *
     * @param id silinecek olan recipe content objesinin id değeri.
     */
    public void deleteById(String id) {
        recipeContentRepository.deleteById(id);
    }

    /**
     * Yemek tarifi id değerine göre yemek tarifinin içeriklerini liste olarak döndürür..
     *
     * @param recipeId İçeriği listelenecek yemek tarifi içeriğinin id değeri.
     * @return belirli bir yemek tarifine ait yemek tarifi içeriği listesi
     */
    public List<RecipeContent> getByRecipeId(String recipeId) {
        return recipeContentRepository.findByRecipeId(recipeId);
    }

    /**
     * Bir yemek tarifine ait yemek tarifi içeriklerini siler.
     *
     * @param recipeId İçeriği silinecek yemek tarifi
     */
    public void deleteByRecipeId(String recipeId) {
        recipeContentRepository.deleteByRecipeId(recipeId);
    }
}

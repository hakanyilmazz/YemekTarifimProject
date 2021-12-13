package com.yemektarifim.backend.service;

import com.yemektarifim.backend.mapper.RecipeMapper;
import com.yemektarifim.backend.model.FriendPostListModel;
import com.yemektarifim.backend.model.Recipe;
import com.yemektarifim.backend.model.User;
import com.yemektarifim.backend.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RecipeService {

    private RecipeRepository recipeRepository;
    private LoginService loginService;

    @Autowired
    public void setRecipeRepository(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * @return Veritabanındaki tüm yemek tariflerinin listesi
     */
    public List<Recipe> getAll() {
        return recipeRepository.findAll();
    }

    /**
     * @param id yemek tarifinin id değeri
     * @return O id değerine sahip yemek tarifi.
     */
    public Recipe getById(String id) {
        return recipeRepository.findById(id).orElseThrow(() -> new RuntimeException(
                String.format("Cannot Find Recipe by Id %s", id)
        ));
    }

    /**
     * Veritabanına recipe (yemek tarifi) nesnesini kaydeder.
     *
     * @return added Recipe id
     */
    public String add(String username, Recipe recipe) {
        recipe.setUser(loginService.getByUsername(username));
        Recipe insert = recipeRepository.insert(recipe);
        return insert.getId();
    }

    /**
     * Veritabanındaki bir yemek tarifi nesnesinin bilgilerini günceller.
     */
    public void update(Recipe recipe) {
        Recipe database = recipeRepository.findById(recipe.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot find Recipe by Id %s", recipe.getId())
                ));

        Recipe result = RecipeMapper.update(database, recipe);

        recipeRepository.save(result);
    }

    /**
     * Veritabanından bir yemek tarifi nesnesini siler.
     *
     * @param id silinecek olan recipe objesinin id değeri.
     */
    public void deleteById(String id) {
        recipeRepository.deleteById(id);
    }

    /**
     * User id değerine göre yemek tariflerini liste olarak döndürür..
     *
     * @param userId Yemek tarifi paylaşımları listelenecek kullanıcının id değeri.
     * @return belirli bir kullanıcıya ait yemek tarifi listesi
     */
    public List<Recipe> getByUserId(String userId) {
        return recipeRepository.findByUserId(userId);
    }

    /**
     * Kullanıcının tüm yemek tariflerini siler.
     *
     * @param userId Yemek tarifini paylaşan kullanıcının userId değeri.
     */
    public void deleteByUserId(String userId) {
        recipeRepository.deleteByUserId(userId);
    }

    public List<FriendPostListModel> getFriendRecipesByUsername(String username) {
        User user = loginService.getByUsername(username);
        Set<User> friends = user.getFriends();

        if (friends == null) {
            friends = new HashSet<>();
        }

        List<FriendPostListModel> friendPostListModelList = new ArrayList<>();

        for (User tempUser : friends) {
            List<Recipe> friendRecipeList = recipeRepository.findByUserId(tempUser.getId());
            String friendUsername = loginService.getUsernameByLoginId(tempUser.getLogin().getId());

            for (Recipe recipeTemp : friendRecipeList) {
                friendPostListModelList.add(new FriendPostListModel(recipeTemp, friendUsername));
            }
        }

        return friendPostListModelList;
    }

    public List<Recipe> getByUsername(String username) {
        User user = loginService.getByUsername(username);
        return recipeRepository.findByUserId(user.getId());
    }
}

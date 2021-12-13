package com.yemektarifim.backend.service;

import com.yemektarifim.backend.mapper.ScoreMapper;
import com.yemektarifim.backend.model.LifeChainBlock;
import com.yemektarifim.backend.model.LifeChainRequest;
import com.yemektarifim.backend.model.Recipe;
import com.yemektarifim.backend.model.Score;
import com.yemektarifim.backend.repository.RecipeRepository;
import com.yemektarifim.backend.repository.ScoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final RecipeRepository recipeRepository;
    private static List<LifeChainBlock> generalLifeChain = new ArrayList<>();

    /**
     * @return Veritabanındaki tüm score'ların listesi
     */
    public List<Score> getAll() {
        return scoreRepository.findAll();
    }

    /**
     * @param id herhangi bir score'un id değeri
     * @return O id değerine sahip score bilgisi
     */
    public Score getById(String id) {
        return scoreRepository.findById(id).orElseThrow(() -> new RuntimeException(
                String.format("Cannot Find Score by Id %s", id)
        ));
    }

    /**
     * Veritabanına score nesnesini kaydeder.
     */
    public void add(Score score) {
        scoreRepository.insert(score);
    }

    /**
     * Veritabanındaki bir score nesnesinin bilgilerini günceller.
     */
    public void update(Score score) {
        Score database = scoreRepository.findById(score.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot find Score by Id %s", score.getId())
                ));

        Score result = ScoreMapper.update(database, score);

        scoreRepository.save(result);
    }

    /**
     * Veritabanından bir score nesnesini siler.
     *
     * @param id silinecek olan score objesinin id değeri.
     */
    public void deleteById(String id) {
        scoreRepository.deleteById(id);
    }

    /**
     * Kullanıcının yapmış olduğu skorları siler.
     *
     * @param userId kullanıcının userId değeri.
     */
    public void deleteByUserId(String userId) {
        scoreRepository.deleteByUserId(userId);
    }

    /**
     * @param userId Score bilgisi gösterilecek kullanıcının userId değeri.
     * @return belirli bir kullanıcıya ait score bilgisi.
     */
    public Score getByUserId(String userId) {
        return scoreRepository.findByUserId(userId);
    }

    public List<Recipe> getRecipeListForCompetition() {
        List<Recipe> all = recipeRepository.findAll();

        if (all.size() < 5) {
            throw new RuntimeException("No recipe for competition.");
        }

        return all;
    }

    public List<LifeChainBlock> setScore(String username, LifeChainRequest lifeChainRequest) {
        List<LifeChainBlock> lifeChainBlockList = lifeChainRequest.getLifeChainBlockList();
        LifeChainBlock lifeChainBlock = lifeChainRequest.getLifeChainBlock();

        generalLifeChain.removeIf(temp -> temp.getUsername().equals(username));

        if (generalLifeChain == null) {
            generalLifeChain = new ArrayList<>();
        }

        if (isValidLifeChainList(username, lifeChainBlockList)) {
            if (generalLifeChain.isEmpty()) {
                generalLifeChain.add(new LifeChainBlock(username, lifeChainBlock.getScore(), 0));
            } else {
                generalLifeChain.add(new LifeChainBlock(username, lifeChainBlock.getScore(), generalLifeChain.get(generalLifeChain.size() - 1).getBlockHash()));
            }
        } else {
            if (generalLifeChain.isEmpty()) {
                generalLifeChain.add(new LifeChainBlock(username, 0, 0));
            } else {
                generalLifeChain.add(new LifeChainBlock(username, 0, generalLifeChain.get(generalLifeChain.size() - 1).getBlockHash()));
            }
        }

        return generalLifeChain;
    }

    private boolean isValidLifeChainList(String username, List<LifeChainBlock> list) {
        for (LifeChainBlock temp : list) {
            int blockHash = temp.getBlockHash();
            LifeChainBlock tempLifeChain = new LifeChainBlock(username, temp.getScore(), temp.getPreviousBlockHash());

            if (tempLifeChain.getBlockHash() != blockHash) {
                return false;
            }
        }

        return true;
    }

    public List<LifeChainBlock> getLifeChainList() {
        return generalLifeChain;
    }
}

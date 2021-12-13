package com.tezodevi.android.util;

import com.tezodevi.android.model.ResponseRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CompetitionHelper {

    public static List<ResponseRecipe> getCompetitionWithAnswers(List<ResponseRecipe> recipeList) {
        Random random = new Random();

        if (recipeList.size() < 5) {
            return null;
        }

        final List<ResponseRecipe> result = new ArrayList<>();

        int a = -1, b = -1, c = -1;

        for (int i = 0; i < 3; i++) {
            int randomIndex = random.nextInt(recipeList.size());

            while (randomIndex == a || randomIndex == b || randomIndex == c) {
                randomIndex = random.nextInt(recipeList.size());
            }

            if (i == 0) {
                a = randomIndex;
            } else if (i == 1) {
                b = randomIndex;
            } else {
                c = randomIndex;
            }

            result.add(recipeList.get(randomIndex));
        }

        return result;
    }
}

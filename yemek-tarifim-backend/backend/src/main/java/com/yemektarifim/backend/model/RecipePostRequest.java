package com.yemektarifim.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class RecipePostRequest {
    private Recipe recipe;
    private List<RecipeContent> recipeContentList;
}

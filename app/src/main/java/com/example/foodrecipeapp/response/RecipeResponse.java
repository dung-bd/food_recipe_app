package com.example.foodrecipeapp.response;


import com.example.foodrecipeapp.model.RecipesModel;

public class RecipeResponse {
    private RecipesModel recipe;

    public RecipesModel getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipesModel recipe) {
        this.recipe = recipe;
    }
}

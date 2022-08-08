package com.example.foodrecipeapp.api;

import com.example.foodrecipeapp.response.RecipeResponse;
import com.example.foodrecipeapp.response.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealApiServices {
    private static final String BASE_URL = "https://recipesapi.herokuapp.com/";
    private static MealApiServices INSTANCE;
    private RecipeApi recipeApi;

    private int page = 1;

    public MealApiServices() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        recipeApi = retrofit.create(RecipeApi.class);
    }

    public static MealApiServices getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new MealApiServices();
        return INSTANCE;
    }

    public Call<RecipeSearchResponse> searchRecipe(String category) {
        return recipeApi.searchRecipe(category, page);
    }

    public Call<RecipeResponse> getRecipe(String recipe_id) {
        return recipeApi.getRecipe(recipe_id);
    }
}

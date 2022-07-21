package com.example.foodrecipeapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodrecipeapp.api.MealApiServices;
import com.example.foodrecipeapp.model.CategoryModel;
import com.example.foodrecipeapp.model.RecipesModel;
import com.example.foodrecipeapp.response.RecipeResponse;
import com.example.foodrecipeapp.response.RecipeSearchResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealViewModel extends ViewModel {
    public MutableLiveData<RecipeSearchResponse> mutableLiveData = new MutableLiveData<>();
    public MutableLiveData<RecipeResponse> recipesModelMutableLiveData = new MutableLiveData<RecipeResponse>();

    public void getSearchRecipe(String category) {
        MealApiServices.getINSTANCE().searchRecipe(category).enqueue(new Callback<RecipeSearchResponse>() {
            @Override
            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
                mutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {

            }
        });
    }

    public void getRecipe(String recipe_id) {
       MealApiServices.getINSTANCE().getRecipe(recipe_id).enqueue(new Callback<RecipeResponse>() {
           @Override
           public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
               recipesModelMutableLiveData.postValue(response.body());
           }

           @Override
           public void onFailure(Call<RecipeResponse> call, Throwable t) {

           }
       });
    }

    public void getCategoryModel() {
        ArrayList<CategoryModel> categoryModels = new ArrayList<>();
        categoryModels.add(new CategoryModel("Breakfast"));
        categoryModels.add(new CategoryModel("Barbeque"));
        categoryModels.add(new CategoryModel("Brunch"));
        categoryModels.add(new CategoryModel("Chicken"));
        categoryModels.add(new CategoryModel("Beef"));
        categoryModels.add(new CategoryModel("Dinner"));
        categoryModels.add(new CategoryModel("Italian"));
        categoryModels.add(new CategoryModel("Wine"));
    }
}

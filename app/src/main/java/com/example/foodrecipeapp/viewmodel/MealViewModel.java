package com.example.foodrecipeapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodrecipeapp.api.MealApiServices;
import com.example.foodrecipeapp.model.CategoryModel;
import com.example.foodrecipeapp.model.RecipesModel;
import com.example.foodrecipeapp.repo.FoodRepo;
import com.example.foodrecipeapp.response.RecipeResponse;
import com.example.foodrecipeapp.response.RecipeSearchResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealViewModel extends ViewModel {
    FoodRepo foodRepo = new FoodRepo();

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

    public LiveData<ArrayList<CategoryModel>> getCategory() {
        return foodRepo.getCategory();
    }

}

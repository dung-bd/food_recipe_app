package com.example.foodrecipeapp.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodrecipeapp.model.CategoryModel;

import java.util.ArrayList;

public class FoodRepo {
    private MutableLiveData<ArrayList<CategoryModel>> mutableCategoryList;

    public LiveData<ArrayList<CategoryModel>> getCategory() {
        if (mutableCategoryList == null) {
            mutableCategoryList = new MutableLiveData<>();
            loadProducts();
        }
        return mutableCategoryList;
    }

    private void loadProducts() {
        ArrayList<CategoryModel> categoryModels = new ArrayList<>();
        categoryModels.add(new CategoryModel("Breakfast"));
        categoryModels.add(new CategoryModel("Barbeque"));
        categoryModels.add(new CategoryModel("Brunch"));
        categoryModels.add(new CategoryModel("Chicken"));
        categoryModels.add(new CategoryModel("Beef"));
        categoryModels.add(new CategoryModel("Dinner"));
        categoryModels.add(new CategoryModel("Italian"));
        categoryModels.add(new CategoryModel("Wine"));
        mutableCategoryList.setValue(categoryModels);
    }
}

package com.example.foodrecipeapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodrecipeapp.adapter.CategoryAdapter;
import com.example.foodrecipeapp.adapter.MealAdapter;
import com.example.foodrecipeapp.databinding.ActivityMainBinding;
import com.example.foodrecipeapp.model.CategoryModel;
import com.example.foodrecipeapp.response.RecipeSearchResponse;
import com.example.foodrecipeapp.viewmodel.MealViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryListener, MealAdapter.OnMealListener {
    private static final int REQUEST_PERMISSION_CODE = 10;
    private ArrayList<CategoryModel> categoryModels;
    protected MealViewModel mealViewModel;
    private MealAdapter mealAdapter;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mealViewModel = ViewModelProviders.of(this).get(MealViewModel.class);
        mealViewModel.getCategory().observe(this, new Observer<ArrayList<CategoryModel>>() {
            @Override
            public void onChanged(ArrayList<CategoryModel> products) {
                binding.recyclerViewCategory.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                binding.recyclerViewCategory.setLayoutManager(layoutManager);
                CategoryAdapter categoryAdapter = new CategoryAdapter(products, MainActivity.this::onCategoryClick);
                binding.recyclerViewCategory.setAdapter(categoryAdapter);
                categoryModels = products;
            }
        });

        initSearchView();
        initRecyclerViewMeal();

        binding.imgPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRequestPermission();
            }
        });
    }

    private void onMealObserver() {
        mealViewModel.mutableLiveData.observe(this, new Observer<RecipeSearchResponse>() {
            @Override
            public void onChanged(RecipeSearchResponse recipeSearchResponse) {
                mealAdapter.setData(recipeSearchResponse.getRecipes(), MainActivity.this, MainActivity.this);
            }
        });
    }

    private void initRecyclerViewMeal() {
        searchRecipesApi(null);
        mealAdapter = new MealAdapter();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.recyclerViewMeal.setLayoutManager(layoutManager);
        binding.recyclerViewMeal.setAdapter(mealAdapter);
        onMealObserver();
    }

    private void searchRecipesApi(String query) {
        binding.recyclerViewMeal.smoothScrollToPosition(0);
        mealViewModel.getSearchRecipe(query);
        binding.searchView.clearFocus();
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryModels.get(position).getCategoryName();
        mealViewModel.getSearchRecipe(category);
    }

    // on click the meal item send data to recipe Activity
    @Override
    public void onMealClick(int position) {
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        intent.putExtra("Recipe", mealAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    private void initSearchView() {
        binding.searchView.setIconifiedByDefault(false);
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchRecipesApi(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void clickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permissions, REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
package com.example.foodrecipeapp.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.foodrecipeapp.databinding.ActivityRecipeBinding;
import com.example.foodrecipeapp.model.RecipesModel;
import com.example.foodrecipeapp.response.RecipeResponse;
import com.example.foodrecipeapp.viewmodel.MealViewModel;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RecipeActivity extends AppCompatActivity {
    private ActivityRecipeBinding binding;
    private String recipeID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra("Recipe")) {
            RecipesModel recipesModel = getIntent().getParcelableExtra("Recipe");
            assert recipesModel != null;
            recipeID = recipesModel.getRecipe_id();
            recipeObserver();
        }
    }

    private void recipeObserver() {
        MealViewModel mealViewModel = ViewModelProviders.of(this).get(MealViewModel.class);
        mealViewModel.getRecipe(recipeID);
        mealViewModel.recipesModelMutableLiveData.observe(this, new Observer<RecipeResponse>() {
            @Override
            public void onChanged(RecipeResponse recipeResponse) {
                setRecipeProperties(recipeResponse);
            }
        });

    }

    private void setRecipeProperties(RecipeResponse recipeResponse) {
        if (recipeResponse != null) {

            new GetImage().execute("https://th.bing.com/th/id/OIP.vuilg-c2-qCl3xdfn8nR_QHaEK?pid=ImgDet&rs=1");
            binding.recipeImage.setClipToOutline(true);
            binding.recipeTitleTV.setText(recipeResponse.getRecipe().getTitle());
            binding.publisherTV.setText(recipeResponse.getRecipe().getPublisher());

            float rating = (float) (Math.round(recipeResponse.getRecipe().getSocial_rank())) / 20;
            binding.recipeRating.setRating(rating);
            getIngredients(recipeResponse);

        }
    }

    private void getIngredients(RecipeResponse recipeResponse) {
        if (recipeResponse.getRecipe().getIngredients() != null) {
            for (String ingredient : recipeResponse.getRecipe().getIngredients()) {
                TextView textView = new TextView(RecipeActivity.this);
                textView.setText(ingredient);
                textView.setTextSize(17);
                textView.setTextColor(Color.parseColor("#777777"));
                textView.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                binding.ingredientsContainer.addView(textView);
            }
        } else {
            TextView textView = new TextView(this);
            textView.setText("Error retrieving ingredients.\nCheck network connection.");
            textView.setTextSize(17);
            textView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            binding.ingredientsContainer.addView(textView);
        }

    }

    class GetImage extends AsyncTask<String, Void, byte[]> {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        @Override
        protected byte[] doInBackground(String... strings) {

            Request.Builder builder = new Request.Builder();
            builder.url(strings[0]);

            Request request = builder.build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().bytes();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            if (bytes.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                binding.recipeImage.setImageBitmap(bitmap);
            }
            super.onPostExecute(bytes);
        }
    }

}

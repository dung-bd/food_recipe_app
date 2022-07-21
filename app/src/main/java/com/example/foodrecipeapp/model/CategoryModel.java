package com.example.foodrecipeapp.model;

public class CategoryModel {
    private String categoryName;

    public CategoryModel(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName(){
         return categoryName;
     }
     private void setCategoryName(String categoryName){
         this.categoryName = categoryName;
     }

}

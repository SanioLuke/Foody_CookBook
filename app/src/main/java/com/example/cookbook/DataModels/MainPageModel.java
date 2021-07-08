package com.example.cookbook.DataModels;

import java.io.Serializable;

public class MainPageModel implements Serializable {

    int idMeal;
    String strMeal, strCategory, strArea, strMealThumb, strInstructions, strTags, strYoutube;

    public MainPageModel(int idMeal, String strMeal, String strCategory, String strArea, String strMealThumb, String strInstructions, String strTags, String strYoutube) {
        this.idMeal = idMeal;
        this.strMeal= strMeal;
        this.strCategory = strCategory;
        this.strArea = strArea;
        this.strMealThumb = strMealThumb;
        this.strInstructions= strInstructions;
        this.strTags= strTags;
        this.strYoutube= strYoutube;
    }

    public int getIdMeal() {
        return idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public String getStrArea() {
        return strArea;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public String getStrInstructions() {
        return strInstructions;
    }

    public String getStrTags() {
        return strTags;
    }

    public String getStrYoutube() {
        return strYoutube;
    }
}

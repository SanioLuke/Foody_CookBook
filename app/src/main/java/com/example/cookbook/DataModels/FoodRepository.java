package com.example.cookbook.DataModels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FoodRepository {

    private final Context context;
    private ArrayList<MainPageModel> dataSet = new ArrayList<>();

    public FoodRepository(Context context) {
        this.context = context;
    }

    public MutableLiveData<ArrayList<MainPageModel>> getFoodItems() {
        MutableLiveData<ArrayList<MainPageModel>> data = new MutableLiveData<>();
        getFoodsFromURL();
        getAllFoodsFromPrefs(context);
        data.setValue(dataSet);
        return data;
    }

    private void getFoodsFromURL() {
        ArrayList<MainPageModel> food_items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Values.RANDOM_MEAL, null,
                    response -> {

                        try {
                            for (int j = 0; j < response.getJSONArray("meals").length(); j++) {
                                String outer_val = response.getJSONArray("meals").getString(j);
                                JSONObject jsonObject = new JSONObject(outer_val);

                                MainPageModel mainPageModel = new MainPageModel(
                                        jsonObject.getInt("idMeal"),
                                        jsonObject.getString("strMeal"),
                                        jsonObject.getString("strCategory"),
                                        jsonObject.getString("strArea"),
                                        jsonObject.getString("strMealThumb"),
                                        jsonObject.getString("strInstructions"),
                                        jsonObject.getString("strTags"),
                                        jsonObject.getString("strYoutube"));
                                food_items.add(mainPageModel);

                                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("get_food_data", Context.MODE_PRIVATE).edit();
                                Gson gson = new Gson();
                                String jsonData = gson.toJson(food_items);
                                Log.e("json_file_string", "Size is : " + jsonData);
                                sharedPreferences.putString("json_data", jsonData);
                                sharedPreferences.apply();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Log.e("Volley_data", "Error on : " + error.getMessage()));

            queue.add(jsonObjectRequest);
        }
    }

    public void getAllFoodsFromPrefs(@NotNull Context context) {
        SharedPreferences preferences = context.getSharedPreferences("get_food_data", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json_data = preferences.getString("json_data", null);
        Log.e("json_file_string_prefs", "Size is : " + json_data);
        Type type = new TypeToken<ArrayList<MainPageModel>>() {
        }.getType();
        if (json_data != null) {
            dataSet = gson.fromJson(json_data, type);
        } else {
            dataSet = new Values().sample_food();
        }
    }

}

package com.example.cookbook.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cookbook.Adapters.MainPageAdapter;
import com.example.cookbook.DataModels.MainPageModel;
import com.example.cookbook.DataModels.Values;
import com.example.cookbook.LocalDB.FavDBHelper;
import com.example.cookbook.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavActivity extends AppCompatActivity {

    private ArrayList<Integer> get_all_from_fav = new ArrayList<>();
    private RecyclerView all_favfood_rec;
    private final ArrayList<MainPageModel> get_all_favfood_array = new ArrayList<>();
    private MainPageAdapter mainPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        favInits();  //FavActivity Initializations
        getAllFavItems();  //Get all the favourtite food items (Function)
    }

    private void favInits() {
        FavDBHelper favDBHelper = new FavDBHelper(getApplicationContext());
        all_favfood_rec = findViewById(R.id.all_favfood_rec);
        get_all_from_fav = favDBHelper.getAllFavFoods();
    }

    private void getAllFavItems() {

        for (int i = 0; i < get_all_from_fav.size(); i++) {
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Values.SEARCH_MEAL_DETAILS_BY_ID + get_all_from_fav.get(i), null,
                    response -> {

                        try {
                            String fav_outer_val = response.getJSONArray("meals").getString(0);
                            JSONObject favjsonObject = new JSONObject(fav_outer_val);
                            Log.e("Fav_example_item", "Fav Meal Name is : " + favjsonObject.getString("strMeal"));

                            MainPageModel mainPageModel = new MainPageModel(
                                    favjsonObject.getInt("idMeal"),
                                    favjsonObject.getString("strMeal"),
                                    favjsonObject.getString("strCategory"),
                                    favjsonObject.getString("strArea"),
                                    favjsonObject.getString("strMealThumb"));
                            get_all_favfood_array.add(mainPageModel);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        all_favfood_rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mainPageAdapter = new MainPageAdapter(getApplicationContext(), get_all_favfood_array, 0);
                        all_favfood_rec.setAdapter(mainPageAdapter);
                    },
                    error -> Log.e("FavItems", "Error is : " + error.getMessage()));

            queue.add(jsonObjectRequest);
        }

    }

}
package com.example.cookbook.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cookbook.Adapters.MainPageAdapter;
import com.example.cookbook.DataModels.MainPageModel;
import com.example.cookbook.DataModels.Values;
import com.example.cookbook.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView random_food_rec;
    private ProgressBar mainpage_prog;
    private FloatingActionButton mainpage_fav_btn;
    private MainPageAdapter mainPageAdapter;
    private ArrayList<MainPageModel> get_random_food;
    private ImageButton mainpage_search_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainInits(); // MainActivity Initializations
        getFoodsFromURL(); //MainActivity Event Listners
        mainEventListnerMethods(); //Get the Food from URL Function

    }

    private void mainInits() {
        random_food_rec = findViewById(R.id.random_food_rec);
        mainpage_prog = findViewById(R.id.mainpage_prog);
        mainpage_fav_btn = findViewById(R.id.mainpage_fav_btn);
        mainpage_search_btn = findViewById(R.id.mainpage_search_btn);
        get_random_food = new ArrayList<>();
        random_food_rec.setVisibility(View.GONE);
        mainpage_prog.setVisibility(View.VISIBLE);
    }

    private void mainEventListnerMethods() {

        mainpage_search_btn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SearchActivity.class)));

        mainpage_fav_btn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), FavActivity.class)));

        random_food_rec.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                mainpage_fav_btn.hide();
            } else {
                mainpage_fav_btn.show();
            }
        });
    }

    private void getFoodsFromURL() {

        for (int i = 0; i < 10; i++) {
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Values.RANDOM_MEAL, null,
                    response -> {

                        try {
                            String outer_val = response.getJSONArray("meals").getString(0);
                            JSONObject jsonObject = new JSONObject(outer_val);
                            Log.e("example_item", "Meal Name is : " + jsonObject.getString("strMeal"));

                            MainPageModel mainPageModel = new MainPageModel(
                                    jsonObject.getInt("idMeal"),
                                    jsonObject.getString("strMeal"),
                                    jsonObject.getString("strCategory"),
                                    jsonObject.getString("strArea"),
                                    jsonObject.getString("strMealThumb"));
                            get_random_food.add(mainPageModel);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        random_food_rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mainPageAdapter = new MainPageAdapter(getApplicationContext(), get_random_food, 0);
                        random_food_rec.setAdapter(mainPageAdapter);
                    },
                    error -> Log.e("Volley_data", "Error on : " + error.getMessage()));

            queue.add(jsonObjectRequest);
        }

        random_food_rec.setVisibility(View.VISIBLE);
        mainpage_prog.setVisibility(View.GONE);
    }


}
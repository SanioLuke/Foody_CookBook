package com.example.cookbook.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.cookbook.DataModels.Values;
import com.example.cookbook.LocalDB.FavDBHelper;
import com.example.cookbook.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class FoodDetailsActivity extends AppCompatActivity {

    private int meal_id;
    private TextView this_meal_name, this_meal_instructions, this_meal_category, this_meal_area, this_meal_tags;
    private ImageView this_meal_thumbimg;
    private FloatingActionButton addfav_this_food;
    private FavDBHelper favDBHelper;
    private ImageButton this_meal_close;
    private NestedScrollView this_food_nestscroll;
    private View this_food_video_lay, this_meal_tags_lay;
    private YouTubePlayerView youtube_player_view;
    private ProgressBar this_food_prog;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        foodDetailsInits(); //Food Details Activity Initializations
        foodDetailsEventListnerMethods(); //FoodDetails Activity Event Listners
        getThisFoodByID(meal_id);  //Get a specific food products (Function)

    }

    private void foodDetailsEventListnerMethods() {

        this_food_nestscroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                addfav_this_food.hide();
            } else {
                addfav_this_food.show();
            }

        });

        addfav_this_food.setOnClickListener(v -> {
            if (favDBHelper.checkSpecficFood(meal_id) > 0) {
                if (favDBHelper.removeFavFood(meal_id)) {
                    Log.e("Fav_action", "Item removed");
                    addfav_this_food.setImageTintList(ColorStateList.valueOf(Color.parseColor("#D8D8D8")));
                }
            } else {
                if (favDBHelper.addFavFood(meal_id)) {
                    Log.e("Fav_action", "Item added");
                    addfav_this_food.setImageTintList(ColorStateList.valueOf(Color.RED));
                }
            }
        });

        this_meal_close.setOnClickListener(v -> finish());

    }

    private void foodDetailsInits() {
        this_meal_name = findViewById(R.id.this_meal_name);
        this_meal_instructions = findViewById(R.id.this_meal_instructions);
        this_meal_category = findViewById(R.id.this_meal_category);
        this_meal_area = findViewById(R.id.this_meal_area);
        this_meal_tags = findViewById(R.id.this_meal_tags);
        this_meal_thumbimg = findViewById(R.id.this_meal_thumbimg);
        addfav_this_food = findViewById(R.id.addfav_this_food);
        favDBHelper = new FavDBHelper(getApplicationContext());
        this_food_nestscroll = findViewById(R.id.this_food_nestscroll);
        youtube_player_view = findViewById(R.id.youtube_player_view);
        this_food_video_lay = findViewById(R.id.this_food_video_lay);
        this_meal_tags_lay = findViewById(R.id.this_meal_tags_lay);
        this_food_prog = findViewById(R.id.this_food_prog);
        this_meal_close = findViewById(R.id.this_meal_close);
        this_food_prog.setVisibility(View.VISIBLE);
        this_food_nestscroll.setVisibility(View.GONE);
        Intent intent = getIntent();
        meal_id = intent.getIntExtra("meal_id", 0);
        if (favDBHelper.checkSpecficFood(meal_id) > 0) {
            addfav_this_food.setImageTintList(ColorStateList.valueOf(Color.RED));
        } else {
            addfav_this_food.setImageTintList(ColorStateList.valueOf(Color.parseColor("#B1B1B1")));
        }
    }

    private void getThisFoodByID(int meal_id) {

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Values.SEARCH_MEAL_DETAILS_BY_ID + meal_id, null,
                response -> {

                    try {
                        this_food_prog.setVisibility(View.GONE);
                        this_food_nestscroll.setVisibility(View.VISIBLE);

                        String outer_val = response.getJSONArray("meals").getString(0);
                        JSONObject jsonObject = new JSONObject(outer_val);
                        Log.e("example_item", "Meal Name is : " + jsonObject.getString("strMeal"));

                        this_meal_name.setText(jsonObject.getString("strMeal"));
                        this_meal_instructions.setText(jsonObject.getString("strInstructions"));
                        this_meal_category.setText(jsonObject.getString("strCategory"));
                        this_meal_area.setText(jsonObject.getString("strArea"));

                        String tags = jsonObject.getString("strTags");
                        if (!tags.isEmpty() && !tags.equals("null")) {
                            this_meal_tags_lay.setVisibility(View.VISIBLE);
                            this_meal_tags.setText(tags);
                        } else {
                            this_meal_tags_lay.setVisibility(View.GONE);
                        }

                        Glide.with(getApplicationContext()).load(jsonObject.getString("strMealThumb")).into(this_meal_thumbimg);
                        String videourl = jsonObject.getString("strYoutube");


                        if (!videourl.equals("") && videourl.length() > 0) {
                            this_food_video_lay.setVisibility(View.VISIBLE);

                        } else {
                            this_food_video_lay.setVisibility(View.GONE);
                        }

                        youtube_player_view.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(@NotNull YouTubePlayer youTubePlayer) {

                                if (!videourl.equals("") && videourl.length() > 0) {
                                    String video_id = videourl.substring(32);
                                    Log.e("videoURL_ID", video_id);
                                    youTubePlayer.loadVideo(video_id, 0); //S0Q4gqBUs7c
                                    youTubePlayer.pause();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("Volley_data", "Error on : " + error.getMessage()));

        queue.add(jsonObjectRequest);
    }

}
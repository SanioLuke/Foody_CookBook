package com.example.cookbook.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.example.cookbook.DataModels.MainPageModel;
import com.example.cookbook.LocalDB.FavDBHelper;
import com.example.cookbook.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

public class FoodDetailsActivity extends AppCompatActivity {

    private MainPageModel each_meal;
    private TextView this_meal_name, this_meal_instructions, this_meal_category, this_meal_area, this_meal_tags;
    private ImageView this_meal_thumbimg;
    private FloatingActionButton addfav_this_food;
    private FavDBHelper favDBHelper;
    private ImageButton this_meal_close;
    private NestedScrollView this_food_nestscroll;
    private View this_food_video_lay, this_meal_tags_lay, this_meal_instructions_lay, this_meal_category_lay, this_meal_area_lay;
    private YouTubePlayerView youtube_player_view;
    private ProgressBar this_food_prog;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        foodDetailsInits(); //Food Details Activity Initializations
        setDetailsToPage();
        foodDetailsEventListnerMethods(); //FoodDetails Activity Event Listners

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
            if (favDBHelper.checkSpecficFood(each_meal.getIdMeal()) > 0) {
                if (favDBHelper.removeFavFood(each_meal.getIdMeal())) {
                    Log.e("Fav_action", "Item removed");
                    addfav_this_food.setImageTintList(ColorStateList.valueOf(Color.parseColor("#D8D8D8")));
                }
            } else {
                if (favDBHelper.addFavFood(each_meal.getIdMeal(), each_meal.getStrMeal(), each_meal.getStrCategory(), each_meal.getStrArea(),
                        each_meal.getStrMealThumb(), each_meal.getStrInstructions(), each_meal.getStrTags(), each_meal.getStrYoutube())) {
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
        this_meal_instructions_lay= findViewById(R.id.this_meal_instructions_lay);
        this_meal_category_lay= findViewById(R.id.this_meal_category_lay);
        this_meal_area_lay= findViewById(R.id.this_meal_area_lay);
        this_food_video_lay = findViewById(R.id.this_food_video_lay);
        this_meal_tags_lay = findViewById(R.id.this_meal_tags_lay);
        this_food_prog = findViewById(R.id.this_food_prog);
        this_meal_close = findViewById(R.id.this_meal_close);
        this_food_prog.setVisibility(View.VISIBLE);
        this_food_nestscroll.setVisibility(View.GONE);
    }

    private void setDetailsToPage() {
        Intent intent = getIntent();
        each_meal = (MainPageModel) intent.getSerializableExtra("each_meal");
        if (each_meal != null) {

            if (favDBHelper.checkSpecficFood(each_meal.getIdMeal()) > 0) {
                addfav_this_food.setImageTintList(ColorStateList.valueOf(Color.RED));
            } else {
                addfav_this_food.setImageTintList(ColorStateList.valueOf(Color.parseColor("#B1B1B1")));
            }

            this_food_prog.setVisibility(View.GONE);
            this_food_nestscroll.setVisibility(View.VISIBLE);

            if(each_meal.getStrMeal()!=null && !each_meal.getStrMeal().equals("null"))
                this_meal_name.setText(each_meal.getStrMeal());
            else
                this_meal_name.setText("Food Name");

            if(each_meal.getStrInstructions()!=null && !each_meal.getStrInstructions().equals("null")){
                this_meal_instructions_lay.setVisibility(View.VISIBLE);
                this_meal_instructions.setText(each_meal.getStrInstructions());
            }
            else this_meal_instructions_lay.setVisibility(View.GONE);

            if(each_meal.getStrCategory()!=null && !each_meal.getStrCategory().equals("null")){
                this_meal_category_lay.setVisibility(View.VISIBLE);
                this_meal_category.setText(each_meal.getStrCategory());
            }
            else
                this_meal_category_lay.setVisibility(View.GONE);

            if(each_meal.getStrArea()!=null && !each_meal.getStrArea().equals("null")){
                this_meal_area_lay.setVisibility(View.VISIBLE);
                this_meal_area.setText(each_meal.getStrArea());
            }
            else
                this_meal_area_lay.setVisibility(View.GONE);

            Glide.with(getApplicationContext()).load(each_meal.getStrMealThumb()).into(this_meal_thumbimg);

            String tags = each_meal.getStrTags();
            if (tags != null && !tags.isEmpty() && !tags.equals("null")) {
                this_meal_tags_lay.setVisibility(View.VISIBLE);
                this_meal_tags.setText(tags);
            } else {
                this_meal_tags_lay.setVisibility(View.GONE);
            }

            String videourl = each_meal.getStrYoutube();
            if (videourl != null && !videourl.equals("") && videourl.length() > 0) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork == null || !activeNetwork.isConnected() || !activeNetwork.isAvailable()) {
                    this_food_video_lay.setVisibility(View.GONE);
                } else {
                    this_food_video_lay.setVisibility(View.VISIBLE);
                    youtube_player_view.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                            String video_id = videourl.substring(32);
                            Log.e("videoURL_ID", video_id);
                            youTubePlayer.loadVideo(video_id, 0); //S0Q4gqBUs7c
                            youTubePlayer.cueVideo(video_id, 0);
                        }
                    });
                }
            } else {
                this_food_video_lay.setVisibility(View.GONE);
            }

        }
    }

}
package com.example.cookbook.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import com.example.cookbook.Adapters.MainPageAdapter;
import com.example.cookbook.DataModels.FoodViewModel;
import com.example.cookbook.DataModels.MainPageModel;
import com.example.cookbook.DataModels.Values;
import com.example.cookbook.LocalDB.FavDBHelper;
import com.example.cookbook.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView random_food_rec;
    private ProgressBar mainpage_prog;
    private FloatingActionButton mainpage_fav_btn;
    private MainPageAdapter mainPageAdapter;
    private ImageButton mainpage_search_btn;
    private FoodViewModel foodViewModel;
    private Values values= new Values();
    private FavDBHelper favDBHelper;
    private View main_page_lay;
    private long backPressedTime;

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
        main_page_lay= findViewById(R.id.main_page_lay);
        random_food_rec.setVisibility(View.GONE);
        mainpage_prog.setVisibility(View.VISIBLE);
        foodViewModel= ViewModelProviders.of(this).get(FoodViewModel.class);
        favDBHelper= new FavDBHelper(getApplicationContext());
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

        ArrayList<MainPageModel> resultdata= new ArrayList<>();
        random_food_rec.setVisibility(View.VISIBLE);
        mainpage_prog.setVisibility(View.GONE);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork==null || !activeNetwork.isConnected() || !activeNetwork.isAvailable()){
            if(favDBHelper.getAllFavFoods().size() > 0)
                resultdata= favDBHelper.getAllFavFoods();
            resultdata.addAll(values.sample_food());
        }
        else {
            foodViewModel.ViewModelInit(getApplicationContext());
            foodViewModel.getAllFoodItems().observe(this, mainPageModels -> mainPageAdapter.notifyDataSetChanged());
            resultdata= foodViewModel.getAllFoodItems().getValue();
        }

        random_food_rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mainPageAdapter = new MainPageAdapter(getApplicationContext(), resultdata, 0);
        random_food_rec.setAdapter(mainPageAdapter);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
        } else {
            Snackbar.make(main_page_lay, "Press back again to Exit", Snackbar.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
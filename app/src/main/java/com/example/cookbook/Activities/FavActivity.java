package com.example.cookbook.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.Adapters.MainPageAdapter;
import com.example.cookbook.DataModels.MainPageModel;
import com.example.cookbook.LocalDB.FavDBHelper;
import com.example.cookbook.R;

import java.util.ArrayList;

public class FavActivity extends AppCompatActivity {

    RecyclerView all_favfood_rec;
    ArrayList<MainPageModel> get_all_favfood_array = new ArrayList<>();
    MainPageAdapter mainPageAdapter;
    TextView all_favfood_no_data;
    ImageButton all_favfood_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        FavDBHelper favDBHelper = new FavDBHelper(getApplicationContext());
        all_favfood_rec = findViewById(R.id.all_favfood_rec);
        all_favfood_no_data = findViewById(R.id.all_favfood_no_data);
        all_favfood_back_btn = findViewById(R.id.all_favfood_back_btn);
        get_all_favfood_array = favDBHelper.getAllFavFoods();

        all_favfood_back_btn.setOnClickListener(v -> finish());

        if (get_all_favfood_array != null && get_all_favfood_array.size() > 0) {

            all_favfood_no_data.setVisibility(View.GONE);
            all_favfood_rec.setVisibility(View.VISIBLE);

            all_favfood_rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mainPageAdapter = new MainPageAdapter(getApplicationContext(), get_all_favfood_array, 0);
            all_favfood_rec.setAdapter(mainPageAdapter);
        } else {
            all_favfood_no_data.setVisibility(View.VISIBLE);
            all_favfood_rec.setVisibility(View.GONE);
        }

    }
}
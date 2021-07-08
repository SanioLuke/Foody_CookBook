package com.example.cookbook.DataModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class FoodViewModel extends ViewModel {

    private MutableLiveData<ArrayList<MainPageModel>> mFoods;

    public void ViewModelInit(Context context) {
        if (mFoods != null) {
            return;
        }
        FoodRepository foodRepository = new FoodRepository(context);
        mFoods = foodRepository.getFoodItems();
    }

    public LiveData<ArrayList<MainPageModel>> getAllFoodItems() {
        return mFoods;
    }
}

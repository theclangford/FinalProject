package group.finalproject.food;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import group.finalproject.R;

public class FoodFavourites extends AppCompatActivity {

    protected final String ACTIVITY_NAME = "FoodFavouritesActivity";

    protected ArrayList<Food> favorites;
    protected FoodItemAdapter favoritesAdapter;
    FoodDatabaseHelper foodDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_favourites);
        Log.i(ACTIVITY_NAME, "In onCreate");

        ListView favResultsView = (ListView) findViewById(R.id.foodFav);

        foodDatabase = FoodDatabaseHelper.getInstance(this);
        favorites = foodDatabase.getAllFoods();
        favoritesAdapter = new FoodItemAdapter(FoodFavourites.this);
        favoritesAdapter.setList(favorites);
        favResultsView.setAdapter(favoritesAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

}

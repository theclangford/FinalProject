package group.finalproject.food;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import group.finalproject.R;

public class FoodFavourites extends AppCompatActivity {

    /**
     * Activity name
     */
    protected final String ACTIVITY_NAME = "FoodFavouritesActivity";

    /**
     * Stores list of favorite food items
     */
    protected ArrayList<Food> favorites;

    /**
     * Stores list of tag names
     */
    protected ArrayList<Tag> tags;

    /**
     * Adapter for list of food items
     */
    protected FoodItemAdapter favoritesAdapter;

    /**
     * Adapter for tag summaries
     */
    protected TagSummaryAdapter tagAdapter;

    /**
     * Instance of FoodDatabaseHelper
     */
    FoodDatabaseHelper foodDatabase;

    /**
     * Dialog that shows when a favorite item is clicked
     */
    AlertDialog favItemDialog;

    /**
     * Initialize views and set list adapters.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_favourites);
        Log.i(ACTIVITY_NAME, "In onCreate");

        ListView tagsView = findViewById(R.id.tagSummary);
        ListView favResultsView = findViewById(R.id.foodFav);

        foodDatabase = FoodDatabaseHelper.getInstance(this);
        tags = foodDatabase.getTags();
        favorites = foodDatabase.getAllFoods();
        tagAdapter = new TagSummaryAdapter(FoodFavourites.this);
        tagAdapter.setList(tags);
        favoritesAdapter = new FavFoodItemAdapter(FoodFavourites.this);
        favoritesAdapter.setList(favorites);
        tagsView.setAdapter(tagAdapter);
        favResultsView.setAdapter(favoritesAdapter);

        favResultsView.setOnItemClickListener((parent, view, position, id) -> {
            Food currentItem = favorites.get(position);
            openOptionsDialog(currentItem);
        });

    }

    /**
     * Create options dialog for favorite items.
     * Code adapted from https://medium.com/viithiisys/android-custom-dialog-box-fce3a039c695
     * @param item
     */
    protected void openOptionsDialog(Food item) {
        favItemDialog = new AlertDialog.Builder(this).create();

        // Set Custom Title
        TextView title = new TextView(this);
        // Title Properties
        title.setText("Options for " + item.getName());
        title.setPadding(10, 10, 10, 10);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        favItemDialog.setCustomTitle(title);

        // Set View
        LayoutInflater inflater = getLayoutInflater();
        View favoriteOptions = inflater.inflate(R.layout.food_favorites_options, null);
        final EditText tagField = favoriteOptions.findViewById(R.id.editTag);
        favItemDialog.setView(favoriteOptions);

        // Set Button
        favItemDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Update item tag attribute in database and then refresh view
                foodDatabase.updateFoodTag(item.getId(), tagField.getText().toString());
                favorites.clear();
                favorites.addAll(foodDatabase.getAllFoods());
                favoritesAdapter.notifyDataSetChanged();
                tags.clear();
                tags.addAll(foodDatabase.getTags());
                tagAdapter.notifyDataSetChanged();
            }
        });

        favItemDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Delete item", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete item from database and then refresh view
                foodDatabase.deleteFood(item.getId());
                favorites.clear();
                favorites.addAll(foodDatabase.getAllFoods());
                favoritesAdapter.notifyDataSetChanged();
                tags.clear();
                tags.addAll(foodDatabase.getTags());
                tagAdapter.notifyDataSetChanged();
            }
        });

        favItemDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
            }
        });

        new Dialog(getApplicationContext());
        favItemDialog.show();

        final Button addBT = favItemDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams posBtnLP = (LinearLayout.LayoutParams) addBT.getLayoutParams();
        posBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        addBT.setPadding(50, 10, 10, 10);   // Set Position
        addBT.setTextColor(Color.GRAY);
        addBT.setLayoutParams(posBtnLP);

        final Button deleteBT = favItemDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) addBT.getLayoutParams();
        posBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        deleteBT.setTextColor(Color.GRAY);
        deleteBT.setLayoutParams(neutralBtnLP);

        final Button cancelBT = favItemDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) addBT.getLayoutParams();
        negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        cancelBT.setTextColor(Color.GRAY);
        cancelBT.setLayoutParams(negBtnLP);
    }

    /**
     * Executes when activity is resumed
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    /**
     * Executes when activity is started
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    /**
     * Executes when activity is paused
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    /**
     * Executes when activity is stopped
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    /**
     * Executes when activity is destroyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

}

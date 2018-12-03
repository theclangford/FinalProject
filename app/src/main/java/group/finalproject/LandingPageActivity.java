package group.finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.content.Intent;

import group.finalproject.food.FoodActivity;
import group.finalproject.food.FoodFavourites;


public class LandingPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        Toolbar toolbar = findViewById(R.id.landingToolbar);
        setSupportActionBar(toolbar);

        Button btn1 = findViewById(R.id.btnOne);
        Button btn2 = findViewById(R.id.btnTwo);
        Button btn3 = findViewById(R.id.btnThree);
        Button btn4 = findViewById(R.id.btnFour);

        btn1.setOnClickListener(e -> {
            Intent nextScreen = new Intent(LandingPageActivity.this, FoodActivity.class);
            startActivityForResult(nextScreen, 555);
        });


        btn2.setOnClickListener(e -> {
            Intent nextScreen = new Intent(LandingPageActivity.this, CBCActivity.class);
            startActivityForResult(nextScreen, 444);
        });

        btn3.setOnClickListener(e -> {
            Intent nextScreen = new Intent(LandingPageActivity.this, MovieActivity.class);
            startActivityForResult(nextScreen, 333);
        });

        btn4.setOnClickListener(e -> {
            Intent nextScreen = new Intent(LandingPageActivity.this, BusActivity.class);
            startActivityForResult(nextScreen, 222);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.landing_page, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.foodMenuItem:
                Intent foodIntent = new Intent(LandingPageActivity.this, FoodActivity.class);
                startActivityForResult(foodIntent, 555);
                break;
            case R.id.movieMenuItem:
                Intent movieIntent = new Intent(LandingPageActivity.this, MovieActivity.class);
                startActivityForResult(movieIntent, 333);
                break;
            case R.id.busMenuItem:
                Intent busIntent = new Intent(LandingPageActivity.this, BusActivity.class);
                startActivityForResult(busIntent, 222);
                break;
            case R.id.cbcMenuItem:
                Intent cbcIntent = new Intent(LandingPageActivity.this, CBCActivity.class);
                startActivityForResult(cbcIntent, 444);
                break;
        }
        return true;
    }
}

package group.finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;


public class LandingPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        Button btn1 = (Button) findViewById(R.id.btnOne);
        Button btn2 = (Button) findViewById(R.id.btnTwo);
        Button btn3 = (Button) findViewById(R.id.btnThree);
        Button btn4 = (Button) findViewById(R.id.btnFour);

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
}

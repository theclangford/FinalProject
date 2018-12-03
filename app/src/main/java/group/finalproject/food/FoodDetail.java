package group.finalproject.food;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import group.finalproject.R;

public class FoodDetail extends AppCompatActivity {

    /**
     * Create food detail fragment and commit transaction upon creating activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        // Get passed details
        Bundle itemInfo = getIntent().getExtras();

        // Load fragment
        FoodDetailFragment fragment = new FoodDetailFragment();
        fragment.setArguments(itemInfo);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ftrans = fm.beginTransaction();
        ftrans.replace(R.id.itemDetailPhone, fragment);
        ftrans.commit();
    }

}

package group.finalproject;

//example query
//https://api.edamam.com/api/food-database/parser?ingr=red%20apple&app_id=1b640846&app_key=a617689707e1a1f8da7793816768f37e

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class FoodActivity extends AppCompatActivity {
    private final String ACTIVITY_NAME = "FoodActivity";
    private final double ACTIVITY_VERSION = 1.0;
    private final String URL_STRING = "https://api.edamam.com/api/food-database/parser?ingr=";
    private final String API_KEY = "&app_id=1b640846&app_key=a617689707e1a1f8da7793816768f37e";
    private float dpi;

    protected ListView foodResultsView;
    protected ArrayList<Food> foodResults;
    protected AlertDialog filtersDialog;
    protected AlertDialog helpDialog;
    protected android.support.v7.widget.Toolbar toolbar;
    protected ProgressBar searchProgress;
    protected FoodItemAdapter foodItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        dpi = this.getResources().getDisplayMetrics().density;

        // Get search progressbar
        searchProgress = (ProgressBar) findViewById(R.id.searchProgress);
        searchProgress.setVisibility(View.INVISIBLE);

        // Get toolbar
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.foodSearch);

        // Get listview
        foodResultsView = (ListView) findViewById(R.id.resultsList);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            FoodQuery foodQuery = new FoodQuery(query);
            foodQuery.execute();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu items for use
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.food_main_actions, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }

    // Create custom search url
    private URL createQueryURL(String query) throws UnsupportedEncodingException {
        URL url = null;
        try {
            url = new URL(URL_STRING + URLEncoder.encode(query, "UTF-8") + API_KEY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.filters:
                openFilterDialog();
                break;
            case R.id.help:
                openHelpDialog();
                break;
        }
        return true;
    }

    private class FoodItemAdapter extends ArrayAdapter<Food> {
        FoodItemAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            return foodResults.size();
        }

        @Override
        public Food getItem(int position) {
            return foodResults.get(position);
        }

//        @Override
//        public long getItemId(int position) {
//            return super.getItemId(position);
//        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = FoodActivity.this.getLayoutInflater();
            View result = null;
            result = inflater.inflate(R.layout.food_item, null);

            TextView name = (TextView) result.findViewById(R.id.itemName);
            TextView brand = (TextView) result.findViewById(R.id.itemBrand);

            name.setText(getItem(position).getName());
            brand.setText(getItem(position).getBrand());

            return result;
        }
    }

    private class FoodQuery extends AsyncTask<String, Integer, ArrayList<Food>> {

        private String query;

        FoodQuery(String query) {
            this.query = query;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            searchProgress.setProgress(values[0]);
        }

        // Returns list of relevant food items and information from API as Food objects
        protected ArrayList<Food> doInBackground(String... strings) {

            searchProgress.setVisibility(View.VISIBLE);

            foodResults = new ArrayList<>();

            try {
                // Connect to custom API query
                URL url = createQueryURL(query);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                publishProgress(20);

                // Read result and store in string
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                    sb.append(line + "\n");
                String result = sb.toString();
                publishProgress(40);

                // Store string in JSON
                JSONObject jsonObject = new JSONObject(result);
                JSONArray foods = jsonObject.getJSONArray("hints");
                System.out.println(foods.length());
                publishProgress(60);

                // Create Food objects to store in array
                for (int i = 0; i < foods.length(); i++) {
                    JSONObject currentItem = foods.getJSONObject(i).getJSONObject("food");
                    String name = "";
                    double calories = 0;
                    double fats = 0;
                    String brand = "";

                    if (currentItem.has("label"))
                        name = currentItem.getString("label");

                    if (currentItem.getJSONObject("nutrients").has("ENERC_KCAL"))
                        calories = currentItem.getJSONObject("nutrients").getDouble("ENERC_KCAL");

                    if (currentItem.getJSONObject("nutrients").has("FAT"))
                        fats = currentItem.getJSONObject("nutrients").getDouble("FAT");

                    if (currentItem.has("brand"))
                        brand = currentItem.getString("brand");

                    if (currentItem.getString("category").equals("Generic foods")) {
                        foodResults.add(new Food(name, calories, fats));
                    } else if (currentItem.getString("category").equals("Packaged foods")) {
                        foodResults.add(new Food(name, calories, fats, brand));
                    }
                    publishProgress(100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return foodResults;
        }

        @Override
        protected void onPostExecute(ArrayList<Food> foods) {
            super.onPostExecute(foods);
            foodItemAdapter = new FoodItemAdapter(FoodActivity.this);
            foodResultsView.setAdapter(foodItemAdapter);
            searchProgress.setVisibility(View.GONE);

            Toast toast = Toast.makeText(FoodActivity.this, "Items found: " + foodItemAdapter.getCount(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // Code adapted from https://medium.com/viithiisys/android-custom-dialog-box-fce3a039c695
    protected void openHelpDialog() {
        helpDialog = new AlertDialog.Builder(this).create();

        // Set Custom Title
        TextView title = new TextView(this);
        // Title Properties
        title.setText("Application information");
        title.setPadding(10, 10, 10, 10);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        helpDialog.setCustomTitle(title);

        // Set View
        LayoutInflater inflater = getLayoutInflater();
        View helpFood = inflater.inflate(R.layout.food_help, null);
        final TextView authorInfo = helpFood.findViewById(R.id.authorInfo);
        TextView versionInfo = helpFood.findViewById(R.id.versionInfo);
        versionInfo.setText("Activity version: " + ACTIVITY_VERSION);
        final TextView instructions = helpFood.findViewById(R.id.instructionsFood);
        helpDialog.setView(helpFood, (int)(15*dpi), (int)(15*dpi), (int)(15*dpi), (int)(15*dpi));

        new Dialog(getApplicationContext());
        helpDialog.show();
    }

    // Code adapted from https://medium.com/viithiisys/android-custom-dialog-box-fce3a039c695
    protected void openFilterDialog() {
        filtersDialog = new AlertDialog.Builder(this).create();

        // Set Custom Title
        TextView title = new TextView(this);
        // Title Properties
        title.setText("Filter nutrition");
        title.setPadding(10, 10, 10, 10);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        filtersDialog.setCustomTitle(title);

        // Set View
        LayoutInflater inflater = getLayoutInflater();
        View filterFood = inflater.inflate(R.layout.food_filters, null);
        final EditText calMin = filterFood.findViewById(R.id.calMin);
        final EditText calMax = filterFood.findViewById(R.id.calMax);
        final EditText fatMin = filterFood.findViewById(R.id.fatMin);
        final EditText fatMax = filterFood.findViewById(R.id.fatMax);
        filtersDialog.setView(filterFood);

        // Set Button
        filtersDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
            }
        });

        filtersDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
            }
        });

        new Dialog(getApplicationContext());
        filtersDialog.show();

        // Set Properties for OK Button
        final Button okBT = filtersDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        okBT.setPadding(50, 10, 10, 10);   // Set Position
        okBT.setTextColor(Color.GRAY);
        okBT.setLayoutParams(neutralBtnLP);

        final Button cancelBT = filtersDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        cancelBT.setTextColor(Color.GRAY);
        cancelBT.setLayoutParams(negBtnLP);
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

package group.finalproject.movie;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import group.finalproject.R;

public class MovieActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String encodedMovieName;
    FrameLayout progressBarHolder;
    Button buttonSearch, buttonlistMovies;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    private DrawerLayout drawer;
    JSONObject jo = null;

    Spinner spinner;
    Locale myLocale;
    String currentLanguage = "en", currentLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        //create a toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.draw_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drower_open, R.string.navigation_drower_Close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //create a progressbar
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        EditText movieName = (EditText) findViewById(R.id.movieName);

        //clear the fragmets when edit text get focused
        movieName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (getSupportFragmentManager().getFragments().size() > 0) {
                        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        Intent intent = new Intent(MovieActivity.this, MovieActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            }
        });


        //search for the movie from the title enttered into text bar
        buttonSearch = (Button) findViewById(R.id.buttonsearch);

        buttonSearch.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.buttonsearch:
                        new ProgressBarTask().execute();
                        break;
                }

                try {
                    encodedMovieName = URLEncoder.encode(movieName.getText().toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String url = String.format("http://www.omdbapi.com/?apikey=eba776bf&r=xml&t=%s", encodedMovieName);


                try {
                    GetData getData = new GetData(new URL(url));
                    getData.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buttonlistMovies = (Button) findViewById(R.id.buttonlistMovies);

        buttonlistMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieActivity.this, ListMovieActivity.class);
                startActivity(intent);
            }
        });

        //search action trigger after hitting enter from text view
        movieName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    buttonSearch.performClick();
                }
                return false;
            }
        });


        //language support
        currentLanguage = getIntent().getStringExtra(currentLang);
        //spinner to select the language
        spinner = (Spinner) findViewById(R.id.spinner);

        List<String> list = new ArrayList<String>();

        list.add("Select language");
        list.add("English");
        list.add("Français");

        //populate the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //get the language selected from the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        setLocale("en");
                        break;
                    case 2:
                        setLocale("fr");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.setTitle(R.string.app_name);
    }

    //set the local language to what the user selected from the dropdown
    public void  setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, MovieActivity.class);
            refresh.putExtra(currentLang, localeName);
            startActivity(refresh);
        } else {
            Toast.makeText(MovieActivity.this, "Language already selected!", Toast.LENGTH_SHORT).show();
        }
    }

    //action navigation bar options select
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_help:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HelpFragment()).commit();
                break;
            case R.id.nav_about:
                View view = findViewById(R.id.draw_layout);
                String message = "Ranjika Perera \n Version 1";
                int duration = Snackbar.LENGTH_INDEFINITE;

                final Snackbar snackbar = Snackbar.make(view, message, duration);
                snackbar.setAction(getString(R.string.gotit), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //press back key
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getFragments().size() > 0) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Intent intent = new Intent(MovieActivity.this, MovieActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }

    private class GetData extends AsyncTask<Void, Void, String> {

        URL url;
        HttpURLConnection urlConnection;
        private String response_string;

        public String getResponse_string() {
            return response_string;
        }

        public void setResponse_string(String response_string) {
            this.response_string = response_string;
        }

        public GetData(URL url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... args) {

            StringBuilder result = new StringBuilder();

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            }catch( Exception e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }


            return result.toString();
        }

        @Override
        protected void onPostExecute(String _response) {
            super.onPostExecute(_response);
            if(_response!=null){
                setResponse_string(_response);

                JSONObject jsonObj = null;
                try {
                    jsonObj = XML.toJSONObject(_response);

                    JSONObject jsonObject = new JSONObject(jsonObj.toString());
                    JSONObject jsonChildObject = (JSONObject) jsonObject.get("root");
                    Iterator iterator = jsonChildObject.keys();
                    String key = null;
                    while (iterator.hasNext()) {
                        key = (String) iterator.next();
                        jo = (JSONObject) jsonChildObject.get("movie");
                    }

                    JSONArray jsonArray = new JSONArray();
                    String[] key_array = {"title", "year", "runtime", "actors", "plot", "poster"};
                    Iterator x = jo.keys();
                    while (x.hasNext()) {
                        key = (String) x.next();
                        for (String k : key_array) {
                            if (key.equals(k))
                                jsonArray.put(jo.get(key));
                        }

                    }

                    int length = jsonArray.length();
                    List<String> listContents = new ArrayList<String>(length);
                    listContents.add(jsonArray.getString(0));
                    ListView myListView = (ListView) findViewById(R.id.movielist);
                    SearchResultsAdapter search = new SearchResultsAdapter(MovieActivity.this, jsonArray);
                    myListView.setAdapter(search);

                    //show details of the listed movie in search result after click
                    myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        //switch to the activity MovieDetailsActtivity with extra string of data
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent showActivityDetail = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                            //parse the json array to string to sent as extra
                            String st = jsonArray.toString();
                            showActivityDetail.putExtra("movie_response", st);
                            showActivityDetail.putExtra("Activity_Source", "MovieActivity");
                            startActivity(showActivityDetail);
                        }
                    });
                } catch (JSONException e) {
                    Log.e("JSON exception", e.getMessage());
                    e.printStackTrace();
                }
            }else{
                Log.d("_respo", "??????????????????");
            }

        }
    }

    //Asynctask for progressbar
    private class ProgressBarTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            buttonSearch.setEnabled(false);
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(20);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(20);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            buttonSearch.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 0; i < 5; i++) {
                    Log.d("myLog", "Emulating some task.. Step " + i);
                    TimeUnit.MILLISECONDS.sleep(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

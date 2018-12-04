package group.finalproject.movie;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import group.finalproject.R;

public class ListMovieActivity extends Activity {

    private static final String TAG = "ListMovieActivity";
    DataBaseHelper mDataBaseHelper;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movie);

        mListView = (ListView) findViewById(R.id.list_view);
        mDataBaseHelper = new DataBaseHelper(this);
        populateListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
    }

    private void populateListView() {
        Log.d(TAG, "Populate List view");

        //get the data and append it to list
        Cursor movie_info = mDataBaseHelper.getData("movie_info");
        ArrayList<String> myMovieTitleList = new ArrayList<>();
        ArrayList<String> myMovieYearList = new ArrayList<>();
        ArrayList<Bitmap> moviePoster = new ArrayList<>();
        ArrayList<String> arrayList = new ArrayList<>();

        while (movie_info.moveToNext()) {
            //get the value from the database and add it to column 1 (title)
            //then add it to array list
            myMovieTitleList.add(movie_info.getString(1));
            myMovieYearList.add(movie_info.getString(2));
            moviePoster.add(mDataBaseHelper.getImage(mDataBaseHelper.getImage_fromDB(movie_info.getString(1))));
            arrayList.add(movie_info.getString(1));
        }
        //create the list adapter and set the adapter
        my_movie_list_adapter movieAdapter = new my_movie_list_adapter(this, myMovieTitleList, myMovieYearList, moviePoster);
        mListView.setAdapter(movieAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent showActivityDetail = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                String st = arrayList.get(i);
                showActivityDetail.putExtra("movie_response", st);
                showActivityDetail.putExtra("Activity_Source", "ListMovieActivity");
                startActivity(showActivityDetail);
            }
        });
    }

}

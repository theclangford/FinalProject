package group.finalproject.movie;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;

import group.finalproject.FullScreenImageActivity;
import group.finalproject.R;

public class MovieDetailsActivity extends AppCompatActivity {

    ImageView imageView;
    TextView titleTextView;
    boolean isImageFitToScreen;
    ListView movieInfoListView, leftOverlistView;
    private String fullScreenInd;
    JSONArray ja;
    DataBaseHelper mDataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mDataBaseHelper = new DataBaseHelper(this);

        Intent intent = getIntent();
        //set the title
        titleTextView = (TextView) findViewById(R.id.titletextView);
        //create an object reference to the list movie_info_List
        movieInfoListView = (ListView) findViewById(R.id.movie_info_list);
        //create an object reference to image view poster_imageView and load the poster on that image view
        imageView = (ImageView) findViewById(R.id.poster_imageView);

        //extract the detail of the movie inside the movie tag
        String movie_response = intent.getStringExtra("movie_response");


        if (intent.getStringExtra("Activity_Source").equals("MovieActivity")) {
            try {

                //parse the string to a json array
                ja = new JSONArray(movie_response);
                titleTextView.setText(ja.getString(0) + "\n(" + ja.getString(1)
                        .replaceAll("[^\\d.]", "") + ")");


                //get the plot from the response
                String[] arr = {ja.getString(4)};

                //pass the plot to the adapter
                ArrayAdapter<String> movie_plot =
                        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);

                //populate the ListView of myListView using the adapter
                movieInfoListView.setAdapter(movie_plot);

                JSONArray leftOver = new JSONArray();
                leftOver.put(ja.getString(2));
                leftOver.put(ja.getString(3));

                leftOverlistView = (ListView) findViewById(R.id.movie_list_2);
                //pass the movie information json array to the adapter Movie_Detail_Adapter
                Movie_Detail_Adapter movie_leftOver = new Movie_Detail_Adapter(MovieDetailsActivity.this, leftOver);
                leftOverlistView.setAdapter(movie_leftOver);

                new AsyncTaskLoadImage(imageView).execute(ja.getString(5));

                try {
                    onSnackBarActionClick(ja.getString(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getApplicationContext(),
                                FullScreenImageActivity.class);
                        try {
                            intent.putExtra("poster", ja.getString(5));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if ("y".equals(fullScreenInd)) {
                            intent.putExtra("fullScreenIndicator", "");
                        } else {
                            intent.putExtra("fullScreenIndicator", "y");
                        }
                        intent.putExtra("Activity_Source", "MovieActivity");
                        startActivity(intent);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            Cursor cursor = mDataBaseHelper.getRowDataFromoTitle(movie_response);
            String title = null;
            if (cursor.moveToFirst()) {
                do {
                    title = cursor.getString(cursor.getColumnIndex("title"));
                    String year = cursor.getString(cursor.getColumnIndex("year"));
                    titleTextView.setText(title + "\n(" + year
                            .replaceAll("[^\\d.]", "") + ")");

                    String plot = cursor.getString(cursor.getColumnIndex("plot"));

                    //get the plot from the response
                    String[] arr = {plot};

                    //pass the plot to the adapter
                    ArrayAdapter<String> movie_plot =
                            new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);

                    //populate the ListView of myListView using the adapter
                    movieInfoListView.setAdapter(movie_plot);

                    String runtime = cursor.getString(cursor.getColumnIndex("runtime"));
                    String actors = cursor.getString(cursor.getColumnIndex("actors"));


                    JSONArray leftOver = new JSONArray();
                    leftOver.put(runtime);
                    leftOver.put(actors);

                    leftOverlistView = (ListView) findViewById(R.id.movie_list_2);
                    //pass the movie information json array to the adapter Movie_Detail_Adapter
                    Movie_Detail_Adapter movie_leftOver = new Movie_Detail_Adapter(MovieDetailsActivity.this, leftOver);
                    leftOverlistView.setAdapter(movie_leftOver);

                } while (cursor.moveToNext());
            }
            cursor.close();

            byte[] imgByte = mDataBaseHelper.getImage_fromDB(movie_response);

            imageView.setImageBitmap(mDataBaseHelper.getImage(imgByte));


            onSnackBarActionClick(movie_response);
            final String st = title;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getApplicationContext(),
                            FullScreenImageActivity.class);
                    if ("y".equals(fullScreenInd)) {
                        intent.putExtra("fullScreenIndicator", "");
                    } else {
                        intent.putExtra("fullScreenIndicator", "y");
                    }
                    intent.putExtra("Activity_Source", "ListMovieActivity");
                    intent.putExtra("Title", st);
                    startActivity(intent);
                }
            });


        }


    }

    public void addData(JSONArray newDataJA) {
        Bitmap bitmap = null;

        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        boolean insert_data = mDataBaseHelper.addDataTo_movie_info(newDataJA, getBitmapAsByteArray(bitmap));

        if (insert_data)
            toastMessage("Data Successfully Inserted");
        else
            toastMessage("Something Went Wrong!");
    }

    public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }


    public void onSnackBarActionClick(String movieTitle) {
        View view = findViewById(R.id.movie_detai_layout);
        int duration = Snackbar.LENGTH_INDEFINITE;
        final Snackbar snackbar = Snackbar.make(view, null, duration);
        if (mDataBaseHelper.CheckIsDataAlreadyInDBorNot("movie_info", "title", movieTitle)) {
            snackbar.setAction("Delete Movie", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int i = mDataBaseHelper.deleteData("movie_info", movieTitle);
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        } else {
            snackbar.setAction("Save Movie", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addData(ja);
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }


    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

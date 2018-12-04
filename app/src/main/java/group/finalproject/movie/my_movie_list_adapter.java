package group.finalproject.movie;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import group.finalproject.R;

public class my_movie_list_adapter extends BaseAdapter {


    LayoutInflater mInflator;
    ArrayList<String> myMovieTitleList = new ArrayList<>();
    ArrayList<String> myMovieYearList = new ArrayList<>();
    ArrayList<Bitmap> moviePoster = new ArrayList<>();

    public my_movie_list_adapter(Context c, ArrayList<String> m, ArrayList<String> y, ArrayList<Bitmap> p) {
        myMovieTitleList = m;
        myMovieYearList = y;
        moviePoster = p;
        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myMovieTitleList.size();
    }

    @Override
    public Object getItem(int i) {
        return myMovieTitleList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = mInflator.inflate(R.layout.saved_movie_list, null);
        TextView title = (TextView) v.findViewById(R.id.titleMovie);
        TextView year = (TextView) v.findViewById((R.id.textViewYear));
        ImageView poster = (ImageView) v.findViewById(R.id.imageViewPoster);

        title.setText(myMovieTitleList.get(i));
        year.setText(myMovieYearList.get(i));
        poster.setImageBitmap(moviePoster.get(i));
        return v;
    }
}

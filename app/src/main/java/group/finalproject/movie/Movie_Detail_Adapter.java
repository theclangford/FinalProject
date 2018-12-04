package group.finalproject.movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import group.finalproject.R;

public class Movie_Detail_Adapter extends BaseAdapter {

    LayoutInflater minflater;
    JSONArray ja = null;
    String st = null;

    public Movie_Detail_Adapter(Context c, JSONArray ja){
        this.ja = ja;
        minflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Movie_Detail_Adapter(Context c, String st){
        this.st = st;
        minflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {

        if(ja != null)
            return ja.length();
        else
            return 1;

    }

    @Override
    public Object getItem(int i) {
        String item = null;

        try {
             item = (String )ja.get(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = minflater.inflate(R.layout.moviedetail_list,null);
        TextView titleText = (TextView) v.findViewById(R.id.textView);
        try {
            titleText.setText(ja.getString(i));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return v;
    }
}

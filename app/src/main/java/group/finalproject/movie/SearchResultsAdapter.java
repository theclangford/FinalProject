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

public class SearchResultsAdapter extends BaseAdapter {

    LayoutInflater minflater;
    JSONArray ja = null;

    public SearchResultsAdapter(Context c, JSONArray ja){
        this.ja = ja;
        minflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = minflater.inflate(R.layout.searchresults,null);

        TextView titleText = (TextView) v.findViewById(R.id.titletextView);
        TextView yearText = (TextView) v.findViewById(R.id.yeartextView);
        TextView result = (TextView) v.findViewById(R.id.resultTextview);

        result.setText("Results");
        try {
            titleText.setText(ja.getString(0));
            yearText.setText(ja.getString(1).replaceAll("[^\\d.]", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return v;
    }
}

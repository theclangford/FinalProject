package group.finalproject.food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import group.finalproject.R;

public class TagSummaryAdapter extends ArrayAdapter {

    ArrayList<Tag> list;

    TagSummaryAdapter(Context ctx) {
        super(ctx, 0);
    }

    public void setList(ArrayList<Tag> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Tag getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View result = null;
        result = inflater.inflate(R.layout.tag_summary, null);

        TextView details = (TextView) result.findViewById(R.id.tagDetails);
        Tag currentItem = getItem(position);

        details.setText(String.format("Calories for #%s - Total: %.0f, Average: %.0f, Max: %.0f, Min: %.0f", currentItem.getName(), currentItem.getSum(), currentItem.getAverage(), currentItem.getMax(), currentItem.getMin()));

        return result;
    }

}

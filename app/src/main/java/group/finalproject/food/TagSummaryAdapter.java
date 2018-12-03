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

    /**
     * List of tags
     */
    ArrayList<Tag> list;

    /**
     * Constructor
     * @param ctx
     */
    TagSummaryAdapter(Context ctx) {
        super(ctx, 0);
    }

    /**
     * Sets the list of tags
     * @param list
     */
    public void setList(ArrayList<Tag> list) {
        this.list = list;
    }

    /**
     * Gets the amount of items in list
     * @return list size
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Gets item at position specified
     * @param position
     * @return tag at list position
     */
    @Override
    public Tag getItem(int position) {
        return list.get(position);
    }

    /**
     * Gets the view for tag summary
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View result = null;
        result = inflater.inflate(R.layout.tag_summary, null);

        TextView details = result.findViewById(R.id.tagDetails);
        Tag currentItem = getItem(position);

        details.setText(String.format("#%s cals - total: %.0f, avg: %.0f, max: %.0f, min: %.0f", currentItem.getName(), currentItem.getSum(), currentItem.getAverage(), currentItem.getMax(), currentItem.getMin()));

        return result;
    }

}

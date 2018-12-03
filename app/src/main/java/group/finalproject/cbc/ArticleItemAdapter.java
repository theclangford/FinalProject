package group.finalproject.cbc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import group.finalproject.R;

public class ArticleItemAdapter extends ArrayAdapter<Article> {

    List<Article> list;
    ArticleItemAdapter(Context context){
        super(context, 0);
    }

    public void setList(List<Article> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Article getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View result = null;
        result = inflater.inflate(R.layout.rss_item, null);

        TextView title = result.findViewById(R.id.rssTitle);

        title.setText(getItem(position).getTitle());

        return result;
    }
}

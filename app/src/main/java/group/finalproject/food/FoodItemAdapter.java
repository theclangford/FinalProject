package group.finalproject.food;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import group.finalproject.R;

class FoodItemAdapter extends ArrayAdapter<Food> {

    ArrayList<Food> list;

    FoodItemAdapter(Context ctx) {
        super(ctx, 0);
    }

    public void setList(ArrayList<Food> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Food getItem(int position) {
        return list.get(position);
    }

//        @Override
//        public long getItemId(int position) {
//            return super.getItemId(position);
//        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View result = null;
        result = inflater.inflate(R.layout.food_item, null);

        TextView name = (TextView) result.findViewById(R.id.itemName);
        TextView brand = (TextView) result.findViewById(R.id.itemBrand);
        TextView cal = (TextView) result.findViewById(R.id.itemCal);
        TextView fat = (TextView) result.findViewById(R.id.itemFat);

        name.setText(getItem(position).getName());
        brand.setText(getItem(position).getBrand());
        cal.setText(String.format("Calories: %.0f", getItem(position).getCalories()));
        fat.setText(String.format("Fats: %.1f", getItem(position).getFats()));

        return result;
    }
}

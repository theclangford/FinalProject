package group.finalproject.food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import group.finalproject.R;

public class FavFoodItemAdapter extends FoodItemAdapter {

    /**
     * Instance of FoodDatabaseHelper
     */
    private FoodDatabaseHelper foodDatabase;

    /**
     * Constructor calls super constructor and gets an instance of the FoodDatabaseHelper
     * @param ctx
     */
    FavFoodItemAdapter(Context ctx) {
        super(ctx);
        foodDatabase = FoodDatabaseHelper.getInstance(ctx);
    }

    /**
     * Gets all the views from the food_item layout and sets the appropriate values
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View result = null;
        result = inflater.inflate(R.layout.food_item, null);
        Food currentItem = getItem(position);

        TextView name = result.findViewById(R.id.itemName);
        TextView brand = result.findViewById(R.id.itemBrand);
        TextView cal = result.findViewById(R.id.itemCal);
        TextView fat = result.findViewById(R.id.itemFat);
        TextView tag = result.findViewById(R.id.itemTag);

        name.setText(currentItem.getName());
        brand.setText(currentItem.getBrand());
        cal.setText(String.format("Calories: %.0f", currentItem.getCalories()));
        fat.setText(String.format("Fats: %.1f", currentItem.getFats()));
        if (currentItem.getTag() != "") {
            tag.setText("Tag: #" + currentItem.getTag());
        }

        return result;
    }
}

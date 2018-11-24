package group.finalproject.food;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import group.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodDetailFragment extends Fragment {

    private final String ACTIVITY_NAME = "FoodDetailFragment";
    FoodActivity parent;
    boolean isLandscape;

    public FoodDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get passed details
        Bundle itemDetails = getArguments();

        // Inflate the layout for this fragment
        View screen = inflater.inflate(R.layout.fragment_food_detail, container, false);

        TextView calories = screen.findViewById(R.id.itemDetailCal);
        TextView fats = screen.findViewById(R.id.itemDetailFat);
        TextView protein = screen.findViewById(R.id.itemDetailPro);
        TextView carbs = screen.findViewById(R.id.itemDetailCarb);
        TextView fiber = screen.findViewById(R.id.itemDetailFib);

        calories.setText(String.format("Calories: %.0f", itemDetails.getDouble("cal")));
        fats.setText(String.format("Fat: %.1f", itemDetails.getDouble("fat")));
        protein.setText(String.format("Protein: %.0f", itemDetails.getDouble("protein")));
        carbs.setText(String.format("Carbohydrates: %.0f", itemDetails.getDouble("carbs")));
        fiber.setText(String.format("Fiber: %.1f", itemDetails.getDouble("fiber")));

        // Set button to add item to favorites
        Button addFav = screen.findViewById(R.id.addFav);
        addFav.setOnClickListener(v -> {
           if (isLandscape) {
               parent.addItemToFavs(itemDetails.getInt("position"));
           } else {
               Intent position = new Intent();
               position.putExtra("position", itemDetails.getInt("position"));
               getActivity().setResult(222, position);
               getActivity().finish();
           }
        });

        return screen;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (isLandscape)
            parent = (FoodActivity) context;
    }
}

package graduation.plantcare.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import graduation.plantcare.R;
import graduation.plantcare.adapters.GlobalAdapter;

public class DictionaryFragment extends Fragment {

    public DictionaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dictionary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView dictionaryRecyclerView = view.findViewById(R.id.dictionaryRecyclerView);

        dictionaryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<ThreeComponentsItem> dictionaryList = new ArrayList<>();
        dictionaryList.add(new ThreeComponentsItem(R.drawable.ratio_of_nitrogen, "Ratio Of Nitrogen" , "The amount of nitrogen in the soil, which helps plants grow and stay healthy (N)."));
        dictionaryList.add(new ThreeComponentsItem(R.drawable.ratio_of_phosphorus, "Ratio Of Phosphorus" , "The amount of phosphorus in the soil, which helps plants grow and stay healthy (P)."));
        dictionaryList.add(new ThreeComponentsItem(R.drawable.ratio_of_potassium, "Ratio Of Potassium" , "The amount of potassium in the soil, which helps plants grow and stay healthy (K)."));
        dictionaryList.add(new ThreeComponentsItem(R.drawable.temperature, "Temperature" , "How hot or cold the environment is, affecting plant growth and health."));
        dictionaryList.add(new ThreeComponentsItem(R.drawable.humidity_in, "Humidity In %" , "How much water vapor is in the air, shown as a percentage that affects plant growth."));
        dictionaryList.add(new ThreeComponentsItem(R.drawable.ph_value, "PH Value" , "Shows how acidic or alkaline the soil or water is, which affects plant health."));
        dictionaryList.add(new ThreeComponentsItem(R.drawable.rainfall_in_mm, "Water Rainfall" , "The amount of rain that falls, measured in millimeters, important for plant watering."));
        dictionaryList.add(new ThreeComponentsItem(R.drawable.select_crop_type, "Crop Type" , "Choosing the type of crop to grow based on the plant’s needs and environment."));
        dictionaryList.add(new ThreeComponentsItem(R.drawable.select_soil_color, "Soil Color" , "Choosing the color of the soil, which can indicate its type and nutrient content."));

        GlobalAdapter adapter = new GlobalAdapter(getContext(),dictionaryList);
        dictionaryRecyclerView.setAdapter(adapter);
    }
}
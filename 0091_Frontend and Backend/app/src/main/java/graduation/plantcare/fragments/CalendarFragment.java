package graduation.plantcare.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import graduation.plantcare.R;
import graduation.plantcare.adapters.GlobalAdapter;

public class CalendarFragment extends Fragment {
    RecyclerView sowRecycler, harvestRecycler;
    GlobalAdapter sowAdapter, harvestAdapter;
    List<ThreeComponentsItem> sowItems = new ArrayList<>();
    List<ThreeComponentsItem> harvestItems = new ArrayList<>();
    HorizontalScrollView scrollView;
    LinearLayout monthsContainer;
    int currentMonthIndex = Calendar.getInstance().get(Calendar.MONTH);
    int retryCount = 0;
    private static final int MAX_RETRY = 10;
    private boolean isCalendarInitialized = false;

    public CalendarFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sowRecycler = view.findViewById(R.id.calendarPlantsToSowRecyclerView);
        sowRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        sowAdapter = new GlobalAdapter(getContext(), sowItems);
        sowRecycler.setAdapter(sowAdapter);

        harvestRecycler = view.findViewById(R.id.calendarPlantsToHarvestRecyclerView);
        harvestRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        harvestAdapter = new GlobalAdapter(getContext(), harvestItems);
        harvestRecycler.setAdapter(harvestAdapter);

        scrollView = view.findViewById(R.id.horizontalScrollView);
        monthsContainer = view.findViewById(R.id.monthsContainer);

        view.post(this::initializeCalendar);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadPlantsForMonth(String monthKey) {
        try {
            InputStream is = getResources().openRawResource(R.raw.calendar_items);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonString = new String(buffer, StandardCharsets.UTF_8);

            JSONObject fullJson = new JSONObject(jsonString);
            JSONObject monthJson = fullJson.getJSONObject(monthKey);

            Gson gson = new Gson();
            MonthData monthData = gson.fromJson(monthJson.toString(), MonthData.class);

            sowItems.clear();
            harvestItems.clear();
            GlobalAdapter.lastPositionAnimated = -1;

            for (PlantItem item : monthData.plants_to_sow) {
                int imageResId = getResources().getIdentifier(item.image_name, "drawable", getContext().getPackageName());
                sowItems.add(new ThreeComponentsItem(imageResId, item.plant_name, item.description));
            }

            for (PlantItem item : monthData.plants_to_harvest) {
                int imageResId = getResources().getIdentifier(item.image_name, "drawable", getContext().getPackageName());
                harvestItems.add(new ThreeComponentsItem(imageResId, item.plant_name, item.description));
            }

            sowAdapter.notifyDataSetChanged();
            harvestAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initializeCalendar() {
        if (isCalendarInitialized) return;

        View view = getView();
        if (view == null) return;

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        for (int i = 0; i < months.length; i++) {
            final int index = i;
            final LinearLayout monthLayout = (LinearLayout) monthsContainer.getChildAt(i);
            final TextView textView = (TextView) monthLayout.getChildAt(0);
            final View lineView = monthLayout.getChildAt(1);

            if (i == currentMonthIndex) {
                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                lineView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                monthLayout.setBackgroundResource(R.drawable.rounded_selected_items);
                loadPlantsForMonth(months[index].toLowerCase());
                scrollToView(monthLayout);
            } else {
                textView.setTextColor(Color.GRAY);
                lineView.setBackgroundColor(Color.GRAY);
                monthLayout.setBackgroundResource(0);
            }

            monthLayout.setOnClickListener(v -> {
                for (int j = 0; j < months.length; j++) {
                    LinearLayout otherMonth = (LinearLayout) monthsContainer.getChildAt(j);
                    otherMonth.setBackgroundResource(0);
                    TextView otherText = (TextView) otherMonth.getChildAt(0);
                    View otherLine = otherMonth.getChildAt(1);
                    otherText.setTextColor(Color.GRAY);
                    otherLine.setBackgroundColor(Color.GRAY);
                }
                // change the currentMonthIndex to the clicked month
                currentMonthIndex = index;
                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                lineView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                monthLayout.setBackgroundResource(R.drawable.rounded_selected_items);

                scrollToView(monthLayout);
                loadPlantsForMonth(months[index].toLowerCase());
            });
        }

        scheduleScrollWithRetry();
        isCalendarInitialized = true;
    }

    private void scrollToView(final View view) {
        if (scrollView == null || view == null) return;
        Log.d("CalendarFragment", "Scrolling to view with ID: " + view.getId());
        Log.d("CalendarFragment", "ScrollView width: " + scrollView.getWidth());
        Log.d("CalendarFragment", "View width: " + view.getWidth());

        scrollView.post(() -> {
            int scrollX = view.getLeft() - (scrollView.getWidth() / 2) + (view.getWidth() / 2);
            scrollView.smoothScrollTo(scrollX, 0);
        });
    }

    public void scheduleScrollWithRetry() {
        if (scrollView == null || monthsContainer == null) return;

        scrollView.post(() -> {
            if (retryCount >= MAX_RETRY) {
                Log.w("CalendarFragment", "Failed to scroll after " + MAX_RETRY + " tries");
                return;
            }

            if (monthsContainer.getChildCount() > currentMonthIndex) {
                LinearLayout currentMonthLayout = (LinearLayout) monthsContainer.getChildAt(currentMonthIndex);

                if (currentMonthLayout.getWidth() > 0 && scrollView.getWidth() > 0) {
                    int scrollX = currentMonthLayout.getLeft() - (scrollView.getWidth() / 2) + (currentMonthLayout.getWidth() / 2);
                    scrollView.smoothScrollTo(scrollX, 0);
                    Log.d("CalendarFragment", "Scrolled successfully after " + retryCount + " tries");
                } else {
                    retryCount++;
                    scrollView.postDelayed(this::scheduleScrollWithRetry, 50);
                }
            } else {
                retryCount++;
                scrollView.postDelayed(this::scheduleScrollWithRetry, 50);
            }
        });
    }

    public static class PlantItem {
        public String image_name;
        public String plant_name;
        public String description;
    }

    public static class MonthData {
        public List<PlantItem> plants_to_sow;
        public List<PlantItem> plants_to_harvest;
    }
}
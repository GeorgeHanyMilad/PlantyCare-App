package graduation.plantcare.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import graduation.plantcare.R;
import graduation.plantcare.base.BaseActivity;
import graduation.plantcare.fragments.CalendarFragment;
import graduation.plantcare.fragments.DictionaryFragment;
import graduation.plantcare.fragments.HomeFragment;
import graduation.plantcare.fragments.ProfileFragment;

public class HomePage extends BaseActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment homeFragment, calendarFragment, dictionaryFragment, profileFragment;
    private Fragment activeFragment;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home_page);

        bottomNavigationView = findViewById(R.id.bottomNavigationView2);

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        dictionaryFragment = new DictionaryFragment();
        calendarFragment = new CalendarFragment();

        activeFragment = homeFragment;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, homeFragment, "HOME");
        transaction.add(R.id.fragment_container, profileFragment, "PROFILE").hide(profileFragment);
        transaction.add(R.id.fragment_container, dictionaryFragment, "DICTIONARY").hide(dictionaryFragment);
        transaction.add(R.id.fragment_container, calendarFragment, "CALENDAR").hide(calendarFragment);
        transaction.commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.hide(activeFragment);

            int itemId = item.getItemId();

            if (itemId == R.id.homeFragment) {
                ft.show(homeFragment);
                activeFragment = homeFragment;
            } else if (itemId == R.id.profileFragment) {
                ft.show(profileFragment);
                activeFragment = profileFragment;
            } else if (itemId == R.id.dictionaryFragment) {
                ft.show(dictionaryFragment);
                activeFragment = dictionaryFragment;
            } else if (itemId == R.id.calendarFragment) {
                ft.show(calendarFragment);
                activeFragment = calendarFragment;
                ((CalendarFragment) calendarFragment).scheduleScrollWithRetry();
            }
            ft.commit();
            return true;
        });
    }

    public void selectNavigationItem(int itemId) {
        bottomNavigationView.setSelectedItemId(itemId);
    }

    public Fragment getActiveFragment() {
        return activeFragment;
    }
}
package graduation.plantcare.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.cardview.widget.CardView;

import graduation.plantcare.base.BaseActivity;
import graduation.plantcare.R;

public class PrivacyPolicy extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.privacy_policy);
        setAnimations();
    }

    private void setAnimations() {
        CardView card1 = findViewById(R.id.privacy_card1);
        CardView card2 = findViewById(R.id.privacy_card2);
        CardView card3 = findViewById(R.id.privacy_card3);
        CardView card4 = findViewById(R.id.privacy_card4);
        CardView card5 = findViewById(R.id.privacy_card5);
        CardView card6 = findViewById(R.id.privacy_card6);

        card1.postDelayed(() -> {
            card1.setVisibility(View.VISIBLE);
            card1.startAnimation(createSlideUpAnimation(1000));
        }, 500);

        card2.postDelayed(() -> {
            card2.setVisibility(View.VISIBLE);
            card2.startAnimation(createSlideUpAnimation(1000));
        }, 1000);

        card3.postDelayed(() -> {
            card3.setVisibility(View.VISIBLE);
            card3.startAnimation(createSlideUpAnimation(1000));
        }, 1500);

        card4.postDelayed(() -> {
            card4.setVisibility(View.VISIBLE);
            card4.startAnimation(createSlideUpAnimation(1000));
        }, 2000);

        card5.postDelayed(() -> {
            card5.setVisibility(View.VISIBLE);
            card5.startAnimation(createSlideUpAnimation(1000));
        }, 2500);

        card6.postDelayed(() -> {
            card6.setVisibility(View.VISIBLE);
            card6.startAnimation(createSlideUpAnimation(1000));
        }, 3000);
    }

    private Animation createSlideUpAnimation(int duration) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        anim.setDuration(duration);
        return anim;
    }

    public void backButton(View view) {
        finish();
    }
}
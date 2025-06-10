package graduation.plantcare.ui.home;

import static graduation.plantcare.ui.splash.SplashScreen.isInternetAvailable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import graduation.plantcare.base.BaseActivity;
import graduation.plantcare.R;
import graduation.plantcare.utils.FirebaseAuthHelper;
import graduation.plantcare.utils.FirebaseHelper;
import graduation.plantcare.utils.NoInternetConnection;
import graduation.plantcare.utils.UserSessionHelper;
import nl.dionsegijn.konfetti.core.emitter.Confetti;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class RateUs extends BaseActivity {
    RatingBar ratingBar;
    private static final long RATE_DELAY = 10000;
    private UserSessionHelper userSessionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.rate_us);
        ratingBar = findViewById(R.id.ratingBar);
        userSessionHelper = UserSessionHelper.getInstance(this);
        ratingBar.setRating(userSessionHelper.getSavedRating());
    }

    public void cancel(View view) {
        finish();
    }

    public void submit(View view) {
        long lastRatingTime = userSessionHelper.getLastRatingTime();
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastRatingTime < RATE_DELAY) {
            Toast.makeText(this, "Please wait " + (RATE_DELAY - (currentTime - lastRatingTime)) / 1000 + " seconds before submitting another rating", Toast.LENGTH_SHORT).show();
            return;
        }

        isInternetAvailable(this, isConnected -> {
            if (isConnected) {
                float num = ratingBar.getRating();
                FirebaseAuthHelper firebaseAuthHelper = FirebaseAuthHelper.getInstance();
                FirebaseHelper firebaseHelper = new FirebaseHelper();
                FirebaseUser firebaseUser = firebaseAuthHelper.getCurrentUser();

                Map<String, Object> map = new HashMap<>();
                map.put("uid", firebaseUser.getUid());
                map.put("rating", num);
                map.put("time", currentTime);

                userSessionHelper.saveRating(num, currentTime);

                firebaseHelper.addRate(map, data -> {
                    if (data) {
                        Toast.makeText(this, "Thanks! Sent successfully!", Toast.LENGTH_LONG).show();
                        KonfettiView konfettiView = findViewById(R.id.konfettiView);
                        ConfettiUtils.showConfetti(konfettiView);
                    } else {
                        Toast.makeText(this, "Unexpected error!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                runOnUiThread(() -> {
                    Intent intent = new Intent(this, NoInternetConnection.class);
                    startActivity(intent);
                });
            }
        });
    }
}

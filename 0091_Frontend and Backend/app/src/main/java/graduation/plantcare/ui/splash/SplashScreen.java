package graduation.plantcare.ui.splash;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.net.InetAddress;

import graduation.plantcare.base.BaseActivity;
import graduation.plantcare.R;
import graduation.plantcare.data.user.User;
import graduation.plantcare.ui.home.HomePage;
import graduation.plantcare.utils.FirebaseAuthHelper;
import graduation.plantcare.utils.UserSessionHelper;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends BaseActivity {
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.splash_screen);

        ImageView imageView = findViewById(R.id.logo1);
        TextView plantCareText1 = findViewById(R.id.PLANTYTEXTSPLASHSCREEN1);
        TextView careText1 = findViewById(R.id.CARE1);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        db.setFirestoreSettings(settings);

        imageEffect(imageView);
        textsEffect(plantCareText1, careText1);
        new Handler().postDelayed(this::navigateToNextScreen, 2500);
    }

    private void navigateToNextScreen() {
        UserSessionHelper userSessionHelper = UserSessionHelper.getInstance(this);
        FirebaseAuthHelper firebaseAuthHelper = FirebaseAuthHelper.getInstance();
        User user = userSessionHelper.getUser();
        if (user == null) {
            Intent intent = new Intent(this, SkipScreen1.class);
            startActivity(intent);
        } else {
            if (userSessionHelper.isRememberMeEnabled() && firebaseAuthHelper.isUserLoggedIn() && firebaseAuthHelper.getCurrentUser().isEmailVerified()) {
                Intent intent = new Intent(this, HomePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, WelcomeScreen.class);
                startActivity(intent);
            }
        }
        finish();
    }

    public static void isInternetAvailable(Context context, OnInternetCheckListener listener) {
        Log.d("InternetCheck", "Checking internet...");
        new Thread(() -> {
            try {
                InetAddress ipAddr = InetAddress.getByName("google.com");
                boolean isConnected = !ipAddr.equals("");
                new Handler(Looper.getMainLooper()).post(() -> listener.onResult(isConnected));
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onResult(false));
            }
        }).start();
    }


    public interface OnInternetCheckListener {
        void onResult(boolean isConnected);
    }

    private void textsEffect(TextView plantyCareText1, TextView careText1) {
        plantyCareText1.setVisibility(View.VISIBLE);
        careText1.setVisibility(View.VISIBLE);

        ObjectAnimator fadeIn1 = ObjectAnimator.ofFloat(plantyCareText1, "alpha", 0f, 1f);
        fadeIn1.setDuration(500);

        ObjectAnimator fadeIn2 = ObjectAnimator.ofFloat(careText1, "alpha", 0f, 1f);
        fadeIn2.setDuration(500);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fadeIn1, fadeIn2);
        animatorSet.start();
    }

    private void imageEffect(View view) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", -1000f, 0f);
        translationY.setDuration(2000);

        ObjectAnimator rotationX = ObjectAnimator.ofFloat(view, "rotationX", 0f, 360f);
        rotationX.setDuration(2000);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.2f, 1f);
        scaleX.setDuration(500);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.8f, 1f);
        scaleY.setDuration(500);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationY, rotationX);
        animatorSet.play(scaleX).with(scaleY).after(translationY);
        animatorSet.start();
    }
}
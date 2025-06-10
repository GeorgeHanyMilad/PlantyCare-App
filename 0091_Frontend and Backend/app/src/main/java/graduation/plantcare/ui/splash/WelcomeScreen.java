package graduation.plantcare.ui.splash;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import graduation.plantcare.base.BaseActivity;
import graduation.plantcare.R;
import androidx.activity.EdgeToEdge;

import graduation.plantcare.ui.auth.Login;
import graduation.plantcare.ui.auth.SignUp;

public class WelcomeScreen extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.welcome);
        applyFallingEffect();
    }

    public void login(View view){
        Intent intent = new Intent(WelcomeScreen.this, Login.class);
        startActivity(intent);
    }
    private void applyFallingEffect() {
        ImageView view = findViewById(R.id.plant22);
        view.setAlpha(0f);
        view.setTranslationY(-50f);
        view.setScaleX(0);
        view.setScaleY(0);
        view.animate().alpha(1f).translationY(0f).scaleX(1f).scaleY(1f).setDuration(1000).start();

//        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.002f, 1f);
//        scaleX.setDuration(2000);
//        scaleX.setRepeatCount(100);
//        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.99f, 1f);
//        scaleY.setDuration(2000);
//        scaleY.setRepeatCount(100);
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(scaleY, scaleX);
//        animatorSet.start();
    }

    public void signUp(View view) {
        Intent intent = new Intent(WelcomeScreen.this, SignUp.class);
        startActivity(intent);
    }
}
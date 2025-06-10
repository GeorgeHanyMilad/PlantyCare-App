package graduation.plantcare.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;

import graduation.plantcare.base.BaseActivity;
import graduation.plantcare.R;

public class SkipScreen4 extends BaseActivity {
    ImageView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.skip_screen4);
        view = findViewById(R.id.treeImage4);
        applyFallingEffect();
    }

    private void applyFallingEffect() {
        view.setAlpha(0f);
        view.setTranslationY(-50f);
        view.setScaleX(0);
        view.setScaleY(0);
        view.animate().alpha(1f).translationY(0f).scaleX(1f).scaleY(1f).setDuration(500).start();
    }

    public void skipActivity4(View view) {
        Intent intent = new Intent(SkipScreen4.this, WelcomeScreen.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
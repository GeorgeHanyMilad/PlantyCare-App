package graduation.plantcare.utils;

import static graduation.plantcare.ui.splash.SplashScreen.isInternetAvailable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import graduation.plantcare.R;

public class NoInternetConnection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.no_internet_connection);

    }

    public void tryAgain(View view) {
        isInternetAvailable(this,isConnected -> {
            if (isConnected) {
                finish();
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "No internet signal!", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

}
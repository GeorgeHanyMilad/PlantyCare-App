package graduation.plantcare.ui.auth;

import static graduation.plantcare.ui.splash.SplashScreen.isInternetAvailable;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import graduation.plantcare.base.BaseActivity;
import graduation.plantcare.R;
import graduation.plantcare.utils.FirebaseAuthHelper;
import graduation.plantcare.utils.FirebaseHelper;
import graduation.plantcare.utils.NoInternetConnection;
import graduation.plantcare.utils.UserSessionHelper;

public class CheckYourEmail extends BaseActivity {

    private static final long RESEND_DELAY = 5 * 60 * 1000;
    private long lastResendTime = 0;
    private long currentDelay = RESEND_DELAY * 2;
    private String email, password;
    private TextView checkYourEmailMSG, resendEmailText;
    private FirebaseAuth firebaseAuth;
    private Handler handler = new Handler();
    private Runnable emailVerificationChecker;

    private void startEmailVerificationListener() {
        emailVerificationChecker = () -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                user.reload().addOnCompleteListener(task -> {
                    if (user.isEmailVerified()) {
                        Log.d("FirebaseFirebase", "Email is verified!");
                        FirebaseHelper firebaseHelper = new FirebaseHelper();
                        firebaseHelper.updateUserStatus(firebaseAuth.getUid(), "verified", true);
                        handler.removeCallbacks(emailVerificationChecker);
                    } else {
                        handler.postDelayed(emailVerificationChecker, 5000);
                    }
                });
            }
        };
        handler.post(emailVerificationChecker);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.check_your_email);
        firebaseAuth = FirebaseAuth.getInstance();

        initializeViews();
        loadUserDataFromSharedPreferences();
        prepareResendTextLink();

        setVerifiedListener();
        startEmailVerificationListener();
    }

    private void setVerifiedListener() {
        DatabaseReference nameRef = FirebaseDatabase.getInstance()
                .getReference("users/"+firebaseAuth.getUid()+"/verified");

        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isVerified = dataSnapshot.getValue(Boolean.class);
                if (Boolean.TRUE.equals(isVerified)){
                    UserSessionHelper userSessionHelper = UserSessionHelper.getInstance(CheckYourEmail.this);
                    userSessionHelper.saveUser(firebaseAuth.getCurrentUser(),true);
                    showVerificationSuccessDialog();
                }
                Log.d("Firebase", "Verified is: " + isVerified);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Firebase", "Failed to read name.", error.toException());
            }
        });
    }

    private void initializeViews() {
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        resendEmailText = findViewById(R.id.resendEmailText);
        checkYourEmailMSG = findViewById(R.id.checkYourEmailMessage);
    }

    private void loadUserDataFromSharedPreferences() {
        UserSessionHelper userSessionHelper = UserSessionHelper.getInstance(this);
        FirebaseAuthHelper firebaseAuthHelper = FirebaseAuthHelper.getInstance();
        FirebaseUser firebaseUser = firebaseAuthHelper.getCurrentUser();
        userSessionHelper.saveUser(firebaseUser, false);
        checkYourEmailMSG.setText(checkYourEmailMSG.getText().toString().replace("xxx", "\n" + firebaseUser.getEmail()));
    }

    private void showVerificationSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this , R.style.CustomAlertDialog);
        builder.setCancelable(false);
        builder.setTitle("Email Verified");
        builder.setMessage("Your email has been successfully verified. \nYou will be redirected to the Login page.");
        builder.setPositiveButton("OK", (dialog, which) -> {
            signInAndRedirect();
        });
        builder.setIcon(R.drawable.check);
        builder.show();
    }

    private void signInAndRedirect() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseAuthHelper.getInstance().signIn(email,password, new FirebaseAuthHelper.AuthCallback() {
                @Override
                public void onSuccess(FirebaseUser firebaseUser) {
                    Intent intent = new Intent(CheckYourEmail.this, Login.class);
                    intent.putExtra("email",email);
                    intent.putExtra("pass",password);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(CheckYourEmail.this, "Login failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void backButton(View view) {
        finish();
    }

    public void prepareResendTextLink() {
        String fullText = resendEmailText.getText().toString();
        String subText = "Resend email";
        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf(subText);
        int endIndex = startIndex + subText.length();
        if (startIndex != -1) {
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    isInternetAvailable(CheckYourEmail.this, isConnected -> {
                        if (isConnected) {
                            if (!FirebaseAuthHelper.getInstance().getCurrentUser().isEmailVerified()) {
                                handleResendClick();
                            } else {
                                runOnUiThread(() -> {
                                    Toast.makeText(CheckYourEmail.this, "Email already verified.", Toast.LENGTH_LONG).show();
                                    signInAndRedirect();
                                });
                            }
                        } else {
                            runOnUiThread(() -> {
                                Intent intent = new Intent(CheckYourEmail.this, NoInternetConnection.class);
                                startActivity(intent);
                            });
                        }
                    });
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getColor(R.color.my_link_color_dark_theme));
                    ds.setUnderlineText(true);
                }
            };
            spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        resendEmailText.setText(spannableString);
        resendEmailText.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }

    private void handleResendClick() {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastResend = currentTime - lastResendTime;
        if (timeSinceLastResend < currentDelay) {
            long remainingTime = currentDelay - timeSinceLastResend;
            showRemainingTimeToast(remainingTime);
        } else {
            FirebaseAuthHelper firebaseAuthHelper = FirebaseAuthHelper.getInstance();
            firebaseAuthHelper.getCurrentUser().sendEmailVerification();
            lastResendTime = currentTime;
            currentDelay += RESEND_DELAY;
            Toast.makeText(this, "Verification link sent successfully again!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showRemainingTimeToast(long remainingTime) {
        long remainingMinutes = (remainingTime / 1000) / 60;
        long remainingSeconds = (remainingTime / 1000) % 60;
        String message = String.format("Please wait %d minutes and %d seconds before resending.", remainingMinutes, remainingSeconds);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
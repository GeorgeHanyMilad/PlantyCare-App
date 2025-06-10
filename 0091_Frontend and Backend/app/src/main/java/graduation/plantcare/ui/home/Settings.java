package graduation.plantcare.ui.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;
import graduation.plantcare.R;
import graduation.plantcare.base.BaseActivity;
import graduation.plantcare.data.user.User;
import graduation.plantcare.ui.auth.Login;
import graduation.plantcare.ui.profile.AboutUs;
import graduation.plantcare.ui.profile.ContactUs;
import graduation.plantcare.ui.profile.EditProfile;
import graduation.plantcare.ui.profile.PrivacyPolicy;
import graduation.plantcare.utils.FirebaseAuthHelper;
import graduation.plantcare.utils.FirebaseHelper;
import graduation.plantcare.utils.UserSessionHelper;

public class Settings extends BaseActivity {
    private static final long RESEND_DELAY = 5 * 60 * 1000;
    private long lastResendTime = RESEND_DELAY + 1;
    private long currentDelay = RESEND_DELAY;
    TextView displayName, settingUserName;
    CircleImageView profileImage;
    Switch switchButton;

    @Override
    protected void onResume() {
        super.onResume();
        setDisplayName();
        setPhoto();
    }

    private void setPhoto() {
        Uri photoUrl = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        if (photoUrl != null) {
            Glide.with(this)
                    .load(photoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.loading)
                    .into(profileImage);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.settings);
        displayName = findViewById(R.id.settingDisplayName);
        settingUserName = findViewById(R.id.settingUserName);
        switchButton = findViewById(R.id.darkModeSwitch);
        profileImage = findViewById(R.id.settingProfileIcon);

        UserSessionHelper userSessionHelper = UserSessionHelper.getInstance(this);
        if (userSessionHelper.isDarkMode()) {
            switchButton.setChecked(true);
        }
        switchButton.setOnCheckedChangeListener((compoundButton, b) -> {
            userSessionHelper.setDarkMode(b);
            finish();
            if (b) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            recreate();
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        });

        setDisplayName();
    }

    @SuppressLint("SetTextI18n")
    public void setDisplayName() {
        UserSessionHelper userSessionHelper = UserSessionHelper.getInstance(this);
        User user = userSessionHelper.getUser();
        displayName.setText(user.getFirstName() + " " + user.getLastName());
        settingUserName.setText(user.getEmail());
    }

    public void backButton(View view) {
        finish();
    }

    public void logout(View view) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button signOutButton = dialog.findViewById(R.id.signout);
        MaterialButton cancelButton = dialog.findViewById(R.id.logoutDialogCancel);

        signOutButton.setOnClickListener(v -> {
            Toast.makeText(this, "Signing out...", Toast.LENGTH_SHORT).show();
            UserSessionHelper userSessionHelper = UserSessionHelper.getInstance(this);
            userSessionHelper.setRememberMe(false);
            FirebaseAuthHelper firebaseAuthHelper = FirebaseAuthHelper.getInstance();
            firebaseAuthHelper.signOut();
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            finish();
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void faqs(View view) {
        startActivity(new Intent(this, FAQs.class));
    }

    public void support(View view) {
        startActivity(new Intent(this, ContactUs.class));
    }

    public void privacy(View view) {
        startActivity(new Intent(this, PrivacyPolicy.class));
    }

    public void about(View view) {
        startActivity(new Intent(this, AboutUs.class));
    }

    public void shareTheApp(View view) {
        startActivity(new Intent(this, ShareApp.class));
    }

    public void sendResetLink(View view) {
        handleResendClick();
    }

    private void handleResendClick() {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastResend = currentTime - lastResendTime;
        if (timeSinceLastResend < currentDelay) {
            long remainingTime = currentDelay - timeSinceLastResend;
            showRemainingTimeToast(remainingTime);
        } else {
            FirebaseAuthHelper firebaseAuthHelper = FirebaseAuthHelper.getInstance();
            FirebaseAuth.getInstance().sendPasswordResetEmail(firebaseAuthHelper.getCurrentUser().getEmail());
            lastResendTime = currentTime;
            currentDelay += RESEND_DELAY;
            Toast.makeText(this, "Password reset link sent successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showRemainingTimeToast(long remainingTime) {
        long remainingMinutes = (remainingTime / 1000) / 60;
        long remainingSeconds = (remainingTime / 1000) % 60;
        String message = String.format("Please wait %d minutes and %d seconds before resending.", remainingMinutes, remainingSeconds);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void rateUsMethod(View view) {
        startActivity(new Intent(this, RateUs.class));
    }

    public void editProfileMethod(View view) {
        startActivity(new Intent(this, EditProfile.class));
    }

    public void deleteMyAccount(View view) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_account_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button startDeletion = dialog.findViewById(R.id.startDeletion);
        MaterialButton cancelButton = dialog.findViewById(R.id.goBack);
        TextInputLayout deleteAccountPasswordLayout = dialog.findViewById(R.id.deleteAccountPasswordLayout);
        TextInputEditText deleteAccountPasswordInput = dialog.findViewById(R.id.deleteAccountPasswordInput);

        deleteAccountPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    deleteAccountPasswordLayout.setError("Password is required");
                } else if (charSequence.toString().length() >= 6) {
                    deleteAccountPasswordLayout.setError(null);
                } else {
                    deleteAccountPasswordLayout.setError("Password must be at least 6 characters");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        startDeletion.setOnClickListener(v -> {
            if (deleteAccountPasswordInput.getText().toString().isEmpty()){
                deleteAccountPasswordLayout.setError("Password is required");
            } else if (deleteAccountPasswordLayout.getError() == null){
                User user = UserSessionHelper.getInstance(this).getUser();
                String uid = user.getUID();
                String email = user.getEmail();
                String password = deleteAccountPasswordInput.getText().toString();
                FirebaseAuthHelper.getInstance().deleteEmail(email, password, new FirebaseAuthHelper.AuthCallbackWithoutInfo() {
                    @Override
                    public void onSuccess() {
                        FirebaseHelper firebaseHelper = new FirebaseHelper();
                        firebaseHelper.deleteUser(uid);
                        UserSessionHelper.getInstance(Settings.this).clearUserData();
                        Intent intent = new Intent(Settings.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(Settings.this, uid + " Account deleted successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure() {
                        deleteAccountPasswordLayout.setError("Invalid password!");
                    }
                });
            }
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
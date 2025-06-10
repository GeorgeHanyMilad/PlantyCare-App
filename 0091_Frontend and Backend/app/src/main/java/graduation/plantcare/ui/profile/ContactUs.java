package graduation.plantcare.ui.profile;

import static graduation.plantcare.ui.splash.SplashScreen.isInternetAvailable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import graduation.plantcare.base.BaseActivity;
import graduation.plantcare.R;
import graduation.plantcare.data.user.User;
import graduation.plantcare.utils.FirebaseAuthHelper;
import graduation.plantcare.utils.FirebaseHelper;
import graduation.plantcare.utils.NoInternetConnection;
import graduation.plantcare.utils.UserSessionHelper;

public class ContactUs extends BaseActivity {
    TextInputLayout emailLayout, fullNameLayout, messageLayout;
    TextInputEditText email, fullName, message;
    private static final long RESEND_DELAY = 5 * 60 * 1000;
    private long lastResendTime = RESEND_DELAY + 1;
    private long currentDelay = RESEND_DELAY;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.contact_us);
        initialize();
        setListeners();
        FirebaseAuthHelper firebaseAuthHelper = FirebaseAuthHelper.getInstance();
        if (firebaseAuthHelper.getCurrentUser() != null) {
            email.setText(firebaseAuthHelper.getCurrentUser().getEmail());
            UserSessionHelper userSessionHelper = UserSessionHelper.getInstance(this);
            User user = userSessionHelper.getUser();
            fullName.setText(user.getFirstName() + " " + user.getLastName());
        }
    }

    private void setListeners() {
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    emailLayout.setHelperText(null);
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError("Email mustn't be empty!");
                } else if (!s.toString().contains("@")) {
                    emailLayout.setHelperText(null);
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError("Email must contain '@'!");
                } else if (!s.toString().contains(".")) {
                    emailLayout.setHelperText(null);
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError("Email must contain a domain (e.g., .com, .net)!");
                } else if (!(s.length() >= 6 && s.length() <= 50)) {
                    emailLayout.setHelperText(null);
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError("Email length must be between 6 and 50 characters!");
                } else if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    emailLayout.setHelperText(null);
                    emailLayout.setErrorEnabled(false);
                    emailLayout.setError(null);
                } else {
                    emailLayout.setHelperText(null);
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError("Invalid email format!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 3) {
                    fullNameLayout.setHelperText(null);
                    fullNameLayout.setErrorEnabled(false);
                    fullNameLayout.setError(null);
                } else {
                    fullNameLayout.setHelperText(null);
                    fullNameLayout.setErrorEnabled(true);
                    fullNameLayout.setError("so short!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String msg = s.toString();
                int lenRem = 300 - msg.length();
                messageLayout.setErrorEnabled(true);
                if (msg.isEmpty()) {
                    messageLayout.setError("Empty message!!");
                } else if (msg.length() < 10) {
                    messageLayout.setError(lenRem + "\n" + "so short!");
                } else if (lenRem < 0) {
                    messageLayout.setError(lenRem + "");
                } else {
                    messageLayout.setErrorEnabled(false);
                    messageLayout.setHelperText(lenRem + "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initialize() {
        emailLayout = findViewById(R.id.contactEmailField);
        fullNameLayout = findViewById(R.id.contactFullNameField);
        messageLayout = findViewById(R.id.contactMessageField);
        messageLayout.setHelperText("300");
        email = findViewById(R.id.contactEmailInput);
        fullName = findViewById(R.id.contactFullNameInput);
        message = findViewById(R.id.contactMessageInput);
    }

    public void backButton(View view) {
        finish();
    }

    private boolean isReadyToSend() {
        boolean isValid = true;
        String fName = fullName.getText().toString();
        String emailInput = email.getText().toString();
        String msg = message.getText().toString();

        if (fName.isEmpty()) {
            fullNameLayout.setErrorEnabled(true);
            fullNameLayout.setError("Required!");
            isValid = false;
        }
        if (emailInput.isEmpty()) {
            emailLayout.setErrorEnabled(true);
            emailLayout.setError("Required!");
            isValid = false;
        }
        if (msg.isEmpty()) {
            messageLayout.setErrorEnabled(true);
            messageLayout.setError("Required!");
            isValid = false;
        }

        if (fName.length() < 3) {
            fullNameLayout.setErrorEnabled(true);
            fullNameLayout.setError("so short!");
            isValid = false;
        }

        if (msg.length() < 10) {
            messageLayout.setErrorEnabled(true);
            messageLayout.setError("so short!");
            isValid = false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailLayout.setErrorEnabled(true);
            emailLayout.setError("Invalid email format!");
            isValid = false;
        }

        if (!emailInput.contains("@")) {
            emailLayout.setErrorEnabled(true);
            emailLayout.setError("Email must contain '@'!");
            isValid = false;
        }
        if (!emailInput.contains(".")) {
            emailLayout.setErrorEnabled(true);
            emailLayout.setError("Email must contain a domain (e.g., .com, .net)!");
            isValid = false;
        }

        if (!(emailInput.length() >= 6 && emailInput.length() <= 50)) {
            emailLayout.setErrorEnabled(true);
            emailLayout.setError("Email length must be between 6 and 50 characters!");
            isValid = false;
        }
        if (isValid) {
            fullNameLayout.setError(null);
            fullNameLayout.setErrorEnabled(false);
            emailLayout.setError(null);
            emailLayout.setErrorEnabled(false);
            messageLayout.setError(null);
            messageLayout.setErrorEnabled(false);
        }
        return isValid;
    }

    public void sendMessageMethod(View view) {
        isInternetAvailable(this, isConnected -> {
            if (isConnected) {
                if (isReadyToSend()) {
                    handleResendClick();
                } else {
                    Toast.makeText(this, "All input fields are required!", Toast.LENGTH_SHORT).show();
                }
            } else {
                runOnUiThread(() -> {
                    Intent intent = new Intent(this, NoInternetConnection.class);
                    startActivity(intent);
                });
            }
        });
    }

    private void handleResendClick() {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastResend = currentTime - lastResendTime;
        if (timeSinceLastResend < currentDelay) {
            long remainingTime = currentDelay - timeSinceLastResend;
            showRemainingTimeToast(remainingTime);
        } else {
            FirebaseAuthHelper firebaseAuthHelper = FirebaseAuthHelper.getInstance();
            FirebaseHelper firebaseHelper = new FirebaseHelper();
            FirebaseUser firebaseUser = firebaseAuthHelper.getCurrentUser();
            Map<String, Object> map = new HashMap<>();
            map.put("uid", firebaseUser.getUid());
            map.put("email", email.getText().toString());
            map.put("fullName", fullName.getText().toString());
            map.put("message", message.getText().toString());
            firebaseHelper.addSupportMessage(map, data -> {
                if (data) {
                    message.setText(null);
                    messageLayout.setError(null);
                    messageLayout.setHelperText("300");
                    Toast.makeText(this, "Sent successfully!", Toast.LENGTH_SHORT).show();
                    lastResendTime = currentTime;
                    currentDelay += RESEND_DELAY;
                } else {
                    Toast.makeText(this, "Unexpected error!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showRemainingTimeToast(long remainingTime) {
        long remainingMinutes = (remainingTime / 1000) / 60;
        long remainingSeconds = (remainingTime / 1000) % 60;
        String message = String.format("Please wait %d minutes and %d seconds before resending.", remainingMinutes, remainingSeconds);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
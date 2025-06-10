package graduation.plantcare.ui.auth;

import static graduation.plantcare.ui.splash.SplashScreen.isInternetAvailable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import graduation.plantcare.base.BaseActivity;
import graduation.plantcare.R;
import graduation.plantcare.data.user.User;
import graduation.plantcare.utils.FirebaseHelper;
import graduation.plantcare.utils.NoInternetConnection;
import graduation.plantcare.utils.UserSessionHelper;

public class ForgetPassword extends BaseActivity {
    MaterialButton continueBtn;
    TextInputLayout emailField;
    TextInputEditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.forget_password);
        initializeVariables();

        emailEditText.addTextChangedListener(createEmailWatcher());
        continueBtn.setOnClickListener(s -> {
            isInternetAvailable(this, isConnected -> {
                if (isConnected) {
                    handleForgetPassword();
                } else {
                    runOnUiThread(() -> {
                        Intent intent = new Intent(this, NoInternetConnection.class);
                        startActivity(intent);
                    });
                }
            });
        });

    }

    private void handleForgetPassword() {
        runOnUiThread(() -> {
            if (isReady()) {
                FirebaseHelper firebaseHelper = new FirebaseHelper();
                firebaseHelper.getUserByEmail(emailEditText.getText().toString(), new FirebaseHelper.UserCallback() {
                    @Override
                    public void onUserFound(User u) {
                        if (u.getVerified()) {
                            FirebaseAuth.getInstance().sendPasswordResetEmail(emailEditText.getText().toString())
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Log.d("FirebaseAuth", "تم إرسال رابط إعادة تعيين كلمة المرور");
                                            Toast.makeText(ForgetPassword.this, "Reset link sent successfully!", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(ForgetPassword.this, Login.class);
                                            intent.putExtra("email", emailEditText.getText().toString());
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Log.e("FirebaseAuth", "خطأ: " + task.getException().getMessage());
                                            Toast.makeText(ForgetPassword.this, "فشل في إرسال البريد", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onUserNotFound() {
                        emailField.setError("Email not registered!");

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
            } else {
                Intent intent = new Intent(this, NoInternetConnection.class);
                startActivity(intent);
            }
        });
    }

    private boolean isReady() {
        String emailInput = emailEditText.getText().toString().trim();

        boolean isValid = true;

        if (emailInput.isEmpty()) {
            emailField.setError("Required!");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailField.setError("Invalid email format!");
            isValid = false;
        } else {
            emailField.setError(null);
        }

        return isValid;
    }


    private TextWatcher createEmailWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    emailField.setHelperText(null);
                    emailField.setError("Email mustn't be empty!");
                } else if (!s.toString().contains("@")) {
                    emailField.setHelperText(null);
                    emailField.setError("Email must contain '@'!");
                } else if (!s.toString().contains(".")) {
                    emailField.setHelperText(null);
                    emailField.setError("Email must contain a domain (e.g., .com, .net)!");
                } else if (!(s.length() >= 6 && s.length() <= 50)) {
                    emailField.setHelperText(null);
                    emailField.setError("Email length must be between 6 and 50 characters!");
                } else if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    emailField.setHelperText(null);
                    emailField.setError(null);
                } else {
                    emailField.setHelperText(null);
                    emailField.setError("Invalid email format!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private void initializeVariables() {
        UserSessionHelper userSessionHelper = UserSessionHelper.getInstance(this);
        continueBtn = findViewById(R.id.forgetContinueButton);
        emailField = findViewById(R.id.forgetEmailField);
        emailEditText = findViewById(R.id.forgetEmailInput);
        if (userSessionHelper.getUser() != null)
            emailEditText.setText(userSessionHelper.getUser().getEmail());
    }

    public void continueMethod(View view) {
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.isEmailExists(emailEditText.getText().toString(), isExist -> {
            if (isExist) {
                SharedPreferences sharedPreferences = getSharedPreferences("emailToChangePassword", MODE_PRIVATE);
                sharedPreferences.edit().putString("email", emailEditText.getText().toString()).apply();
                Intent intent = new Intent(this, CheckYourEmail.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(ForgetPassword.this, "Failed!", Toast.LENGTH_SHORT).show();
                emailField.setError("Email is NOT registered!");
            }
        });
    }

    public void backFinish(View view) {
        finish();
    }
}
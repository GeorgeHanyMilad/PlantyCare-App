package graduation.plantcare.ui.auth;

import static graduation.plantcare.ui.splash.SplashScreen.isInternetAvailable;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import graduation.plantcare.base.BaseActivity;
import graduation.plantcare.R;
import graduation.plantcare.data.user.User;
import graduation.plantcare.ui.home.HomePage;
import graduation.plantcare.utils.FirebaseAuthHelper;
import graduation.plantcare.utils.NoInternetConnection;
import graduation.plantcare.utils.UserSessionHelper;

public class Login extends BaseActivity {
    FirebaseAuthHelper firebaseAuthHelper = FirebaseAuthHelper.getInstance();
    private ProgressBar progressBar;
    private TextInputEditText email, password;
    private TextInputLayout emailField, passwordField;
    private MaterialButton login;
    private TextView errorMsg;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);

        initializeViews();
        setupListeners();
        prepareTextLinks();
        loadUserData();

        login.setOnClickListener(v -> {
            isInternetAvailable(this, isConnected -> {
                if (isConnected) {
                    handleLogin();
                } else {
                    runOnUiThread(() -> {
                        Intent intent = new Intent(this, NoInternetConnection.class);
                        startActivity(intent);
                    });
                }
            });
        });
    }

    private void initializeViews() {
        email = findViewById(R.id.loginEmailInput);
        emailField = findViewById(R.id.loginEmailField);
        password = findViewById(R.id.loginPasswordInput);
        passwordField = findViewById(R.id.loginPasswordField);
        errorMsg = findViewById(R.id.loginErrorMessage);
        login = findViewById(R.id.loginButton);
        checkBox = findViewById(R.id.loginCheckBox);
        progressBar = findViewById(R.id.loginProgressBar);
    }

    private void loadUserData() {
        UserSessionHelper userSessionHelper = UserSessionHelper.getInstance(this);
        User user = userSessionHelper.getUser();
        if (user != null) {
            email.setText(user.getEmail());
        } else {
            FirebaseUser firebaseUser = firebaseAuthHelper.getCurrentUser();
            if (firebaseUser != null) {
                email.setText(firebaseUser.getEmail());
            }
        }
    }

    private void setupListeners() {
        email.addTextChangedListener(createEmailWatcher());
        password.addTextChangedListener(createPasswordWatcher());
    }

    private TextWatcher createEmailWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String emailInput = s.toString().trim();
                if (emailInput.isEmpty()) {
                    emailField.setError(getString(R.string.error_email_empty));
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    emailField.setError(getString(R.string.error_invalid_email));
                } else {
                    emailField.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private TextWatcher createPasswordWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passwordInput = s.toString();
                if (passwordInput.isEmpty()) {
                    passwordField.setError("Required!");
                } else if (passwordInput.length() < 6) {
                    passwordField.setError("Password must be at least 6 characters.");
                } else {
                    passwordField.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private void handleLogin() {
        runOnUiThread(() -> {
            if (isReadyToLogin()) {
                progressBar.setVisibility(View.VISIBLE);
                login.setEnabled(false);
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                loginWithEmailAndPassword(firebaseAuthHelper, mail, pass);
            }
        });
    }

    private void loginWithEmailAndPassword(FirebaseAuthHelper firebaseHelper, String mail, String pass) {
        firebaseHelper.signIn(mail, pass, new FirebaseAuthHelper.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                if (user.isEmailVerified()) {
                    UserSessionHelper userSessionHelper = UserSessionHelper.getInstance(Login.this);
                    userSessionHelper.saveUser(user, checkBox.isChecked());
                    new Handler().postDelayed(Login.this::handleSuccessfulLogin, 2000);
                } else {
                    showError("Please verify your email address!");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE); // Hide ProgressBar
                    login.setEnabled(true); // Re-enable login button
                    showError("Invalid Email or Password!");
                });
            }
        });
    }

    private void handleSuccessfulLogin() {
        errorMsg.setText(null);
        errorMsg.setVisibility(View.INVISIBLE);
        runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            login.setEnabled(true);
        });
        Toast.makeText(this, "Login Successfully!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Login.this, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void showError(String message) {
        errorMsg.setText(message);
        errorMsg.setVisibility(View.VISIBLE);
    }

    private boolean isReadyToLogin() {
        String emailInput = email.getText().toString().trim();
        String passwordInput = password.getText().toString();

        boolean isValid = true;

        if (emailInput.isEmpty()) {
            emailField.setErrorEnabled(true);
            emailField.setError("Required!");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailField.setErrorEnabled(true);
            emailField.setError("Invalid email format!");
            isValid = false;
        } else {
            emailField.setError(null);
            emailField.setErrorEnabled(false);
        }

        if (passwordInput.isEmpty()) {
            passwordField.setErrorEnabled(true);
            passwordField.setError("Required!");
            isValid = false;
        } else if (passwordInput.length() < 6) {
            passwordField.setErrorEnabled(true);
            passwordField.setError("Password must be at least 6 characters.");
            isValid = false;
        } else {
            passwordField.setErrorEnabled(false);
            passwordField.setError(null);
        }

        return isValid;
    }

    private void prepareTextLinks() {
        prepareSignUpTextLink();
        prepareForgetPasswordTextLink();
    }

    private void prepareSignUpTextLink() {
        TextView textView = findViewById(R.id.sign_up_text);
        String fullText = textView.getText().toString();
        String subText = getString(R.string.sign_up_link);
        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf(subText);
        int endIndex = startIndex + subText.length();
        if (startIndex != -1) {
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Intent intent = new Intent(Login.this, SignUp.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getColor(R.color.my_link_color_dark_theme));
                    ds.setUnderlineText(true);
                }
            };
            spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(spannableString);
        textView.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }

    private void prepareForgetPasswordTextLink() {
        TextView forgotPasswordText = findViewById(R.id.forget_password_text);
        SpannableString spannableString = new SpannableString(getString(R.string.forget_password));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(Login.this, ForgetPassword.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getColor(R.color.my_link_color_dark_theme));
                ds.setUnderlineText(true);
            }
        };
        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgotPasswordText.setText(spannableString);
        forgotPasswordText.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }
}
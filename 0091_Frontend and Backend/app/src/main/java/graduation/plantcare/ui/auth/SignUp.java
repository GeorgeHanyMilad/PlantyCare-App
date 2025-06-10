package graduation.plantcare.ui.auth;

import static graduation.plantcare.ui.splash.SplashScreen.isInternetAvailable;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import graduation.plantcare.base.BaseActivity;
import graduation.plantcare.R;
import graduation.plantcare.ui.profile.PrivacyPolicy;
import graduation.plantcare.utils.FirebaseAuthHelper;
import graduation.plantcare.utils.NoInternetConnection;

public class SignUp extends BaseActivity {

    private TextInputEditText firstName, lastName, email, password, confirmPassword;
    private TextInputLayout firstNameField, lastNameField, emailField, passwordField, confField;
    private MaterialButton signup;
    private MaterialCheckBox suCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_up);

        initializeViews();
        setupListeners();
        prepareLoginTextLink();
        preparePolicyTextLink();

        signup.setOnClickListener(v -> {
            isInternetAvailable(this, isConnected -> {
                if (isConnected) {
                    handleSignUp();
                } else {
                    runOnUiThread(() ->{
                        Intent intent = new Intent(SignUp.this, NoInternetConnection.class);
                        startActivity(intent);
                    });
                }
            });
        });
    }

    private void initializeViews() {
        firstName = findViewById(R.id.signupFirstNameInput);
        firstNameField = findViewById(R.id.signupFirstNameField);

        lastName = findViewById(R.id.signupLastNameInput);
        lastNameField = findViewById(R.id.signupLastNameField);

        email = findViewById(R.id.signupEmailInput);
        emailField = findViewById(R.id.signupEmailField);

        password = findViewById(R.id.signupPasswordInput);
        passwordField = findViewById(R.id.signupPasswordField);

        confirmPassword = findViewById(R.id.signupConfirmPasswordInput);
        confField = findViewById(R.id.signupConfirmPasswordField);

        signup = findViewById(R.id.signupButton);
        suCheckBox = findViewById(R.id.signupCheckBox);
    }

    private void setupListeners() {
        suCheckBox.setOnClickListener(v -> {
            if (suCheckBox.isChecked()) {
                suCheckBox.setError(null);
            } else {
                suCheckBox.setError("Required!");
            }
        });

        firstName.addTextChangedListener(createTextWatcher(firstNameField, 3, "so short!"));
        lastName.addTextChangedListener(createTextWatcher(lastNameField, 3, "so short!"));
        email.addTextChangedListener(createEmailWatcher());
        password.addTextChangedListener(createPasswordWatcher());
        confirmPassword.addTextChangedListener(createConfirmPasswordWatcher());
    }

    private TextWatcher createTextWatcher(TextInputLayout field, int minLength, String errorMessage) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= minLength) {
                    field.setHelperText(null);
                    field.setError(null);
                    field.setErrorEnabled(false);
                } else {
                    field.setHelperText(null);
                    field.setErrorEnabled(true);
                    field.setError(errorMessage);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
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
                    emailField.setErrorEnabled(true);
                    emailField.setError("Email mustn't be empty!");
                } else if (!s.toString().contains("@")) {
                    emailField.setHelperText(null);
                    emailField.setErrorEnabled(true);
                    emailField.setError("Email must contain '@'!");
                } else if (!s.toString().contains(".")) {
                    emailField.setHelperText(null);
                    emailField.setErrorEnabled(true);
                    emailField.setError("Email must contain a domain (e.g., .com, .net)!");
                } else if (!(s.length() >= 6 && s.length() <= 50)) {
                    emailField.setHelperText(null);
                    emailField.setErrorEnabled(true);
                    emailField.setError("Email length must be between 6 and 50 characters!");
                } else if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    emailField.setHelperText(null);
                    emailField.setError(null);
                    emailField.setErrorEnabled(false);
                } else {
                    emailField.setHelperText(null);
                    emailField.setErrorEnabled(true);
                    emailField.setError("Invalid email format!");
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
                    passwordField.setHelperText(null);
                    passwordField.setErrorEnabled(true);
                    passwordField.setError("Password cannot be empty!");
                } else if (passwordInput.length() < 6) {
                    passwordField.setHelperText(null);
                    passwordField.setErrorEnabled(true);
                    passwordField.setError("Password must be at least 6 chars long!");
                } else if (!passwordInput.matches(".*[A-Z].*")) {
                    passwordField.setHelperText(null);
                    passwordField.setErrorEnabled(true);
                    passwordField.setError("Password must contain at least one uppercase (A-Z)!");
                } else if (!passwordInput.matches(".*[a-z].*")) {
                    passwordField.setHelperText(null);
                    passwordField.setErrorEnabled(true);
                    passwordField.setError("Password must contain at least one lowercase (a-z)!");
                } else if (!passwordInput.matches(".*\\d.*")) {
                    passwordField.setHelperText(null);
                    passwordField.setErrorEnabled(true);
                    passwordField.setError("Password must contain at least one digit (0-9)!");
                } else if (!passwordInput.matches(".*[!@#$%^&*()_+=<>?/{}\\[\\]-].*")) {
                    passwordField.setHelperText(null);
                    passwordField.setErrorEnabled(true);
                    passwordField.setError("Password must contain at least one special character!");
                } else {
                    updatePasswordStrength(passwordInput);
                }
                if (!confirmPassword.getText().toString().isEmpty()) {
                    confirmPassword.setText(confirmPassword.getText());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private void updatePasswordStrength(String passwordInput) {
        if (passwordInput.length() < 10) {
            passwordField.setHelperText("Weak Password");
            passwordField.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#FFA500")));
            passwordField.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#FFA500")));
            passwordField.setBoxStrokeColor(Color.parseColor("#FFA500"));
        } else if (passwordInput.length() <= 12) {
            passwordField.setHelperText("Medium Password");
            passwordField.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#FFD700")));
            passwordField.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#FFD700")));
            passwordField.setBoxStrokeColor(Color.parseColor("#FFD700"));
        } else {
            passwordField.setHelperText("Strong Password");
            passwordField.setHintTextColor(ColorStateList.valueOf(Color.GREEN));
            passwordField.setHelperTextColor(ColorStateList.valueOf(Color.GREEN));
            passwordField.setBoxStrokeColor(Color.GREEN);
        }
    }

    private TextWatcher createConfirmPasswordWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String confirm = s.toString();
                if (confirmPassword.getText().toString().trim().isEmpty()) {
                    confField.setError("Password cannot be empty!");
                    confField.setErrorEnabled(true);
                    return;
                }
                if (!confirm.equals(password.getText().toString())) {
                    confField.setError("Passwords do not match!");
                    confField.setErrorEnabled(true);
                } else {
                    confField.setHelperText("Confirmed!");
                    confField.setError(null);
                    confField.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private void handleSignUp() {
        if (isReadyToSignUp()) {
            String fName = firstName.getText().toString();
            String lName = lastName.getText().toString();
            String mail = email.getText().toString();
            String pass = password.getText().toString();

            FirebaseAuthHelper firebaseAuthHelper = FirebaseAuthHelper.getInstance();

            firebaseAuthHelper.signUp(mail, pass, fName, lName, new FirebaseAuthHelper.AuthCallback() {
                @Override
                public void onSuccess(FirebaseUser user) {
                    firebaseAuthHelper.getCurrentUser().sendEmailVerification();
                    Toast.makeText(SignUp.this, "Verification link Sent Successfully!", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(SignUp.this, CheckYourEmail.class);
                    in.putExtra("email", mail);
                    in.putExtra("password", pass);
                    startActivity(in);
                    finish();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(SignUp.this, "Failed!", Toast.LENGTH_SHORT).show();
                    emailField.setErrorEnabled(true);
                    emailField.setError("Email is already registered!");
                }
            });
        }
    }

    private boolean isReadyToSignUp() {
        boolean isValid = true;
        String fName = firstName.getText().toString().trim();
        String lName = lastName.getText().toString().trim();
        String emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();
        String confirmPasswordInput = confirmPassword.getText().toString();

        if (fName.isEmpty()) {
            firstNameField.setErrorEnabled(true);
            firstNameField.setError("Required!");
            isValid = false;
        }
        if (lName.isEmpty()) {
            lastNameField.setErrorEnabled(true);
            lastNameField.setError("Required!");
            isValid = false;
        }
        if (emailInput.isEmpty()) {
            emailField.setErrorEnabled(true);
            emailField.setError("Required!");
            isValid = false;
        }
        if (passwordInput.isEmpty()) {
            passwordField.setErrorEnabled(true);
            passwordField.setError("Required!");
            isValid = false;
        }
        if (confirmPasswordInput.isEmpty()) {
            confField.setErrorEnabled(true);
            confField.setError("Required!");
            isValid = false;
        }
        if (!confirmPasswordInput.equals(passwordInput)) {
            confField.setErrorEnabled(true);
            confField.setError("Passwords do not match!");
            isValid = false;
        }
        if (!suCheckBox.isChecked()) {
            suCheckBox.setError("Required!");
            isValid = false;
        }
        return isValid;
    }

    public void prepareLoginTextLink() {
        TextView textView = findViewById(R.id.haveAnAccountLoginText);
        String fullText = textView.getText().toString();
        String subText = getString(R.string.login_link);
        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf(subText);
        int endIndex = startIndex + subText.length();
        if (startIndex != -1) {
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Intent intent = new Intent(SignUp.this, Login.class);
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

    public void preparePolicyTextLink() {
        CheckBox checkBoxText = findViewById(R.id.signupCheckBox);
        String fullText = checkBoxText.getText().toString();
        String subText = getString(R.string.privacy_policy_link);
        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf(subText);
        int endIndex = startIndex + subText.length();
        if (startIndex != -1) {
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Intent intent = new Intent(SignUp.this, PrivacyPolicy.class);
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
        checkBoxText.setText(spannableString);
        checkBoxText.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }
}


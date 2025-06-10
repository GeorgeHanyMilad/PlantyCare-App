package graduation.plantcare.ui.profile;

import static graduation.plantcare.ui.splash.SplashScreen.isInternetAvailable;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import graduation.plantcare.R;
import graduation.plantcare.base.BaseActivity;
import graduation.plantcare.data.user.User;
import graduation.plantcare.ui.home.Settings;
import graduation.plantcare.utils.FirebaseHelper;
import graduation.plantcare.utils.FirebaseStorageHelper;
import graduation.plantcare.utils.NoInternetConnection;
import graduation.plantcare.utils.UserSessionHelper;

public class EditProfile extends BaseActivity {
    private static final int SAVE_DELAY = 10000;
    private CircleImageView profileImage;
    private TextInputLayout firstNameLayout, lastNameLayout;
    private TextInputEditText firstName, lastName;
    private MaterialButton cancel, save;
    private String oldFirstName, oldLastName;
    private ProgressBar progressBar;
    private Dialog dialog;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private boolean isNameChanged = false;
    private boolean isImageChanged = false;
    private boolean isSaving = false;
    private Bitmap originalBitmap;

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            imagePickerLauncher.launch(cameraIntent);
        } else {
            Log.e("Camera", "No app to handle camera intent");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted!", Toast.LENGTH_SHORT).show();
                openCamera();
            } else {
                Toast.makeText(this, "Camera Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void newProfilePicture(View view2) {
        dialog = new Dialog(EditProfile.this);
        dialog.setContentView(R.layout.model1_picture_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView uploadBtn = dialog.findViewById(R.id.uploadButton);
        ImageView takePhotoBtn = dialog.findViewById(R.id.takePhotoButton);
        TextView textView = dialog.findViewById(R.id.model1TextView);
        MaterialButton removeBtn = dialog.findViewById(R.id.removeButton);
        textView.setText("Select Your New Picture :)");

        uploadBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        takePhotoBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA_PERMISSION
                );
            }
        });

        removeBtn.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.edit_profile);

        initializeVariables();
        initializeValues();
        setupListeners();

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        Uri selectedImageUri = data.getData();
                        if (selectedImageUri != null) {
                            profileImage.setImageURI(selectedImageUri);
                            isImageChanged = true;
                        }
                        dialog.dismiss();
                    } else {
                        Log.d("ImagePicker", "Result not OK or data is null");
                    }
                });
    }

    private void initializeVariables() {
        profileImage = findViewById(R.id.editProfileIcon);
        firstNameLayout = findViewById(R.id.editFirst);
        lastNameLayout = findViewById(R.id.editLast);
        firstName = findViewById(R.id.editFirstInput);
        lastName = findViewById(R.id.editLastInput);
        cancel = findViewById(R.id.editCancelBtn);
        save = findViewById(R.id.editSaveBtn);

    }


    private void initializeValues() {
        UserSessionHelper userSessionHelper = UserSessionHelper.getInstance(this);
        User user = userSessionHelper.getUser();

        Drawable drawable = profileImage.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            originalBitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            if (width <= 0) width = profileImage.getWidth();
            if (height <= 0) height = profileImage.getHeight();
            if (width <= 0 || height <= 0) {
                width = 512;
                height = 512;
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            originalBitmap = bitmap;
        }

        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        oldFirstName = user.getFirstName();
        oldLastName = user.getLastName();
        progressBar = findViewById(R.id.editProgressBar);

        Uri photoUrl = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        if (photoUrl != null) {
            Glide.with(this)
                    .load(photoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_internet)
                    .into(profileImage);
        }
    }

    private void setupListeners() {
        setTextListeners(firstName, firstNameLayout);
        setTextListeners(lastName, lastNameLayout);

        profileImage.setOnClickListener(v -> {
            isImageChanged = true;
            newProfilePicture(v);
        });

        cancel.setOnClickListener(t -> finish());
        save.setOnClickListener(t -> {
            if (isSaving) {
                Toast.makeText(this, "Please wait before saving again", Toast.LENGTH_SHORT).show();
                return;
            }

            isSaving = true;

            isInternetAvailable(this, isConnected -> {
                if (!isConnected) {
                    runOnUiThread(() -> {
                        isSaving = false;
                        startActivity(new Intent(this, NoInternetConnection.class));
                    });
                    return;
                }
                handleSaveBtn();
            });
        });
    }

    private void setTextListeners(TextInputEditText textInputEditText, TextInputLayout textInputLayout) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Required!");
                } else if (s.toString().length() < 3) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Must be at least 3 characters!");
                } else {
                    textInputLayout.setError(null);
                    textInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                isNameChanged = !firstName.getText().toString().equals(oldFirstName)
                        || !lastName.getText().toString().equals(oldLastName);
            }
        });

    }

    private boolean isReadyToSave() {
        boolean isValid = true;
        String fName = firstName.getText().toString().trim();
        String lName = lastName.getText().toString().trim();
        if (fName.isEmpty()) {
            firstNameLayout.setError("Required!");
            isValid = false;
        }
        if (lName.isEmpty()) {
            lastNameLayout.setError("Required!");
            isValid = false;
        }
        if (firstNameLayout.getError() != null || lastNameLayout.getError() != null) {
            isValid = false;
        }
        if (!isNameChanged && !isImageChanged) {
            Toast.makeText(this, "No changes to save!", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        Log.d("isSaving", String.valueOf(isSaving));
        return isValid;
    }

    private void handleSaveBtn() {
        if (!isReadyToSave()) {
            isSaving = false;
            return;
        }

        if (!isNameChanged && !isImageChanged) {
            Toast.makeText(this, "No changes to save!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        String currentFirstName = firstName.getText().toString();
        String currentLastName = lastName.getText().toString();

        if (isNameChanged) {
            Map<String, Object> map = new HashMap<>();
            map.put("firstName", currentFirstName);
            map.put("displayName", currentFirstName);
            map.put("lastName", currentLastName);
            firebaseHelper.updateUserStatus(user.getUid(), map);

            UserSessionHelper.getInstance(this).saveUser(user, true);
        }

        if (isImageChanged) {
            updateProfileImage();
        } else {
            UserSessionHelper.getInstance(this).setName(currentFirstName, currentLastName);
            new Handler().postDelayed(() -> {
                progressBar.setVisibility(View.INVISIBLE);
                finish();
            }, 1000);
        }

        new Handler().postDelayed(() -> {
            isSaving = false;
        }, SAVE_DELAY);
    }

    private void updateProfileImage() {
        Bitmap currentBitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
        if (currentBitmap.sameAs(originalBitmap)) {
            isImageChanged = false;
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        progressBar.setVisibility(View.VISIBLE);

        FirebaseStorageHelper.uploadProfileImage(
                currentBitmap,
                user.getUid(),
                new FirebaseStorageHelper.OnImageUploadListener() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        updateUserProfile(downloadUri);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(EditProfile.this,
                                    "Image upload failed: " + errorMessage,
                                    Toast.LENGTH_LONG).show();
                            isSaving = false;
                        });
                        Log.e("Firebase", errorMessage);
                    }
                });
    }

    private void updateUserProfile(Uri photoUri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(photoUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (task.isSuccessful()) {
                        UserSessionHelper.getInstance(this).saveUser(user, true);
                        finish();
                    } else {
                        Toast.makeText(EditProfile.this,
                                "Profile update failed",
                                Toast.LENGTH_SHORT).show();
                    }
                    isSaving = false;
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
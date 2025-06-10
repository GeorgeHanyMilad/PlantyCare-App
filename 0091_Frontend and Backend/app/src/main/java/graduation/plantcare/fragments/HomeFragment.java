package graduation.plantcare.fragments;

import static graduation.plantcare.ui.splash.SplashScreen.isInternetAvailable;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import graduation.plantcare.R;
import graduation.plantcare.adapters.OnScanResultChangedListener;
import graduation.plantcare.features.model_one.ModelOne;
import graduation.plantcare.ui.home.HistoryItem;
import graduation.plantcare.ui.home.Settings;
import graduation.plantcare.utils.NoInternetConnection;
import graduation.plantcare.utils.UserSessionHelper;
import android.Manifest;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import graduation.plantcare.adapters.ScanResultAdapter;
import graduation.plantcare.features.model_three.ModelThree;
import graduation.plantcare.features.model_two.ModelTwo;
import graduation.plantcare.ui.auth.Login;
import graduation.plantcare.utils.FirebaseAuthHelper;

public class HomeFragment extends Fragment implements OnScanResultChangedListener {

    private MaterialButton newScanButton, plantPickButton, fertilizerButton;
    private TextView homeGreetingMessage, emptyText;
    private String uid;
    private CircleImageView profileImageView;
    private ImageButton settingIcon;
    private RecyclerView recyclerView;
    private FrameLayout homeFrame1, homeFrame2, homeFrame3;
    private Dialog dialog;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private ValueEventListener userExistenceListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        initializeViews(view);
        setListeners(view);
        setupUserSession();
        setupAnimations();
        setupImagePicker();
        loadUserProfileImage();
    }

    private void setListeners(View view) {
        newScanButton.setOnClickListener(v -> {
            showImagePickerDialog();
        });
        plantPickButton.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ModelTwo.class));
        });
        fertilizerButton.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ModelThree.class));
        });
        settingIcon.setOnClickListener(v -> openSettingActivity(view));
        profileImageView.setOnClickListener(v -> {
            try {
                openImage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void initializeViews(View view) {
        homeGreetingMessage = view.findViewById(R.id.homeGreetingMessage);
        profileImageView = view.findViewById(R.id.homeProfileIcon);
        recyclerView = view.findViewById(R.id.homeRecyclerView);
        emptyText = view.findViewById(R.id.tv_empty_history);
        homeFrame1 = view.findViewById(R.id.homeFrame1);
        homeFrame2 = view.findViewById(R.id.homeFrame2);
        homeFrame3 = view.findViewById(R.id.homeFrame3);
        newScanButton = view.findViewById(R.id.newScanMaterialButton);
        plantPickButton = view.findViewById(R.id.plantPickMaterialButton);
        fertilizerButton = view.findViewById(R.id.fertilizerMaterialButton);
        settingIcon = view.findViewById(R.id.homeSettingIcon);
    }

    private void setupUserSession() {
        UserSessionHelper userSessionHelper = UserSessionHelper.getInstance(requireContext());
        uid = userSessionHelper.getUser().getUID();
        homeGreetingMessage.setText("Hi, " + userSessionHelper.getUser().getFirstName() + " 🔥");
        setEmailExitsListener();
    }

    private void setupAnimations() {
        homeGreetingMessage.startAnimation(createFadeInAnimation(2000));

        homeFrame1.postDelayed(() -> {
            homeFrame1.setVisibility(View.VISIBLE);
            homeFrame1.startAnimation(createSlideUpAnimation(1000));
        }, 500);

        homeFrame2.postDelayed(() -> {
            homeFrame2.setVisibility(View.VISIBLE);
            homeFrame2.startAnimation(createSlideUpAnimation(1000));
        }, 800);

        homeFrame3.postDelayed(() -> {
            homeFrame3.setVisibility(View.VISIBLE);
            homeFrame3.startAnimation(createSlideUpAnimation(1000));
        }, 1000);
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        handleImageResult(result.getData());
                        Log.d("ImagePicker", "Image picked successfully");
                    } else {
                        Log.d("ImagePicker", "Result not OK or data is null");
                    }
                });
    }

    private void handleImageResult(Intent data) {
        try {
            Bitmap bitmap = getBitmapFromIntent(data);
            File file = createImageFile(bitmap);
            Log.d("ImageProcessing", "Image processed successfully1");
            isInternetAvailable(getContext(), isConnected -> {
                Log.d("ImageProcessing", "Image processed successfully2");
                if (isConnected) {
                    startModelOneActivity(file);
                    if (dialog != null) dialog.dismiss();
                } else {
                    showNoInternetConnection();
                    Log.d("ImageProcessing", "No internet connection");
                }
            });
        } catch (IOException e) {
            Log.e("ImageProcessing", "Error processing image", e);
            Toast.makeText(requireContext(), "Error processing image", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getBitmapFromIntent(Intent data) throws IOException {
        Uri selectedImageUri = data.getData();
        if (selectedImageUri != null) {
            return MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);
        } else {
            Bundle extras = data.getExtras();
            return (Bitmap) extras.get("data");
        }
    }

    private File createImageFile(Bitmap bitmap) throws IOException {
        File file = new File(requireContext().getCacheDir(), System.currentTimeMillis() + ".jpg");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        }
        return file;
    }

    private void startModelOneActivity(File file) {
        Intent intent = new Intent(requireContext(), ModelOne.class);
        intent.putExtra("imagePath", file.getAbsolutePath());
        startActivity(intent);
        Log.d("ImageProcessing", "Starting ModelOneActivity");
    }

    private void showNoInternetConnection() {
        requireActivity().runOnUiThread(() -> {
            Intent intent = new Intent(requireContext(), NoInternetConnection.class);
            startActivity(intent);
        });
    }

    private void loadUserProfileImage() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null && firebaseUser.getPhotoUrl() != null && getContext() != null) {
            Glide.with(getContext())
                    .load(firebaseUser.getPhotoUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.loading)
                    .into(profileImageView);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Camera Permission Granted!", Toast.LENGTH_SHORT).show();
                openCamera();
            } else {
                Toast.makeText(requireContext(), "Camera Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchScanResults();
        loadUserProfileImage();
        setupUserSession();
    }

    private void setEmailExitsListener() {
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("users").child(uid);

        userExistenceListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    FirebaseAuthHelper.getInstance().signOut();
                    showDeleteAccountDialog();
                    Log.d("Firebase" , "User was deleted from database");
                } else {
                    String firstName = snapshot.child("firstName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);
                    UserSessionHelper userSessionHelper = UserSessionHelper.getInstance(getContext());
                    userSessionHelper.setName(firstName,lastName);
                    homeGreetingMessage.setText("Hi, " + firstName + " 🔥");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Database error", error.toException());
            }
        };

        userRef.addValueEventListener(userExistenceListener);
    }

    private void showDeleteAccountDialog() {
        if (isAdded() && !requireActivity().isFinishing() && !requireActivity().isDestroyed()) {
            new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                    .setCancelable(false)
                    .setTitle("Account Deleted")
                    .setMessage("Your account has been deleted from our system.\n\nIf this was a mistake, please contact support or create a new account.")
                    .setPositiveButton("OK", (dialog, which) -> navigateToLogin())
                    .setIcon(R.drawable.warning)
                    .show();
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(requireContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private Animation createSlideUpAnimation(int duration) {
        Animation anim = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up);
        anim.setDuration(duration);
        return anim;
    }

    private Animation createFadeInAnimation(int duration) {
        Animation anim = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in);
        anim.setDuration(duration);
        return anim;
    }

    private void showImagePickerDialog() {
        dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.model1_picture_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView uploadBtn = dialog.findViewById(R.id.uploadButton);
        ImageView takePhotoBtn = dialog.findViewById(R.id.takePhotoButton);
        MaterialButton removeBtn = dialog.findViewById(R.id.removeButton);

        uploadBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        takePhotoBtn.setOnClickListener(v -> checkCameraPermission());

        removeBtn.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION
            );
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            imagePickerLauncher.launch(cameraIntent);
        } else {
            Log.e("Camera", "No app to handle camera intent");
            Toast.makeText(requireContext(), "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchScanResults() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        ));

        db.collection("first_model_scans")
                .whereEqualTo("userId", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<HistoryItem> results = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        HistoryItem result = doc.toObject(HistoryItem.class);
                        if (result != null) {
                            result.setDocumentId(doc.getId());
                            results.add(result);
                        }
                    }

                    updateRecyclerView(results);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching scans", e);
                    Toast.makeText(requireContext(), "Failed to load history", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateRecyclerView(List<HistoryItem> results) {
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(new ScanResultAdapter(requireContext(), results, this));
        } else {
            ((ScanResultAdapter) recyclerView.getAdapter()).updateData(results);
        }
        emptyText.setVisibility(results.isEmpty() ? View.VISIBLE : View.INVISIBLE);
    }

    public void openImage() throws IOException {
        Drawable drawable = profileImageView.getDrawable();
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            bitmap = getBitmapFromVectorDrawable(drawable);
        }

        File file = new File(requireContext().getCacheDir(), "temp_image.png");
        FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.flush();
        out.close();

        Uri uri = FileProvider.getUriForFile(
                requireContext(),
                "graduation.plantcare.provider",
                file
        );

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    private Bitmap getBitmapFromVectorDrawable(Drawable drawable) {
        if (drawable instanceof VectorDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888
            );
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    public void openSettingActivity(View view) {
        startActivity(new Intent(requireContext(), Settings.class));
    }

    public void showEmptyView() {
        emptyText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (userExistenceListener != null) {
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                    .removeEventListener(userExistenceListener);
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onScanResultsEmpty() {
        showEmptyView();
    }
}
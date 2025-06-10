package graduation.plantcare.features.model_one;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import graduation.plantcare.R;
import graduation.plantcare.base.BaseActivity;
import graduation.plantcare.data.models.model1.ApiClient;
import graduation.plantcare.data.models.model1.ApiService;
import graduation.plantcare.data.models.model1.ImagePredictionResponse;
import graduation.plantcare.utils.FirebaseAuthHelper;
import graduation.plantcare.utils.FirebaseHelper;
import graduation.plantcare.utils.FirebaseStorageHelper;
import graduation.plantcare.utils.UserSessionHelper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModelOne extends BaseActivity {
    TextView modelOneDiseaseName2, modelOneBriefDescription2, modelOnePreventDescription2, modelOneSupplementName2;
    ImageView modelOneSupplementImage2, modelOneUserPlantImage2;
    CardView briefCard, stepsCard, supplementCard;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.model_one_result);

        initialize();
        String imagePath = getIntent().getStringExtra("imagePath");
        retrieveModelOneData(imagePath);
    }

    public void initialize() {
        modelOneBriefDescription2 = findViewById(R.id.modelOneBriefDescription);
        modelOneDiseaseName2 = findViewById(R.id.modelOneDiseaseName);
        modelOnePreventDescription2 = findViewById(R.id.modelOnePreventDescription);
        modelOneSupplementName2 = findViewById(R.id.modelOneSupplementName);
        modelOneSupplementImage2 = findViewById(R.id.modelOneSupplementImage);
        modelOneUserPlantImage2 = findViewById(R.id.modelOneUserPlantImage);
        progressBar = findViewById(R.id.modelOneProgressBar);
        briefCard = findViewById(R.id.briefCard);
        stepsCard = findViewById(R.id.stepsCard);
        supplementCard = findViewById(R.id.supplementCard);
    }

    private void retrieveModelOneData(String imagePath) {
        Intent intent = getIntent();
        if (intent.hasExtra("imageUrl")) {
            loadFromIntentData();
            return;
        }

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            showFailureDialog();
            return;
        }

        Glide.with(this)
                .load(Uri.fromFile(imageFile))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.loading)
                .error(R.drawable.no_internet)
                .into(modelOneUserPlantImage2);

        ApiService apiService = ApiClient.getApiService();
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

        Call<ImagePredictionResponse> call = apiService.predictWithImage(body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ImagePredictionResponse> call, Response<ImagePredictionResponse> response) {
                Log.e("ModelOne", "API Error: " + response.body());
                if (response.isSuccessful() && response.body() != null) {
                    handleApiSuccess(response.body(), imageFile);
                    FirebaseHelper  firebaseHelper = new FirebaseHelper();
                    firebaseHelper.increaseModelOneCount(UserSessionHelper.getInstance(ModelOne.this).getUser().getUID(), isSuccess -> {});
                    UserSessionHelper.getInstance(ModelOne.this).increaseModelOneScore();
                } else {
                    showFailureDialog();
                }
            }

            @Override
            public void onFailure(Call<ImagePredictionResponse> call, Throwable t) {
                Log.e("ModelOne", "API Error: " + t.getMessage());
                showFailureDialog();
            }
        });
    }

    private void handleApiSuccess(ImagePredictionResponse prediction, File imageFile) {
        modelOneDiseaseName2.setText(prediction.getDiseaseName());
        modelOneBriefDescription2.setText(prediction.getDescription());
        modelOnePreventDescription2.setText(prediction.getSteps());
        modelOneSupplementName2.setText(prediction.getSupplementName());

        Glide.with(this)
                .load(prediction.getSupplementImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .addListener(new RequestListener<>() {
                    @Override
                    public boolean onLoadFailed(
                            @Nullable GlideException e,
                            Object model,
                            Target<Drawable> target,
                            boolean isFirstResource
                    ) {
                        runOnUiThread(() -> {
                            modelOneSupplementImage2.setVisibility(View.GONE);
                        });
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(
                            Drawable resource,
                            Object model,
                            Target<Drawable> target,
                            DataSource dataSource,
                            boolean isFirstResource
                    ) {
                        runOnUiThread(() -> {
                            modelOneSupplementImage2.setVisibility(View.VISIBLE);
                        });
                        return false;
                    }
                })
                .into(modelOneSupplementImage2);

        uploadImage(
                Uri.fromFile(imageFile),
                prediction.getDiseaseName(),
                prediction.getDescription(),
                prediction.getSteps(),
                prediction.getSupplementName(),
                prediction.getSupplementImage()
        );
    }

    private void setAnimations() {
        modelOneDiseaseName2.postDelayed(() -> {
            modelOneDiseaseName2.setVisibility(View.VISIBLE);
            modelOneDiseaseName2.startAnimation(createFadeInAnimation(1500));
        },500);
        modelOneUserPlantImage2.postDelayed(()->{
            modelOneUserPlantImage2.setVisibility(View.VISIBLE);
            modelOneUserPlantImage2.startAnimation(createSlideUpAnimation(500));
        },500);
        briefCard.postDelayed(() -> {
            briefCard.setVisibility(View.VISIBLE);
            briefCard.startAnimation(createSlideUpAnimation(1000));
        }, 1000);
        stepsCard.postDelayed(() -> {
            stepsCard.setVisibility(View.VISIBLE);
            stepsCard.startAnimation(createSlideUpAnimation(1500));
        }, 1500);

        supplementCard.postDelayed(() -> {
            supplementCard.setVisibility(View.VISIBLE);
            supplementCard.startAnimation(createSlideUpAnimation(1500));
        }, 2000);
    }

    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Invalid Input")
                .setMessage("There was an error processing your image. Please try again.")
                .setPositiveButton("Back", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void uploadImage(Uri imageUri, String diseaseName, String description,
                             String steps, String supplementName, String supplementImage) {

        FirebaseStorageHelper.uploadPlantImage(imageUri, new FirebaseStorageHelper.OnImageUploadListener() {
            @Override
            public void onSuccess(Uri downloadUri) {
                Log.d("FirebaseStorage", "Image uploaded successfully: " + downloadUri.toString());
                setAnimations();
                saveResultToFirebase(downloadUri.toString(), diseaseName, description, steps, supplementName, supplementImage);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("FirebaseStorage", "Upload failed: " + errorMessage);
                Toast.makeText(ModelOne.this, "Upload failed: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Animation createFadeInAnimation(int duration) {
        Animation anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        anim.setDuration(duration);
        return anim;
    }

    private Animation createSlideUpAnimation(int duration) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        anim.setDuration(duration);
        return anim;
    }

    private void saveResultToFirebase(String imageUri, String diseaseName, String description,
                                      String steps, String supplementName, String supplementImage) {
        FirebaseAuthHelper authHelper = FirebaseAuthHelper.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.d("Firestore", "Saving result to Firebase...");
        Map<String, Object> result = new HashMap<>();
        result.put("userId", authHelper.getCurrentUser().getUid());
        result.put("imageUrl", imageUri);
        result.put("diseaseName", diseaseName);
        result.put("description", description);
        result.put("steps", steps);
        result.put("supplementName", supplementName);
        result.put("supplementImage", supplementImage);
        result.put("timestamp", System.currentTimeMillis());

        db.collection("first_model_scans")
                .add(result)
                .addOnSuccessListener((documentReference) -> {
                    Log.d("Firestore", "Document added: " + documentReference.getId());
                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e ->
                        Log.w("Firestore", "Error adding document", e));
    }

    private void loadFromIntentData() {
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("imageUrl");
        String diseaseName = intent.getStringExtra("diseaseName");
        String description = intent.getStringExtra("description");
        String steps = intent.getStringExtra("steps");
        String supplementName = intent.getStringExtra("supplementName");
        String supplementImage = intent.getStringExtra("supplementImage");

        modelOneDiseaseName2.setText(diseaseName);
        modelOneBriefDescription2.setText(description);
        modelOnePreventDescription2.setText(steps);
        modelOneSupplementName2.setText(supplementName);

        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.loading)
                .into(modelOneUserPlantImage2);

        Glide.with(this)
                .load(supplementImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(
                            @Nullable GlideException e,
                            Object model,
                            Target<Drawable> target,
                            boolean isFirstResource
                    ) {
                        runOnUiThread(() -> {
                            modelOneSupplementImage2.setVisibility(View.GONE);
                        });
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(
                            Drawable resource,
                            Object model,
                            Target<Drawable> target,
                            DataSource dataSource,
                            boolean isFirstResource
                    ) {
                        runOnUiThread(() -> {
                            modelOneSupplementImage2.setVisibility(View.VISIBLE);
                        });
                        return false;
                    }
                })
                .into(modelOneSupplementImage2);

        progressBar.setVisibility(View.GONE);
        setAnimations();
    }

    public void backButton(View view) {
        finish();
    }
}
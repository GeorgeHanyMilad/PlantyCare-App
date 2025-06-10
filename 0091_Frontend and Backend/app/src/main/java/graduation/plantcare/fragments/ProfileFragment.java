package graduation.plantcare.fragments;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;

import de.hdodenhof.circleimageview.CircleImageView;
import graduation.plantcare.R;
import graduation.plantcare.data.user.User;
import graduation.plantcare.ui.profile.EditProfile;
import graduation.plantcare.utils.UserSessionHelper;

public class ProfileFragment extends Fragment {
    private ImageButton editIcon;
    CircleImageView profileIcon;
    TextView profileName, profileEmail, profileDate;
    ProgressBar scanProgress, cropProgress, fertilizerProgress;
    TextView scanPercentage, cropPercentage, fertilizerPercentage, scanNumber, cropNumber, fertilizerNumber;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializations(view);
        setListeners();
        setScoreListeners();
        setProfileData();
        loadUserProfileImage();
    }

    private void setScoreListeners() {
        int modelOneScore = UserSessionHelper.getInstance(requireContext()).getUser().getModelOneScore();
        int modelTwoScore = UserSessionHelper.getInstance(requireContext()).getUser().getModelTwoScore();
        int modelThreeScore = UserSessionHelper.getInstance(requireContext()).getUser().getModelThreeScore();
        int maxScore = modelOneScore + modelTwoScore + modelThreeScore;
        scanProgress.setMax(maxScore);
        cropProgress.setMax(maxScore);
        fertilizerProgress.setMax(maxScore);

        scanProgress.setProgress(modelOneScore);
        cropProgress.setProgress(modelTwoScore);
        fertilizerProgress.setProgress(modelThreeScore);

        scanNumber.setText(String.valueOf(modelOneScore));
        cropNumber.setText(String.valueOf(modelTwoScore));
        fertilizerNumber.setText(String.valueOf(modelThreeScore));

        if (maxScore != 0 ){
            scanPercentage.setText(String.format("%.1f", modelOneScore / (float) maxScore * 100) + "%");
            cropPercentage.setText(String.format("%.1f", modelTwoScore / (float) maxScore * 100) + "%");
            fertilizerPercentage.setText(String.format("%.1f", modelThreeScore / (float) maxScore * 100) + "%");

            setProgressBarAnimation(scanProgress, modelOneScore, 1000);
            setProgressBarAnimation(cropProgress, modelTwoScore, 1000);
            setProgressBarAnimation(fertilizerProgress, modelThreeScore, 1000);
        } else {
            scanPercentage.setText("0%");
            cropPercentage.setText("0%");
            fertilizerPercentage.setText("0%");
        }
    }

    public void setProgressBarAnimation(ProgressBar progressBar, int score, int duration) {
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, score);
        animation.setDuration(duration);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        setProfileData();
        loadUserProfileImage();
        setScoreListeners();
    }

    private void loadUserProfileImage() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null && firebaseUser.getPhotoUrl() != null && getContext() != null) {
            Glide.with(getContext())
                    .load(firebaseUser.getPhotoUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.loading)
                    .into(profileIcon);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setProfileData() {
        UserSessionHelper userSessionHelper = UserSessionHelper.getInstance(requireContext());
        User user = userSessionHelper.getUser();

        if (user != null) {
            profileName.setText(user.getFirstName() + " " + user.getLastName());
            profileEmail.setText(user.getEmail());
            profileDate.setText(getJoinedDuration(user.getTimestamp()));
            Log.d("UserTimeStamp", String.valueOf(user.getTimestamp()));
            int scans = 12;
            int crops = 90;
            int fertilizers = 50;

            scanProgress.setProgress(scans);
            cropProgress.setProgress(crops);
            fertilizerProgress.setProgress(fertilizers);

            scanPercentage.setText(scans + "%");
            cropPercentage.setText(crops + "%");
            fertilizerPercentage.setText(fertilizers + "%");

            scanNumber.setText(String.valueOf(scans));
            cropNumber.setText(String.valueOf(crops));
            fertilizerNumber.setText(String.valueOf(fertilizers));
        }
    }

    private void setListeners() {
        editIcon.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), EditProfile.class);
            startActivity(intent);
        });
        profileIcon.setOnClickListener(v -> {
            try {
                openImage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void initializations(View view) {
        editIcon = view.findViewById(R.id.profileEditIcon);
        profileName = view.findViewById(R.id.myProfileName);
        profileEmail = view.findViewById(R.id.myProfileGmail);
        profileDate = view.findViewById(R.id.profileDate);
        scanProgress = view.findViewById(R.id.myProfileScanProgress);
        cropProgress = view.findViewById(R.id.myProfileCropProgress);
        fertilizerProgress = view.findViewById(R.id.myProfileFertilizerProgress);
        scanPercentage = view.findViewById(R.id.myProfileScanPercentage);
        cropPercentage = view.findViewById(R.id.myProfileCropPercentage);
        fertilizerPercentage = view.findViewById(R.id.myProfileFertilizerPercentage);
        scanNumber = view.findViewById(R.id.myProfileScanNumber);
        cropNumber = view.findViewById(R.id.myProfileCropNumber);
        fertilizerNumber = view.findViewById(R.id.myProfileFertilizerNumber);
        profileIcon = view.findViewById(R.id.myProfileIcon);
    }

    public void openImage() throws IOException {
        Drawable drawable = profileIcon.getDrawable();
        Bitmap bitmap = null;
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


    public static String getJoinedDuration(long timestampMillis) {
        LocalDateTime joinedDate = Instant.ofEpochMilli(timestampMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();

        Period period = Period.between(joinedDate.toLocalDate(), now.toLocalDate());
        Duration duration = Duration.between(joinedDate, now);

        if (period.getYears() > 0) {
            return "Joined " + period.getYears() + " year" + (period.getYears() > 1 ? "s" : "") + " ago";
        } else if (period.getMonths() > 0) {
            return "Joined " + period.getMonths() + " month" + (period.getMonths() > 1 ? "s" : "") + " ago";
        } else if (period.getDays() > 0) {
            return "Joined " + period.getDays() + " day" + (period.getDays() > 1 ? "s" : "") + " ago";
        } else if (duration.toHours() > 0) {
            return "Joined " + duration.toHours() + " hour" + (duration.toHours() > 1 ? "s" : "") + " ago";
        } else if (duration.toMinutes() > 0) {
            return "Joined " + duration.toMinutes() + " minute" + (duration.toMinutes() > 1 ? "s" : "") + " ago";
        } else {
            return "Joined just now";
        }
    }
}
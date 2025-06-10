package graduation.plantcare.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

public class FirebaseStorageHelper {
    private static final StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public interface OnImageUploadListener {
        void onSuccess(Uri downloadUri);
        void onFailure(String errorMessage);
    }

    public static void uploadProfileImage(Bitmap imageBitmap, String userId, OnImageUploadListener listener) {

        deleteOldProfileImage(userId);

        if (imageBitmap == null || userId == null || userId.isEmpty()) {
            listener.onFailure("Invalid input parameters");
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageData = baos.toByteArray();

        StorageReference imageRef = storageRef.child(
                String.format("profile_images/%s.jpg", userId)
        );

        imageRef.putBytes(imageData)
                .addOnSuccessListener(taskSnapshot ->
                        imageRef.getDownloadUrl()
                                .addOnSuccessListener(listener::onSuccess)
                                .addOnFailureListener(e ->
                                        listener.onFailure("Failed to get download URL: " + e.getMessage()))
                )
                .addOnFailureListener(e ->
                        listener.onFailure("Upload failed: " + e.getMessage())
                );
    }

    public static void deleteOldProfileImage(String userId) {
        StorageReference oldImageRef = storageRef.child(
                String.format("profile_images/%s.jpg", userId)
        );
        oldImageRef.delete();
    }


    public static void uploadPlantImage(Uri ImageUri, OnImageUploadListener listener) {
        StorageReference imageRef = storageRef.child("plant_images/" + System.currentTimeMillis() + ".jpg");
        imageRef.putFile(ImageUri)
                .addOnSuccessListener(taskSnapshot ->
                        imageRef.getDownloadUrl()
                                .addOnSuccessListener(listener::onSuccess)
                                .addOnFailureListener(e ->
                                        listener.onFailure("Failed to get download URL: " + e.getMessage()))
                )
                .addOnFailureListener(e ->
                        listener.onFailure("Upload failed: " + e.getMessage())
                );
    }

    public static void deletePlantImage(String userId) {
        StorageReference oldImageRef = storageRef.child(
                String.format("plant_images/%s.jpg", userId)
        );
        oldImageRef.delete();
    }


}
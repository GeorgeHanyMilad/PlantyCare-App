package graduation.plantcare.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FirebaseAuthHelper {
    private static final String TAG = "FirebaseAuthHelper";
    private static FirebaseAuthHelper instance;
    private final FirebaseAuth firebaseAuth;

    private FirebaseAuthHelper() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseAuthHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseAuthHelper();
        }
        return instance;
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }

    public void signUp(String email, String password, String firstName, String lastName, AuthCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = getCurrentUser();
                        if (user != null) {
                            saveUserToRealtimeDatabase(user, firstName, lastName);
                            callback.onSuccess(user);
                        }
                    } else {
                        callback.onFailure(task.getException() != null ? task.getException().getMessage() : "Sign up failed");
                    }
                });
    }

    public void signIn(String email, String password, AuthCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = getCurrentUser();
                        if (user != null) {
                            updateLastLogin();
                            callback.onSuccess(user);
                        }
                    } else {
                        callback.onFailure(task.getException() != null ? task.getException().getMessage() : "Sign in failed");
                    }
                });
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    private void saveUserToRealtimeDatabase(FirebaseUser user, String firstName, String lastName) {
        if (user == null) {
            Log.e(TAG, "❌ User is null, cannot save to Firestore");
            return;
        }

        Log.d(TAG, "🚀 Attempting to save user to Firestore: " + user.getEmail());

        Map<String, Object> userData = new HashMap<>();
        userData.put("uid", user.getUid());
        userData.put("email", user.getEmail());
        userData.put("firstName", firstName);
        userData.put("lastName", lastName);
        userData.put("displayName", firstName);
        userData.put("photoUrl", user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "");
        userData.put("createdAt", System.currentTimeMillis());
        userData.put("lastLogin", System.currentTimeMillis());
        userData.put("lastModified", System.currentTimeMillis());
        userData.put("lastPasswordUpdate", System.currentTimeMillis());
        userData.put("verified" , false);
        userData.put("modelOneScore", 0);
        userData.put("modelTwoScore", 0);
        userData.put("modelThreeScore", 0);
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.addUser(userData, success ->{});
    }

    public void updateLastLogin() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            FirebaseHelper firebaseHelper = new FirebaseHelper();
            firebaseHelper.updateUserStatus(user.getUid() ,"lastLogin",System.currentTimeMillis());
        }
    }

    public void deleteEmail(String email, String password, AuthCallbackWithoutInfo callback) {
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

        firebaseAuth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        firebaseAuth.getCurrentUser().delete()
                                .addOnCompleteListener(deleteTask -> {
                                    if (deleteTask.isSuccessful()) {
                                        callback.onSuccess();
                                        Log.d("DeleteUser", "User deleted after re-authentication.");
                                    } else {
                                        callback.onFailure();
                                        Log.e("DeleteUser", "Deletion failed", deleteTask.getException());
                                    }
                                });
                    } else {
                        callback.onFailure();
                        Log.e("DeleteUser", "Re-authentication failed", task.getException());
                    }
                });
    }

    public interface AuthCallback {
        void onSuccess(FirebaseUser user);

        void onFailure(String errorMessage);
    }

    public interface AuthCallbackWithoutInfo{
        void onSuccess();

        void onFailure();
    }
}
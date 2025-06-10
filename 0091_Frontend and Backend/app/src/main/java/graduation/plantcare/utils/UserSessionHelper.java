package graduation.plantcare.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.google.firebase.auth.FirebaseUser;

import graduation.plantcare.data.user.User;

public class UserSessionHelper {
    private static final String PREF_NAME = "UserData";
    private static final String KEY_REMEMBER_ME = "rememberMe";
    private static final String LAST_RATING_TIME = "last_rating_time";
    private static final String SAVED_RATING = "saved_rating";
    private static UserSessionHelper instance;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private final Context appContext;

    private UserSessionHelper(Context context) {
        this.appContext = context.getApplicationContext();
        sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized UserSessionHelper getInstance(Context context) {
        if (instance == null) {
            instance = new UserSessionHelper(context);
        }
        return instance;
    }

    public void saveUser(FirebaseUser user, boolean rememberMe) {
        if (user == null) return;
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.getUserById(user.getUid(), a -> {
            if (a != null) {
                editor.putString("userId", user.getUid());
                editor.putString("modelOneScore", a.get("modelOneScore").toString());
                editor.putString("modelTwoScore", a.get("modelTwoScore").toString());
                editor.putString("modelThreeScore", a.get("modelThreeScore").toString());
                editor.putString("firstName", a.get("firstName").toString());
                editor.putString("lastName", a.get("lastName").toString());
                editor.putString("email", a.get("email").toString());
                editor.putLong("createdAt", (Long) a.get("createdAt"));
                if (user.getPhotoUrl() != null) {
                    editor.putString("profileImageUrl", a.get("photoUrl").toString());
                }
                editor.putBoolean(KEY_REMEMBER_ME, rememberMe);
                editor.apply();
            }
        });
    }

    public User getUser() {
        String userId = sharedPreferences.getString("userId", null);
        if (userId == null) {
            FirebaseAuthHelper firebaseAuthHelper = FirebaseAuthHelper.getInstance();
            FirebaseUser firebaseUser = firebaseAuthHelper.getCurrentUser();
            if (firebaseUser == null) {
                return null;
            }
            saveUser(firebaseUser, false);
            userId = sharedPreferences.getString("userId", null);
        }
        User user = new User(
                userId,
                sharedPreferences.getString("firstName", "User"),
                sharedPreferences.getString("lastName", ""),
                sharedPreferences.getString("email", ""),
                sharedPreferences.getString("password", ""),
                sharedPreferences.getBoolean("verified", false),
                sharedPreferences.getLong("createdAt", 0)
        );
        user.setProfileImageUrl(sharedPreferences.getString("profileImageUrl", null));
        user.setModelOneScore(Integer.parseInt(sharedPreferences.getString("modelOneScore", "0")));
        user.setModelTwoScore(Integer.parseInt(sharedPreferences.getString("modelTwoScore", "0")));
        user.setModelThreeScore(Integer.parseInt(sharedPreferences.getString("modelThreeScore", "0")));
        return user;
    }

    public void setRememberMe(boolean rememberMe) {
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe);
        editor.apply();
    }

    public void setName(String firstName, String lastName) {
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.apply();
    }

    public boolean isRememberMeEnabled() {
        return sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
    }

    public void setDarkMode(boolean b) {
        editor.putBoolean("darkMode", b);
        editor.apply();
    }

    public boolean isDarkMode() {
        Configuration configuration = appContext.getResources().getConfiguration();
        int currentNightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return sharedPreferences.getBoolean("darkMode", Configuration.UI_MODE_NIGHT_YES == currentNightMode);
    }

    public void saveRating(float rating, long currentTime) {
        editor.putFloat(SAVED_RATING, rating);
        editor.putLong(LAST_RATING_TIME, currentTime);
        editor.apply();
    }

    public float getSavedRating() {
        return sharedPreferences.getFloat(SAVED_RATING, 0);
    }

    public long getLastRatingTime() {
        return sharedPreferences.getLong(LAST_RATING_TIME, 0);
    }

    public void increaseModelOneScore() {
        int score = Integer.parseInt(sharedPreferences.getString("modelOneScore", "0"));
        editor.putString("modelOneScore", String.valueOf(score + 1));
        editor.apply();
    }

    public void increaseModelTwoScore() {
        int score = Integer.parseInt(sharedPreferences.getString("modelTwoScore", "0"));
        editor.putString("modelTwoScore", String.valueOf(score + 1));
        editor.apply();
    }

    public void increaseModelThreeScore() {
        int score = Integer.parseInt(sharedPreferences.getString("modelThreeScore", "0"));
        editor.putString("modelThreeScore", String.valueOf(score + 1));
        editor.apply();
    }

    public void clearUserData() {
        editor.clear();
        editor.apply();
    }
}

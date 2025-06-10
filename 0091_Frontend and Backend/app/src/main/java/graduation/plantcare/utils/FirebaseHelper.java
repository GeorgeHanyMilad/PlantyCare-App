package graduation.plantcare.utils;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;
import graduation.plantcare.data.user.User;

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    private final DatabaseReference usersRef;
    private final DatabaseReference supportEmails;
    private final DatabaseReference rateRef;

    public FirebaseHelper() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        supportEmails = database.getReference("supportMessages");
        rateRef = database.getReference("rates");
    }

    public interface DataCallback<T> {
        void onDataReceived(T data);
    }

    public void addUser(Map<String,Object> user, DataCallback<Boolean> callback) {
        usersRef.child(user.get("uid").toString()).setValue(user)
                .addOnSuccessListener(aVoid -> callback.onDataReceived(true))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "فشل في إضافة المستخدم: " + e.getMessage());
                    callback.onDataReceived(false);
                });
    }

    public void addSupportMessage(Map<String,Object> user, DataCallback<Boolean> callback) {
        supportEmails.child(user.get("uid").toString()).setValue(user)
                .addOnSuccessListener(aVoid -> callback.onDataReceived(true))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "فشل في إضافة المستخدم: " + e.getMessage());
                    callback.onDataReceived(false);
                });
    }

    public void addRate(Map<String,Object> user, DataCallback<Boolean> callback) {
        rateRef.child(user.get("uid").toString()).setValue(user)
                .addOnSuccessListener(aVoid -> callback.onDataReceived(true))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "فشل في إضافة المستخدم: " + e.getMessage());
                    callback.onDataReceived(false);
                });
    }

    public void getUserByEmail(String email, UserCallback callback) {
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        callback.onUserFound(user);
                        return;
                    }
                } else {
                    callback.onUserNotFound();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }


    public void updateUserStatus(String userId, String key, Object value) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(key, value);
        usersRef.child(userId).updateChildren(updates);
    }

    public void updateUserStatus(String userId, Map<String,Object> map) {
        usersRef.child(userId).updateChildren(map);
    }

    public void getUserById(String userId, DataCallback<Map<String,Object>> callback) {
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    GenericTypeIndicator<Map<String, Object>> typeIndicator = new GenericTypeIndicator<Map<String, Object>>() {};
                    Map<String, Object> data = snapshot.getValue(typeIndicator);

                    if (data != null) {
                        Map<String, Object> map = new HashMap<>();
                        for (Map.Entry<String, Object> entry : data.entrySet()) {
                            map.put(entry.getKey() , entry.getValue());
                            Log.d("FirebaseData", "Key: " + entry.getKey() + ", Value: " + entry.getValue());
                        }
                        callback.onDataReceived(map);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "خطأ أثناء جلب المستخدم: " + databaseError.getMessage());
                callback.onDataReceived(null);
            }
        });
    }

    public void isEmailExists(String email, DataCallback<Boolean> callback) {
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callback.onDataReceived(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "خطأ أثناء فحص البريد الإلكتروني: " + databaseError.getMessage());
                callback.onDataReceived(false);
            }
        });
    }

    public interface UserCallback {
        void onUserFound(User user);
        void onUserNotFound();
        void onError(String errorMessage);
    }

    public void increaseModelOneCount(String userId, DataCallback<Boolean> callback) {
        DatabaseReference userModelRef = usersRef.child(userId).child("modelOneScore");

        userModelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long currentValue = 0;
                if (snapshot.exists()) {
                    try {
                        currentValue = snapshot.getValue(Long.class);
                    } catch (Exception e) {
                        Log.e(TAG, "فشل في تحويل القيمة: " + e.getMessage());
                    }
                }

                long newValue = currentValue + 1;
                userModelRef.setValue(newValue)
                        .addOnSuccessListener(aVoid -> callback.onDataReceived(true))
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "فشل في تحديث القيمة: " + e.getMessage());
                            callback.onDataReceived(false);
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "فشل في قراءة القيمة: " + error.getMessage());
                callback.onDataReceived(false);
            }
        });
    }

    public void increaseModelTwoCount(String userId, DataCallback<Boolean> callback) {
        DatabaseReference userModelRef = usersRef.child(userId).child("modelTwoScore");

        userModelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long currentValue = 0;
                if (snapshot.exists()) {
                    try {
                        currentValue = snapshot.getValue(Long.class);
                    } catch (Exception e) {
                        Log.e(TAG, "فشل في تحويل القيمة: " + e.getMessage());
                    }
                }

                long newValue = currentValue + 1;
                userModelRef.setValue(newValue)
                        .addOnSuccessListener(aVoid -> callback.onDataReceived(true))
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "فشل في تحديث القيمة: " + e.getMessage());
                            callback.onDataReceived(false);
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "فشل في قراءة القيمة: " + error.getMessage());
                callback.onDataReceived(false);
            }
        });
    }

    public void increaseModelThreeCount(String userId, DataCallback<Boolean> callback) {
        DatabaseReference userModelRef = usersRef.child(userId).child("modelThreeScore");

        userModelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long currentValue = 0;
                if (snapshot.exists()) {
                    try {
                        currentValue = snapshot.getValue(Long.class);
                    } catch (Exception e) {
                        Log.e(TAG, "فشل في تحويل القيمة: " + e.getMessage());
                    }
                }

                long newValue = currentValue + 1;
                userModelRef.setValue(newValue)
                        .addOnSuccessListener(aVoid -> callback.onDataReceived(true))
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "فشل في تحديث القيمة: " + e.getMessage());
                            callback.onDataReceived(false);
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "فشل في قراءة القيمة: " + error.getMessage());
                callback.onDataReceived(false);
            }
        });
    }

    public void deleteUser(String uid){
        Log.d(TAG, "Trying to delete user with UID: " + uid);
        usersRef.child(uid).removeValue();
        supportEmails.child(uid).removeValue();
        rateRef.child(uid).removeValue();
    }
}
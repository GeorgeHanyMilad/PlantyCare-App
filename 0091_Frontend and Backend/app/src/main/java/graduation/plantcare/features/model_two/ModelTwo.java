package graduation.plantcare.features.model_two;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.io.IOException;
import java.util.Arrays;
import graduation.plantcare.R;
import graduation.plantcare.data.models.model2.*;
import graduation.plantcare.features.model_three.ModelThree;
import graduation.plantcare.utils.FirebaseAuthHelper;
import graduation.plantcare.utils.FirebaseHelper;
import graduation.plantcare.utils.UserSessionHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModelTwo extends AppCompatActivity {
    TextInputLayout ratioOfNitrogenLayout, ratioOfPhosphorusLayout, ratioOfPotassiumLayout, temperatureLayout, humidityLayout, pHValueLayout, rainfallLayout;
    TextInputEditText ratioOfNitrogen, ratioOfPhosphorus, ratioOfPotassium, temperature, humidity, pHValue, rainfall;
    MaterialButton predict;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.model_two);

        initializeVariables();
        setListeners();

    }

    private void setListeners() {
        setListener(ratioOfNitrogen,ratioOfNitrogenLayout);
        setListener(ratioOfPhosphorus ,ratioOfPhosphorusLayout);
        setListener(ratioOfPotassium ,ratioOfPotassiumLayout);
        setListener(temperature ,temperatureLayout);
        setListener(humidity ,humidityLayout);
        setListener(pHValue ,pHValueLayout);
        setListener(rainfall ,rainfallLayout);
    }

    public void setListener(TextInputEditText textInputEditText, TextInputLayout textInputLayout) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String label = textInputLayout.getHint().toString();
                String input = charSequence.toString();

                if (input.isEmpty()) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Required!");
                    return;
                }
                if(input.equals("-")){
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Invalid value!");
                    return;
                }
                float value = Float.parseFloat(input);
                switch (label) {
                    case "Ratio Of Nitrogen":
                    case "Ratio Of Phosphorus":
                    case "Ratio Of Potassium":
                        validateRange(value, 0, 1000, textInputLayout);
                        break;
                    case "Humidity in %":
                        validateRange(value, 0, 100, textInputLayout);
                        break;
                    case "Temperature":
                        validateRange(value, -100, 90, textInputLayout);
                        break;
                    case "PH Value":
                        validateRange(value, 0, 14, textInputLayout);
                        break;
                    case "Rainfall in mm":
                        validateRange(value, 0, 10000, textInputLayout);
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void validateRange(float value, int min, int max, TextInputLayout layout) {
        if (value < min || value > max) {
            layout.setErrorEnabled(true);
            layout.setError("Invalid value! [" + min + " - " + max + "]");
        } else {
            layout.setErrorEnabled(false);
            layout.setError(null);
        }
    }


    private boolean isReadyToPredict() {
        boolean isValid = true;
        if (ratioOfNitrogen.getText().toString().isEmpty()) {
            ratioOfNitrogenLayout.setErrorEnabled(true);
            ratioOfNitrogenLayout.setError("Required!");
            isValid = false;
        }
        if (ratioOfPhosphorus.getText().toString().isEmpty()) {
            ratioOfPhosphorusLayout.setErrorEnabled(true);
            ratioOfPhosphorusLayout.setError("Required!");
            isValid = false;
        }
        if (ratioOfPotassium.getText().toString().isEmpty()) {
            ratioOfPotassiumLayout.setErrorEnabled(true);
            ratioOfPotassiumLayout.setError("Required!");
            isValid = false;
        }
        if (temperature.getText().toString().isEmpty() || temperature.getText().toString().equals("-")) {
            temperatureLayout.setErrorEnabled(true);
            temperatureLayout.setError("Required!");
            isValid = false;
        }
        if (humidity.getText().toString().isEmpty()) {
            humidityLayout.setErrorEnabled(true);
            humidityLayout.setError("Required!");
            isValid = false;
        }
        if (pHValue.getText().toString().isEmpty()) {
            pHValueLayout.setErrorEnabled(true);
            pHValueLayout.setError("Required!");
            isValid = false;
        }
        if (rainfall.getText().toString().isEmpty()) {
            rainfallLayout.setErrorEnabled(true);
            rainfallLayout.setError("Required!");
            isValid = false;
        }
        return isValid;
    }

    private void initializeVariables() {
        ratioOfNitrogenLayout = findViewById(R.id.modelTwoRatioOfNitrogenLayout);
        ratioOfPhosphorusLayout = findViewById(R.id.modelTwoRatioOfPhosphorusLayout);
        ratioOfPotassiumLayout = findViewById(R.id.modelTwoRatioOfPotassiumLayout);
        temperatureLayout = findViewById(R.id.modelTwoTemperatureLayout);
        humidityLayout = findViewById(R.id.modelTwoHumidityLayout);
        pHValueLayout = findViewById(R.id.modelTwoPHValueLayout);
        rainfallLayout = findViewById(R.id.modelTwoRainfallLayout);

        ratioOfNitrogen = findViewById(R.id.modelTwoRatioOfNitrogenInput);
        ratioOfPhosphorus = findViewById(R.id.modelTwoRatioOfPhosphorusInput);
        ratioOfPotassium = findViewById(R.id.modelTwoRatioOfPotassiumInput);
        temperature = findViewById(R.id.modelTwoTemperatureInput);
        humidity = findViewById(R.id.modelTwoHumidityInput);
        pHValue = findViewById(R.id.modelTwoPHValueInput);
        rainfall = findViewById(R.id.modelTwoRainfallInput);

        predict = findViewById(R.id.modelTwoPredictButton);

        dialog = new Dialog(ModelTwo.this);
    }

    public void backButton(View view) {
        finish();
    }

    public void predict(View view) {
        if (isReadyToPredict()){

            dialog.setContentView(R.layout.model2_result);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();
            float ratioOfNitrogenInput = Float.parseFloat(ratioOfNitrogen.getText().toString());
            float ratioOfPhosphorusInput = Float.parseFloat(ratioOfPhosphorus.getText().toString());
            float ratioOfPotassiumInput = Float.parseFloat(ratioOfPotassium.getText().toString());
            float temperatureInput = Float.parseFloat(temperature.getText().toString());
            float humidityInput = Float.parseFloat(humidity.getText().toString());
            float pHValueInput = Float.parseFloat(pHValue.getText().toString());
            float rainfallInput = Float.parseFloat(rainfall.getText().toString());

            FeaturesRequest request = new FeaturesRequest(Arrays.asList(ratioOfNitrogenInput, ratioOfPhosphorusInput, ratioOfPotassiumInput, temperatureInput, humidityInput, pHValueInput, rainfallInput));

            ApiService apiService = ApiClient.getApiService();
            Call<PredictionResponse> call = apiService.predict(request);

            call.enqueue(new Callback<>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        FirebaseHelper firebaseHelper = new FirebaseHelper();
                        firebaseHelper.increaseModelTwoCount(UserSessionHelper.getInstance(ModelTwo.this).getUser().getUID(), v -> {
                        });
                        UserSessionHelper.getInstance(ModelTwo.this).increaseModelTwoScore();

                        String prediction = response.body().getPrediction().get(0);
                        String imageName = prediction.toLowerCase();
                        int imageResId = ModelTwo.this.getResources().getIdentifier(imageName, "drawable", ModelTwo.this.getPackageName());
                        Log.d("IMAGE_ID", "Image Resource ID: " + imageResId);

                        ShapeableImageView imageView = dialog.findViewById(R.id.modelTwoImageView);
                        TextView textView = dialog.findViewById(R.id.modelTwoTextView);
                        ProgressBar progressBar = dialog.findViewById(R.id.modelTwoResultProgressBar);
                        textView.setText("The most suitable crop is:\n" + prediction);
                        MaterialButton okButton = dialog.findViewById(R.id.modelTwoOkButton);
                        okButton.setOnClickListener(v -> dialog.dismiss());
                        if (imageResId != 0) {
                            imageView.setImageResource(imageResId);
                        } else {
                            imageView.setImageResource(R.drawable.check);
                        }
                        progressBar.setVisibility(View.GONE);
                        clearFields();
                    } else {
                        try {
                            Log.e("API_ERROR", "Error: " + response.code() + " " + (response.errorBody()).string());
                            Toast.makeText(ModelTwo.this, "API_ERROR " + response.code(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }


                @Override
                public void onFailure(Call<PredictionResponse> call, Throwable t) {
                    dialog.cancel();
                    Log.e("API_FAILURE", "Failed: " + t.getMessage());
                    Toast.makeText(ModelTwo.this, "API_FAILURE", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void clearFields() {
        ratioOfNitrogen.setText("");
        ratioOfPhosphorus.setText("");
        ratioOfPotassium.setText("");
        temperature.setText("");
        humidity.setText("");
        pHValue.setText("");
        rainfall.setText("");

        ratioOfNitrogen.setError(null);
        ratioOfPhosphorus.setError(null);
        ratioOfPotassium.setError(null);
        temperature.setError(null);
        humidity.setError(null);
        pHValue.setError(null);
        rainfall.setError(null);

        ratioOfNitrogenLayout.setError(null);
        ratioOfPhosphorusLayout.setError(null);
        ratioOfPotassiumLayout.setError(null);
        temperatureLayout.setError(null);
        humidityLayout.setError(null);
        pHValueLayout.setError(null);
        rainfallLayout.setError(null);

        ratioOfNitrogenLayout.setErrorEnabled(false);
        ratioOfPhosphorusLayout.setErrorEnabled(false);
        ratioOfPotassiumLayout.setErrorEnabled(false);
        temperatureLayout.setErrorEnabled(false);
        humidityLayout.setErrorEnabled(false);
        pHValueLayout.setErrorEnabled(false);
        rainfallLayout.setErrorEnabled(false);
    }
}
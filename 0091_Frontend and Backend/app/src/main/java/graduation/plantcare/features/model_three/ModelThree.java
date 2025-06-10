package graduation.plantcare.features.model_three;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import graduation.plantcare.R;
import graduation.plantcare.base.BaseActivity;
import graduation.plantcare.data.models.model3.*;
import graduation.plantcare.utils.FirebaseAuthHelper;
import graduation.plantcare.utils.FirebaseHelper;
import graduation.plantcare.utils.UserSessionHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModelThree extends BaseActivity {
    Spinner cropSpinner, soilSpinner;
    MaterialButton predict;
    TextInputEditText temperatureInput, pHInput, rainfallInput, potassiumInput, nitrogenInput, phosphorusInput;
    TextInputLayout temperatureLayout, pHLayout, rainfallLayout, potassiumLayout, nitrogenLayout, phosphorusLayout;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.model_three);

        initializeVariables();
        setListeners();
    }

    private void setListeners() {
        setListener(nitrogenInput, nitrogenLayout);
        setListener(phosphorusInput, phosphorusLayout);
        setListener(potassiumInput, potassiumLayout);
        setListener(pHInput, pHLayout);
        setListener(temperatureInput, temperatureLayout);
        setListener(rainfallInput, rainfallLayout);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.custom_spinner_item_selected,
                cropSpinner.getResources().getStringArray(R.array.crop_type));
        cropSpinner.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                R.layout.custom_spinner_item_selected,
                soilSpinner.getResources().getStringArray(R.array.soil_color));
        soilSpinner.setAdapter(adapter2);

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
                    case "Temperature":
                        validateRange(value, -100, 90, textInputLayout);
                        break;
                    case "PH Value":
                        validateRange(value, 0, 14, textInputLayout);
                        break;
                    case "Rainfall":
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


    private void initializeVariables() {
        cropSpinner = findViewById(R.id.modelThreeCropType);
        soilSpinner = findViewById(R.id.modelThreeColor);
        temperatureInput = findViewById(R.id.modelThreeTemperatureInput);
        pHInput = findViewById(R.id.modelThreePHValueInput);
        rainfallInput = findViewById(R.id.modelThreeRainfallInput);
        potassiumInput = findViewById(R.id.modelThreeRatioOfPotassiumInput);
        nitrogenInput = findViewById(R.id.modelThreeRatioOfNitrogenInput);
        phosphorusInput = findViewById(R.id.modelThreeRatioOfPhosphorusInput);

        temperatureLayout = findViewById(R.id.modelThreeTemperatureLayout);
        pHLayout = findViewById(R.id.modelThreePHValueLayout);
        rainfallLayout = findViewById(R.id.modelThreeRainfallLayout);
        potassiumLayout = findViewById(R.id.modelThreeRatioOfPotassiumLayout);
        nitrogenLayout = findViewById(R.id.modelThreeRatioOfNitrogenLayout);
        phosphorusLayout = findViewById(R.id.modelThreeRatioOfPhosphorusLayout);

        predict = findViewById(R.id.modelThreePredictButton);

        dialog = new Dialog(ModelThree.this);
    }

    private boolean isReadyToPredict() {
        return validateFields(
                new TextInputEditText[]{nitrogenInput, phosphorusInput, potassiumInput, pHInput, rainfallInput, temperatureInput},
                new TextInputLayout[]{nitrogenLayout, phosphorusLayout, potassiumLayout, pHLayout, rainfallLayout, temperatureLayout}
        );
    }

    public static boolean validateFields(TextInputEditText[] inputs, TextInputLayout[] layouts) {
        boolean isValid = true;
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i].getText().toString().isEmpty() || inputs[i].getText().toString().equals("-")) {
                layouts[i].setErrorEnabled(true);
                layouts[i].setError("Required!");
                isValid = false;
            } else {
                layouts[i].setErrorEnabled(false);
                layouts[i].setError(null);
            }
        }
        return isValid;
    }


    public void backButton(View view) {
        finish();
    }

    public void predict(View view) {
        if (isReadyToPredict()) {
            String crop = cropSpinner.getSelectedItem().toString();
            String soil = soilSpinner.getSelectedItem().toString();

            dialog.setContentView(R.layout.model2_result);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();


            float temperature = Float.parseFloat(temperatureInput.getText().toString());
            float pH = Float.parseFloat(pHInput.getText().toString());
            float rainfall = Float.parseFloat(rainfallInput.getText().toString());
            float potassium = Float.parseFloat(potassiumInput.getText().toString());
            float nitrogen = Float.parseFloat(nitrogenInput.getText().toString());
            float phosphorus = Float.parseFloat(phosphorusInput.getText().toString());

            FeaturesRequest request = new FeaturesRequest(crop, soil, nitrogen, phosphorus, potassium, pH, rainfall, temperature);
            ApiService apiService = ApiClient.getApiService();
            Call<PredictionResponse> call = apiService.predict(request);
            call.enqueue(new Callback<PredictionResponse>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String prediction = response.body().getPredictedFertilizer();
                        FirebaseHelper firebaseHelper = new FirebaseHelper();
                        firebaseHelper.increaseModelThreeCount(UserSessionHelper.getInstance(ModelThree.this).getUser().getUID(), v -> {
                        });
                        UserSessionHelper.getInstance(ModelThree.this).increaseModelThreeScore();
                        TextView textView = dialog.findViewById(R.id.modelTwoTextView);
                        textView.setText("The most suitable fertilizer is:\n" + prediction);
                        ProgressBar progressBar = dialog.findViewById(R.id.modelTwoResultProgressBar);
                        ShapeableImageView imageView = dialog.findViewById(R.id.modelTwoImageView);
                        String imageName = prediction.replaceAll(":", "").split(" ")[0].toLowerCase();
                        int imageResId1 = getResources().getIdentifier(imageName, "drawable", getPackageName());
                        int imageResId2 = getResources().getIdentifier("npk" + imageName, "drawable", getPackageName());
                        MaterialButton okButton = dialog.findViewById(R.id.modelTwoOkButton);
                        okButton.setOnClickListener(v -> dialog.dismiss());

                        if (imageResId2 != 0) {
                            imageView.setImageResource(imageResId2);
                        } else if (imageResId1 != 0) {
                            imageView.setImageResource(imageResId1);
                        } else {
                            imageView.setImageResource(R.drawable.icon);
                        }
                        progressBar.setVisibility(View.GONE);
                        clearFields();
                    } else {
                        try {
                            Log.e("API_ERROR", "Error: " + response.code() + " " + (response.errorBody()).string());
                            Toast.makeText(ModelThree.this, "API_ERROR " + response.code(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<PredictionResponse> call, Throwable t) {
                    dialog.cancel();
                    Log.e("API_FAILURE", "Failed: " + t);
                    Toast.makeText(ModelThree.this, "API_FAILURE" , Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void clearFields() {
        temperatureInput.setText("");
        pHInput.setText("");
        rainfallInput.setText("");
        potassiumInput.setText("");
        nitrogenInput.setText("");
        phosphorusInput.setText("");

        temperatureInput.setError(null);
        pHInput.setError(null);
        rainfallInput.setError(null);
        potassiumInput.setError(null);
        nitrogenInput.setError(null);
        phosphorusInput.setError(null);

        temperatureLayout.setError(null);
        pHLayout.setError(null);
        rainfallLayout.setError(null);
        potassiumLayout.setError(null);
        nitrogenLayout.setError(null);
        phosphorusLayout.setError(null);

        temperatureLayout.setErrorEnabled(false);
        pHLayout.setErrorEnabled(false);
        rainfallLayout.setErrorEnabled(false);
        potassiumLayout.setErrorEnabled(false);
        nitrogenLayout.setErrorEnabled(false);
        phosphorusLayout.setErrorEnabled(false);

        cropSpinner.setSelection(0);
        soilSpinner.setSelection(0);
    }
}

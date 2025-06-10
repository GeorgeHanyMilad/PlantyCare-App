package graduation.plantcare.data.models.model3;

import com.google.gson.annotations.SerializedName;

public class PredictionResponse {

    @SerializedName("predicted_fertilizer")
    private String predictedFertilizer;

    public String getPredictedFertilizer() {
        return predictedFertilizer;
    }
}

package graduation.plantcare.data.models.model1;

import com.google.gson.annotations.SerializedName;

public class ImagePredictionResponse {

    @SerializedName("description")
    private String description;

    @SerializedName("disease_name")
    private String diseaseName;

    @SerializedName("steps")
    private String steps;

    @SerializedName("supplement_image")
    private String supplementImage;

    @SerializedName("supplement_name")
    private String supplementName;

    public String getDescription() {
        return description;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public String getSteps() {
        return steps;
    }

    public String getSupplementImage() {
        return supplementImage;
    }

    public String getSupplementName() {
        return supplementName;
    }
}

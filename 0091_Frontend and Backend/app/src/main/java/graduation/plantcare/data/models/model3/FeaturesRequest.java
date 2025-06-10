package graduation.plantcare.data.models.model3;

import com.google.gson.annotations.SerializedName;

public class FeaturesRequest {
    @SerializedName("Crop")
    private String crop;

    @SerializedName("Soil_color")
    private String soilColor;

    @SerializedName("Nitrogen")
    private double nitrogen;

    @SerializedName("Phosphorus")
    private double phosphorus;

    @SerializedName("Potassium")
    private double potassium;

    @SerializedName("pH")
    private double ph;

    @SerializedName("Rainfall")
    private double rainfall;

    @SerializedName("Temperature")
    private double temperature;

    public FeaturesRequest(String crop, String soilColor, double nitrogen, double phosphorus, double potassium,
                           double ph, double rainfall, double temperature) {
        this.crop = crop;
        this.soilColor = soilColor;
        this.nitrogen = nitrogen;
        this.phosphorus = phosphorus;
        this.potassium = potassium;
        this.ph = ph;
        this.rainfall = rainfall;
        this.temperature = temperature;
    }
}

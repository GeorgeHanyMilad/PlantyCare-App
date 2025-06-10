package graduation.plantcare.data.models.model2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeaturesRequest {
    @SerializedName("features")
    private List<Float> features;
    public FeaturesRequest(List<Float> features){
        this.features = features;
    }
}

package graduation.plantcare.data.models.model3;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface ApiService {
    @POST("/predict")
    Call<PredictionResponse> predict(@Body FeaturesRequest request);
}

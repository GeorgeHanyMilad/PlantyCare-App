package graduation.plantcare.data.models.model1;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("/predict")
    Call<ImagePredictionResponse> predictWithImage(@Part MultipartBody.Part image);
}

package sv.edu.itca.apployment.db;

import retrofit2.Call;
import retrofit2.http.GET;
import sv.edu.itca.apployment.modelos.Worker;

import java.util.List;

public interface ApiService {
    @GET("api/profiles")
    Call<List<Worker>> getWorkers();
}

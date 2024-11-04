package sv.edu.itca.apployment;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TrabajadoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EditText id;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trabajadores);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Trabajadores), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor_fragment, new WorkersFragment())
                    .commit();
        }

        ListarPublicaciones();
    }

    private void ListarPublicaciones() {
        String url = "http://192.168.1.3/api/publications";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Handle successful response
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject publication = response.getJSONObject(i);
                        // Process each publication
                        Log.d("Publication", publication.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // Handle failure response
                Toast.makeText(TrabajadoresActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                if (errorResponse != null) {
                    Log.e("API Error", errorResponse.toString());
                }
            }
        });
    }
}

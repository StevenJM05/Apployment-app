package sv.edu.itca.apployment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.request.Request;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class Configuracion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuracion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    public void acerca(View view) {
        Intent acerca = new Intent(Configuracion.this, acerca.class);
        startActivity(acerca);
    }

    public void politicas(View view) {
        Intent politica = new Intent(Configuracion.this, politicas.class);
        startActivity(politica);
    }

    public void perfil(View view) {
        Intent profile = new Intent(Configuracion.this, ProfileFragment.class);
        startActivity(profile);

    }

        public void logout(Request request) {
            showProgressDialog("Cerrando sesión...");
            String url = "https://apployment.online/public/api/logout";
            AsyncHttpClient client = new AsyncHttpClient();

            // Assuming POST for logout
            client.post(url, null, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        System.exit(0);

                    } else {
                        // Handle unsuccessful logout
                        showErrorDialog("Error al cerrar sesión. Intenta nuevamente.");
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    // Handle network error
                    handleError(statusCode, responseBody, error);
                    showErrorDialog("Error al cerrar sesión. Verifica tu conexión a internet.");

                }
            });
        }

    private void handleError(int statusCode, byte[] responseBody, Throwable error) {
        // Implement error handling logic (e.g., display error message)
        System.out.println("Logout failed: Status code " + statusCode + ", Error: " + error);
        // You can also parse the responseBody for more details if available
    }
    private void showProgressDialog(String mensaje) {
        // Mostrar un ProgressDialog con el mensaje indicado
    }

    private void hideProgressDialog() {
        // Ocultar el ProgressDialog
    }

    private void showErrorDialog(String mensaje) {
        // Mostrar un AlertDialog con el mensaje de error
    }
}
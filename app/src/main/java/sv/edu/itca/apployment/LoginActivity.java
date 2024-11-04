package sv.edu.itca.apployment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button btnLogin;
    public String userEmail, userPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            userEmail = email.getText().toString().trim();
            userPass = password.getText().toString().trim();

            if (!userEmail.isEmpty() && !userPass.isEmpty()) {
                verificarDatos(userEmail, userPass);
            } else {
                MostrarMensaje("Por favor, complete los campos");
            }
        });
    }

    private void MostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void verificarDatos(String userEmail, String userPass) {
        String url = "https://apployment.online/public/api/users";
        RequestParams params = new RequestParams();
        params.put("email", userEmail);
        params.put("password", userPass);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    String respuesta = new String(responseBody);
                    try {
                        JSONObject jsonResponse = new JSONObject(respuesta);
                        if (jsonResponse.has("token")&& jsonResponse.has("role")) {
                            String token = jsonResponse.getString("token");
                            String role = jsonResponse.getString("role");
                            String userName = jsonResponse.getString("user_name");

                            MostrarMensaje("Bienvenido " + userName);

                            if (role.equals("trabajador")) {
                                Intent intent = new Intent(LoginActivity.this, TrabajadoresActivity.class);
                                intent.putExtra("token", token);
                                startActivity(intent);
                            } else if (role.equals("usuario")) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("token", token);
                                startActivity(intent);
                            }
                        } else {
                            MostrarMensaje("Error en el inicio de sesión");
                        }
                    } catch (JSONException e) {
                        MostrarMensaje("Error en la respuesta del servidor");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                MostrarMensaje("Error en la conexión");
            }
        });
    }

    public void OpenRegistrarse(View view) {
        Intent intento1 = new Intent(this, Registrarse.class);
        startActivity(intento1);
    }


}

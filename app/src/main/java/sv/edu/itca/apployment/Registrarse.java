package sv.edu.itca.apployment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;

public class Registrarse extends AppCompatActivity {

    private EditText etName, etEmail_regis, etPassword_regis, etConfirmPassword_regis;
    String name, email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrarse);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etName = findViewById(R.id.etName);
        etEmail_regis = findViewById(R.id.etEmail_regis);
        etPassword_regis = findViewById(R.id.etPassword_regis);
        etConfirmPassword_regis = findViewById(R.id.etConfirmPassword_regis);
    }

    public void OpenLogin(View view) {
        Intent inteno1 = new Intent(this, LoginActivity.class);
        startActivity(inteno1);
    }

    public void registrar(View view) {
        name = etName.getText().toString().trim();
        email = etEmail_regis.getText().toString().trim();
        password = etPassword_regis.getText().toString().trim();
        confirmPassword = etConfirmPassword_regis.getText().toString().trim();
        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
            if (password.equals(confirmPassword)) {
                Registrarusuario(name, email, password,confirmPassword);
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Campo vacío", Toast.LENGTH_SHORT).show();
        }
    }

    private void Registrarusuario(String name, String email, String password, String confirmPassword) {
        int rol = 2; // Puedes ajustar este valor según tu lógica
        String url = "https://apployment.online/public/api/register";
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);
        params.put("password_confirmation", confirmPassword);
        params.put("role_id", rol);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 201) { // Cambiado a 201 para creación exitosa
                    try {
                        String respuesta = new String(responseBody);
                        JSONObject jsonResponse = new JSONObject(respuesta);

                        if (jsonResponse.getBoolean("success")) {
                            String role = jsonResponse.getString("role");

                            JSONObject jsonUser = jsonResponse.optJSONObject("user");
                            String idUser = jsonUser.optString("id");
                            String nameUser = jsonUser.optString("name");


                            Toast.makeText(Registrarse.this, "Registro completado. Bienvenido " + nameUser, Toast.LENGTH_SHORT).show();
                            // Redirigir a la pantalla de inicio de sesión
                            Intent ini = new Intent(Registrarse.this, CreateProfileFirst.class);
                            ini.putExtra("idUser",idUser);
                            ini.putExtra("nameUser",nameUser);
                            startActivity(ini);
                        } else {
                            Toast.makeText(Registrarse.this, "No se pudo registrar, intentelo nuevamente", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(Registrarse.this, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(Registrarse.this, "Error en la conexión al servidor: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

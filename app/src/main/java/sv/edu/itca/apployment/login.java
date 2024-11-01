package sv.edu.itca.apployment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class login extends Fragment {

    private EditText email, password;
    private Button btnLogin;
    public String userEmail, userPass;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment login.
     */
    // TODO: Rename and change types and number of parameters
    public static login newInstance(String param1, String param2) {
        login fragment = new login();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void MostrarMensaje(String mensaje){
        Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        email = view.findViewById(R.id.etEmail);
        password = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v->{
            userEmail = email.getText().toString().trim();
            userPass = password.getText().toString().trim();

            if(!userEmail.isEmpty() && !userPass.isEmpty()){
                verificarDatos(userEmail, userPass);

            }else {
                MostrarMensaje("Por favor, complete los campos");
            }
        });

        return view;
    }

    private void verificarDatos(String userEmail, String userPass) {
        String url = "http://192.168.56.1/api/login";
        RequestParams params = new RequestParams();
        params.put("email", userEmail);
        params.put("password", userPass);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode==200){
                    String respuesta = new String(responseBody);
                    try{
                        JSONObject jsonResponse = new JSONObject(respuesta);
                        if(jsonResponse.has("token")){
                            String token = jsonResponse.getString("token");
                            String role = jsonResponse.getString("role");
                            String userName = jsonResponse.getString("user_name");

                            MostrarMensaje("Bienvenido" + userName);

                            if (role.equals("trabajador")){
                                MostrarMensaje("Eres trabajador");
                            }else if(role.equals("usuario")){
                                MostrarMensaje("Eres usuario");
                            }
                        }else {
                            MostrarMensaje("Error en el inicio de sesion");
                        }

                    }catch (JSONException e){
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
}
package sv.edu.itca.apployment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPublication#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPublication extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button Enviar;
    Spinner profileSpinner, professionSpinner;
    List<String> idList = new ArrayList<>();
    List<String> professionList = new ArrayList<>();


    public AddPublication() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPublication.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPublication newInstance(String param1, String param2) {
        AddPublication fragment = new AddPublication();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_publication, container, false);

        // Inicializa los Spinners
        professionSpinner = view.findViewById(R.id.professionSpinner);
        fetchProfilesAndProfessions();

        Button btnSubmit = view.findViewById(R.id.submitButton);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el ID de la profesión seleccionada
                String selectedProfessionId = getSelectedProfessionId();

                // Envía el ID a tu API o realiza la acción deseada
                enviarPublicacion(selectedProfessionId);
            }

        });
        return view;
    }

    private void fetchProfilesAndProfessions() {
        String urlProfessions = "https://apployment.online/public/api/profession";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlProfessions, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    try {
                        String responseString = new String(responseBody, "UTF-8");
                        JSONArray professionsArray = new JSONArray(responseString);

                        // Limpia las listas para evitar duplicados si el método es llamado más de una vez
                        professionList.clear();
                        idList.clear();

                        for (int i = 0; i < professionsArray.length(); i++) {
                            JSONObject profession = professionsArray.getJSONObject(i);
                            String idProfession = String.valueOf(profession.getInt("id"));
                            String nameProfession = profession.getString("name");

                            // Agregar nombre en professionList (para mostrar) y ID en idList (para enviar)
                            professionList.add(nameProfession);
                            idList.add(idProfession);
                        }

                        mostrarMensaje("Conexión exitosa");

                        // Configura el adaptador del Spinner con la lista de nombres
                        ArrayAdapter<String> professionAdapter = new ArrayAdapter<>(getContext(),
                                android.R.layout.simple_spinner_item, professionList);
                        professionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        professionSpinner.setAdapter(professionAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                mostrarMensaje("Error en la conexión");
            }
        });
    }

    private void enviarPublicacion(String selectedProfessionId) {
    }




    // Método para obtener el ID de la profesión seleccionada en el Spinner
    private String getSelectedProfessionId() {
        int position = professionSpinner.getSelectedItemPosition();
        return idList.get(position);  
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }


}
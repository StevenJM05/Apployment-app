package sv.edu.itca.apployment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sv.edu.itca.apployment.modelos.UserSession;

public class AddPublication extends Fragment {

    private Spinner professionSpinner;
    private EditText idProfileEditText;
    private EditText titleEditText, descriptionEditText, locationEditText, dateEditText, statusEditText;
    private Button submitButton;
    private List<String> idList = new ArrayList<>();
    private List<String> professionList = new ArrayList<>();
    private String userId;

    public AddPublication() {
        // Constructor vacío requerido
    }

    public static AddPublication newInstance(String param1, String param2) {
        AddPublication fragment = new AddPublication();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = UserSession.getInstance().getUserId();
        if (userId == null || userId.trim().isEmpty()) {
            mostrarMensaje("Error: no se pudo obtener el ID del usuario.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_publication, container, false);

        // Inicializa vistas
        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        locationEditText = view.findViewById(R.id.locationEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        statusEditText = view.findViewById(R.id.statusEditText);
        idProfileEditText = view.findViewById(R.id.IdDprofile);
        professionSpinner = view.findViewById(R.id.professionSpinner);
        submitButton = view.findViewById(R.id.submitButton);

        // Configura campos
        idProfileEditText.setText(userId);
        idProfileEditText.setEnabled(false);
        statusEditText.setText("1");

        // Configura selector de fecha
        dateEditText.setFocusable(false);
        dateEditText.setClickable(true);
        dateEditText.setOnClickListener(v -> showDatePicker());

        // Carga datos desde la API
        fetchProfilesAndProfessions();

        // Configura botón de envío
        submitButton.setOnClickListener(v -> handleSubmit());

        return view;
    }


    private void fetchProfilesAndProfessions() {
        String urlProfessions = "https://apployment.online/public/api/profession";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlProfessions, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    String respuesta = new String(responseBody);
                    JSONArray professionsArray = new JSONArray(respuesta);

                    // Limpia las listas
                    professionList.clear();
                    idList.clear();

                    for (int i = 0; i < professionsArray.length(); i++) {
                        JSONObject profession = professionsArray.getJSONObject(i);
                        if (profession.has("id") && profession.has("name")) {
                            idList.add(profession.getString("id"));
                            professionList.add(profession.getString("name"));
                        }
                    }

                    if (professionList.isEmpty()) {
                        mostrarMensaje("No se encontraron profesiones.");
                    } else {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                                android.R.layout.simple_spinner_item, professionList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        professionSpinner.setAdapter(adapter);
                        mostrarMensaje("Datos cargados correctamente.");
                    }
                } catch (JSONException e) {
                    mostrarMensaje("Error al procesar los datos.");
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                mostrarMensaje("Error de conexión al cargar profesiones.");
            }
        });
    }

    private void handleSubmit() {
        String selectedProfessionId = getSelectedProfessionId(); // Obtiene el ID de la profesión seleccionada
        if (selectedProfessionId == null) {
            mostrarMensaje("Selecciona una profesión válida.");
            return;
        }

        if (validateFields()) {
            sendPublication(selectedProfessionId); // Se pasa el ID de la profesión, no el nombre
        }
    }

    private boolean validateFields() {
        if (titleEditText.getText().toString().trim().isEmpty()) {
            mostrarMensaje("El título es obligatorio.");
            return false;
        }
        if (descriptionEditText.getText().toString().trim().isEmpty()) {
            mostrarMensaje("La descripción es obligatoria.");
            return false;
        }
        if (locationEditText.getText().toString().trim().isEmpty()) {
            mostrarMensaje("La ubicación es obligatoria.");
            return false;
        }
        if (dateEditText.getText().toString().trim().isEmpty()) {
            mostrarMensaje("La fecha es obligatoria.");
            return false;
        }
        if (statusEditText.getText().toString().trim().isEmpty()) {
            mostrarMensaje("El estado es obligatorio.");
            return false;
        }
        return true;
    }

    private void sendPublication(String professionId) {
        String url = "https://apployment.online/public/api/publications";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("title", titleEditText.getText().toString().trim());
        params.put("description", descriptionEditText.getText().toString().trim());
        params.put("location", locationEditText.getText().toString().trim());
        params.put("date", dateEditText.getText().toString().trim());
        params.put("status", statusEditText.getText().toString().trim());
        params.put("profile_id", idProfileEditText.getText().toString().trim());
        params.put("profession_id", professionId);


        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                mostrarMensaje("Publicación enviada con éxito.");
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                mostrarMensaje("Error al enviar la publicación.");
            }
    });
    }

    private String getSelectedProfessionId() {
        int position = professionSpinner.getSelectedItemPosition();
        if (position >= 0 && position < idList.size()) {
            return idList.get(position);  // Devuelve el ID de la profesión seleccionada
        }
        return null;
    }



    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                    dateEditText.setText(selectedDate);
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }
}

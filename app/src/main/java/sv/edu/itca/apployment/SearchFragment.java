package sv.edu.itca.apployment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import sv.edu.itca.apployment.adapter.ProfessionCardAdapter;

public class SearchFragment extends Fragment {
    private List<String> profesionList;
    private List<String> filteredList;
    private ProfessionCardAdapter adapter;
    private SearchView searchView;

    public SearchFragment() {
        // Constructor público requerido
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profesionList = new ArrayList<>();
        filteredList = new ArrayList<>();
        fetchAllProfesions(); // Cargar todos los registros al inicio
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.searchView); // ID del RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ProfessionCardAdapter(filteredList); // Usar el nuevo Adapter con Cards
        recyclerView.setAdapter(adapter);

        searchView = view.findViewById(R.id.buscarProfesion);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        return view;
    }

    private void fetchAllProfesions() {
        String url = "https://apployment.online/public/api/worker-profiles";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    String respuesta = new String(responseBody);
                    try {
                        JSONArray jsonArray = new JSONArray(respuesta);
                        profesionList.clear(); // Limpiar la lista antes de llenarla
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject profesion = jsonArray.getJSONObject(i);
                            String names = profesion.getString("names");
                            String lastName = profesion.getString("last_name");
                            String fullName = names + " " + lastName;

                            JSONArray professions = profesion.optJSONArray("professions");
                            String professionName = "";
                            if (professions != null && professions.length() > 0) {
                                JSONObject profession = professions.getJSONObject(0);
                                professionName = profession.optString("name", "");
                            }
                            String fullNameWithProfession = fullName + " - " + professionName;
                            profesionList.add(fullNameWithProfession);
                        }

                        // Mostrar todos los registros al inicio
                        filteredList.clear();
                        filteredList.addAll(profesionList);
                        adapter.notifyDataSetChanged();
                        mostrarMensaje("Datos cargados exitosamente");
                    } catch (JSONException e) {
                        mostrarMensaje("Error en la respuesta del servidor");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mostrarMensaje("Error al realizar la solicitud: " + (error != null ? error.getMessage() : "desconocido"));
            }
        });
    }

    private void filterList(String query) {
        filteredList.clear();
        if (TextUtils.isEmpty(query)) {
            filteredList.addAll(profesionList); // Mostrar todos los registros si la búsqueda está vacía
        } else {
            for (String item : profesionList) {
                if (item.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item); // Agregar solo las coincidencias
                }
            }
        }
        adapter.notifyDataSetChanged();
        if (filteredList.isEmpty()) {
            mostrarMensaje("Registro no encontrado");
        }
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }
}

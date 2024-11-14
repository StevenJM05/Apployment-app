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
import sv.edu.itca.apployment.adapter.WorkersAdapter;

public class SearchFragment extends Fragment implements WorkersAdapter.OnWorkerClickListener {
    private List<String> profesionList;
    private List<String> workersIds;
    private List<String> profession;
    private List<String> cities;
    private List<String> filteredProfesionList;
    private List<String> filteredWorkersIds;
    private List<String> filteredProfession;
    private List<String> filteredCities;
    private WorkersAdapter adapter;
    private SearchView searchView;

    public SearchFragment() {
        // Constructor público requerido
    }

    // Este método se llama cuando se hace clic en un trabajador
    @Override
    public void onWorkerClick(int workerId) {
        ProfileFragment profileFragment = ProfileFragment.newInstance(workerId);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, profileFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profesionList = new ArrayList<>();
        workersIds = new ArrayList<>();
        profession = new ArrayList<>();
        cities = new ArrayList<>();

        filteredProfesionList = new ArrayList<>();
        filteredWorkersIds = new ArrayList<>();
        filteredProfession = new ArrayList<>();
        filteredCities = new ArrayList<>();
        fetchAllProfesions(); // Cargar todos los registros al inicio
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.searchView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WorkersAdapter(filteredProfesionList, filteredWorkersIds, filteredProfession, filteredCities, getActivity(), this);
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
        String url = "https://apployment.online/public/api/workers-search";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    String respuesta = new String(responseBody);
                    try {
                        JSONObject jsonObject = new JSONObject(respuesta);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        profesionList.clear();
                        workersIds.clear();
                        profession.clear();
                        cities.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject worker = jsonArray.getJSONObject(i);
                            String names = worker.optString("names", "");
                            String lastName = worker.optString("last_name", "");
                            String fullName = names + " " + lastName;
                            profesionList.add(fullName);

                            String id = worker.optString("id", "");
                            workersIds.add(id);

                            String professionValue = worker.optString("profession", "N/A");
                            profession.add(professionValue);

                            String cityValue = worker.optString("city", "N/A");
                            cities.add(cityValue);
                        }

                        filteredProfesionList.addAll(profesionList);
                        filteredWorkersIds.addAll(workersIds);
                        filteredProfession.addAll(profession);
                        filteredCities.addAll(cities);

                        adapter.notifyDataSetChanged();
                        mostrarMensaje("Datos recibidos con éxito");

                    } catch (JSONException e) {
                        mostrarMensaje("Error en la respuesta del servidor");
                        e.printStackTrace();
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
        filteredProfesionList.clear();
        filteredWorkersIds.clear();
        filteredProfession.clear();
        filteredCities.clear();

        if (TextUtils.isEmpty(query)) {
            filteredProfesionList.addAll(profesionList);
            filteredWorkersIds.addAll(workersIds);
            filteredProfession.addAll(profession);
            filteredCities.addAll(cities);
        } else {
            for (int i = 0; i < profesionList.size(); i++) {
                if (profesionList.get(i).toLowerCase().contains(query.toLowerCase())) {
                    filteredProfesionList.add(profesionList.get(i));
                    filteredWorkersIds.add(workersIds.get(i));
                    filteredProfession.add(profession.get(i));
                    filteredCities.add(cities.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
        if (filteredProfesionList.isEmpty()) {
            mostrarMensaje("Registro no encontrado");
        }
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }
}

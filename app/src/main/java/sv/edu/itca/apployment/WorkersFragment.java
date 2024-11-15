package sv.edu.itca.apployment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
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
import sv.edu.itca.apployment.modelos.UserSession;

public class WorkersFragment extends Fragment implements WorkersAdapter.OnWorkerClickListener {
    private List<String> workersList;
    private List<String> workersIds;
    private List<String> profession;
    private List<String> cities;
    private WorkersAdapter adapter;




    public WorkersFragment() {
        // Constructor vacío
    }

    @Override
    public void onWorkerClick(int workerId) {
        openWorkerDetailFragment(workerId);
    }

    private void openWorkerDetailFragment(int workerId) {
        ProfileFragment detailFragment = new ProfileFragment();

        Bundle args = new Bundle();
        args.putInt("workerId", workerId);
        detailFragment.setArguments(args);

        // Reemplaza el fragmento actual con ProfileFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workersList = new ArrayList<>();
        workersIds = new ArrayList<>();
        profession = new ArrayList<>();
        cities = new ArrayList<>();
        fetchWorkers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workers, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewWorkers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new WorkersAdapter(workersList, workersIds, profession, cities, getContext(), this);
        recyclerView.setAdapter(adapter);

        // Configurar clics para cada CardView
        view.findViewById(R.id.carta1).setOnClickListener(v -> filterByProfession("Electricista"));
        view.findViewById(R.id.carta2).setOnClickListener(v -> filterByProfession("Carpintero"));
        view.findViewById(R.id.carta3).setOnClickListener(v -> filterByProfession("Mecánico"));
        view.findViewById(R.id.carta4).setOnClickListener(v -> filterByProfession("Albañil"));
        view.findViewById(R.id.carta5).setOnClickListener(v -> filterByProfession("Pintor"));
        view.findViewById(R.id.carta6).setOnClickListener(v -> filterByProfession("Soldador"));
        view.findViewById(R.id.carta7).setOnClickListener(v -> filterByProfession("Conductor"));

        return view;
    }

    private void filterByProfession(String selectedProfession) {
        List<String> filteredWorkers = new ArrayList<>();
        List<String> filteredIds = new ArrayList<>();
        List<String> filteredProfessions = new ArrayList<>();
        List<String> filteredCities = new ArrayList<>();

        for (int i = 0; i < workersList.size(); i++) {
            if (profession.get(i).equals(selectedProfession)) {
                filteredWorkers.add(workersList.get(i));
                filteredIds.add(workersIds.get(i));
                filteredProfessions.add(profession.get(i));
                filteredCities.add(cities.get(i));
            }
        }

        adapter.updateData(filteredWorkers, filteredIds, filteredProfessions, filteredCities);
    }

    private void fetchWorkers() {
        String url = "https://apployment.online/public/api/workers";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    String respuesta = new String(responseBody);
                    try {
                        JSONObject jsonObject = new JSONObject(respuesta);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        // Limpiar las listas antes de agregar nuevos datos
                        workersList.clear();
                        workersIds.clear();
                        profession.clear();
                        cities.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject worker = jsonArray.getJSONObject(i);

                            String names = worker.optString("names", "");
                            String lastName = worker.optString("last_name", "");
                            String fullName = names + " " + lastName;
                            workersList.add(fullName);

                            String id = worker.optString("id", "");
                            workersIds.add(id);

                            String professionValue = worker.optString("profession", "N/A");
                            profession.add(professionValue);

                            String cityValue = worker.optString("city", "N/A");
                            cities.add(cityValue);
                        }

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
                mostrarMensaje("Error en la conexión");
            }
        });
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }
}

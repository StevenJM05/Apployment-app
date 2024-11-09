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

public class WorkersFragment extends Fragment implements WorkersAdapter.OnWorkerClickListener{
    private List<String> workersList;  // Lista para nombres completos
    private List<String> workersIds;   // Lista para IDs de trabajadores
    private WorkersAdapter adapter;

    public WorkersFragment() {
        // Constructor público vacío requerido
    }

    @Override
    public void onWorkerClick(int workerId) {
        ProfileFragment profileFragment = ProfileFragment.newInstance(workerId);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, profileFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openWorkerDetailFragment(int workerId) {
        ProfileFragment detailFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt("workerId", workerId);
        detailFragment.setArguments(args);

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
        fetchWorkers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workers, container, false);

        // Inicializar RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewWorkers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Inicializar el adaptador con ambas listas
        adapter = new WorkersAdapter(workersList, workersIds, getContext(), this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void fetchWorkers() {
        String url = "https://apployment.online/public/api/worker-profiles";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    String respuesta = new String(responseBody);
                    try {
                        JSONArray jsonArray = new JSONArray(respuesta);

                        // Limpiar listas antes de llenarlas
                        workersList.clear();
                        workersIds.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject worker = jsonArray.getJSONObject(i);

                            // Obtener nombres completos
                            String names = worker.getString("names");
                            String lastName = worker.getString("last_name");
                            String fullName = names + " " + lastName;


                            // Obtener ID y agregar a la lista de IDs
                            String id = worker.getString("id");

                            JSONArray professions = worker.optJSONArray("professions");
                            String professionName = "";
                            if (professions != null && professions.length() > 0) {
                                JSONObject profession = professions.getJSONObject(0);
                                professionName = profession.optString("name", "");
                            }
                            workersList.add(fullName + " " + professionName);
                            workersIds.add(id);
                        }

                        adapter.notifyDataSetChanged();
                        mostrarMensaje("Datos recibidos con éxito");
                    } catch (JSONException e) {
                        mostrarMensaje("Error en la respuesta del servidor");
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

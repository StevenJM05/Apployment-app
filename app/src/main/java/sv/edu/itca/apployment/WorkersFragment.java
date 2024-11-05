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

public class WorkersFragment extends Fragment {
    private List<String> workersList;
    private WorkersAdapter adapter;

    public WorkersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workersList = new ArrayList<>();
        fetchWorkers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workers, container, false);

        // Inicializar RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewWorkers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WorkersAdapter(workersList, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void fetchWorkers() {
        String url = "https://apployment.online/public/api/profile";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    String respuesta = new String(responseBody);
                    try {
                        JSONArray jsonArray = new JSONArray(respuesta);
                        workersList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject worker = jsonArray.getJSONObject(i);
                            String names = worker.getString("names");
                            String lastName = worker.getString("last_name");
                            String fullName = names + " " + lastName;
                            workersList.add(fullName);
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

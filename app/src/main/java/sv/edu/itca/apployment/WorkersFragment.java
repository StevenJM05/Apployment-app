package sv.edu.itca.apployment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import sv.edu.itca.apployment.adapter.WorkersAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listView;
    //private ArrayAdapter<String> adapter;
    private List<String> workersList;
    private WorkersAdapter adapter;


    public WorkersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkersFragment newInstance(String param1, String param2) {
        WorkersFragment fragment = new WorkersFragment();
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
        workersList = new ArrayList<>();
        fetchWorkers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workers, container, false);

        // Inicializar RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewWorkers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WorkersAdapter(workersList);

        recyclerView.setAdapter(adapter); // Establecer el adaptador

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
                        workersList.clear(); // Limpiar la lista antes de llenarla
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject worker = jsonArray.getJSONObject(i);
                            String names = worker.getString("names");
                            String lastName = worker.getString("last_name");
                            String fullName = names + " " + lastName;
                            workersList.add(fullName);
                        }
                        adapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
                        mostrarMensaje("DATOS RECIBIDOS CON ÉXITO");
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
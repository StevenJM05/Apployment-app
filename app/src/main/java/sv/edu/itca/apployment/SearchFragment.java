package sv.edu.itca.apployment;
import sv.edu.itca.apployment.adapter.WorkersAdapterr;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listView;
    private String profesionname="";
    //private ArrayAdapter<String> adapter;
    private List<String> profesionList;
    private WorkersAdapterr adapter;
    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        profesionList = new ArrayList<>();
        fetchProfesion();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        RecyclerView recyclerView=view.findViewById(R.id.searchView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new WorkersAdapterr(profesionList);
        return view;

    }



    private void fetchProfesion() {
        String url = "https://apployment.online/public/api/worker-profiles?query="+profesionname;
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
                                JSONObject profession = professions.getJSONObject(0);  // Tomamos el primer objeto del array
                                professionName = profession.optString("name", "");  // Optamos por un valor vacío si no existe la clave
                            }
                            String fullNameWithProfession = fullName + " - " + professionName;
                            profesionList.add(fullNameWithProfession);
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

            }
        });
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }
}
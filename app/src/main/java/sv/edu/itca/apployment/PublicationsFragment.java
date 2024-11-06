package sv.edu.itca.apployment;

import android.os.Bundle;
import android.util.Log;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import sv.edu.itca.apployment.adapter.ProfessionAdapter;

public class PublicationsFragment extends Fragment {

    private List<String> publicationList;
    private ProfessionAdapter adapter;

    public PublicationsFragment() {
        // Constructor vacío requerido
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        publicationList = new ArrayList<>();
        fetchPublications();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publications, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.publicationsView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ProfessionAdapter(publicationList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void fetchPublications() {
        String url = "https://apployment.online/public/api/publications";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    try {
                        String responseString = new String(responseBody, "UTF-8");
                        Log.d("PublicationsFragment", "Respuesta JSON: " + responseString);

                        JSONObject jsonResponse = new JSONObject(responseString);
                        JSONArray dataArray = jsonResponse.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject publication = dataArray.getJSONObject(i);
                            int id = publication.getInt("id");
                            String title = publication.getString("title");
                            String description = publication.getString("description");

                            String valor = id + " " + title + " " + description;
                            publicationList.add(valor);
                        }

                        adapter.notifyDataSetChanged();
                        mostrarMensaje("DATOS RECIBIDOS CON ÉXITO");

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("PublicationsFragment", "Error al procesar el JSON", e);
                        mostrarMensaje("Error al procesar el JSON");
                    }
                } else {
                    mostrarMensaje("Código de estado inesperado: " + statusCode);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mostrarMensaje("Error en la conexión de Publicaciones: " + error.getMessage());
                Log.e("PublicationsFragment", "Error en la conexión", error);
            }
        });
    }


    private void mostrarMensaje(String mensaje) {
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }
}

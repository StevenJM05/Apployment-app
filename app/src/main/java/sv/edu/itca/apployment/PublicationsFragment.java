package sv.edu.itca.apployment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import sv.edu.itca.apployment.adapter.PublicationAdapter;

public class PublicationsFragment extends Fragment implements PublicationAdapter.OnPublicationClickListener {
private Button agregarPublicacion;

    private List<String> publicationList;
    private List<String> publicationIdList;
    private List<String> titulosList;
    private List<String> descripcionesList;
    private List<String> fechasList;
    private List<String> usersnamesList;

    private PublicationAdapter adapter;

    public void onPublicationClick(int publicationId) {
        ProfileFragment profileFragment = ProfileFragment.newInstance(publicationId);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, profileFragment)
                .addToBackStack(null)
                .commit();
    }

    public PublicationsFragment() {
        // Constructor vacío requerido
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        publicationList = new ArrayList<>();
        publicationIdList = new ArrayList<>();
        titulosList = new ArrayList<>();
        descripcionesList = new ArrayList<>();
        fechasList = new ArrayList<>();
        usersnamesList = new ArrayList<>();
        fetchPublications();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publications, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.publicationsView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Usa el PublicationAdapter y pasa la interfaz del fragmento
        adapter = new PublicationAdapter(getContext(), publicationList, publicationIdList, titulosList, descripcionesList, fechasList, usersnamesList, this);
        recyclerView.setAdapter(adapter);

        agregarPublicacion = view.findViewById(R.id.btn_add_publication);
        agregarPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPublication();
            }
        });


        return view;
    }

    private void addPublication() {
        AddPublication adddpub = new AddPublication(); // Cambia 'agregarnewPublicacion' al nombre real de tu clase de fragmento
        // Inicia la transacción para reemplazar el fragmento actual
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, adddpub) // Asegúrate de que `fragment_container` sea el contenedor de fragmentos correcto en tu layout
                .addToBackStack(null) // Permite regresar al fragmento anterior al presionar "Atrás"
                .commit();


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
                        publicationList.clear();
                        publicationIdList.clear();
                        JSONArray dataArray = jsonResponse.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject publication = dataArray.getJSONObject(i);
                            int id = publication.getInt("id");
                            String title = publication.getString("title");
                            String description = publication.getString("description");
                            String date = publication.getString("date");

                            JSONObject usuario = publication.getJSONObject("profile");
                            String firstName = usuario.getString("names");
                            String lastName = usuario.getString("last_name");
                            String name = firstName + " " + lastName;

                            publicationIdList.add(String.valueOf(id));
                            titulosList.add((title));
                            descripcionesList.add((description));
                            fechasList.add((date));
                            usersnamesList.add((name));

                            String valor =id+" PUBLICACIÓN DE: " +name+ " "+ " TITULO: "+title+ " HECHA EL DIA: " +date;
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

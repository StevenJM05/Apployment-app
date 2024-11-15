package sv.edu.itca.apployment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cz.msebera.android.httpclient.Header;
import sv.edu.itca.apployment.adapter.SkillAdapter;
import sv.edu.itca.apployment.modelos.UserSession;

public class ProfileFragment extends Fragment {
    private TextView nameTextView, professionTextView, locationTextView, genderTextView, ageTextView, emailTextView, phoneTextView, addressTextView;
    private ImageView profileImageView;
    private RecyclerView skillsRecyclerView;
    private SkillAdapter skillAdapter;
    private List<String> skillList = new ArrayList<>();
    private int workerId;
    private String userId;
    private String another_user_id;


    // Método para crear una nueva instancia de ProfileFragment con el workerId
    public static ProfileFragment newInstance(int workerId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt("workerId", workerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            workerId = getArguments().getInt("workerId");
        }
        userId = UserSession.getInstance().getUserId();
        Toast.makeText(getContext(), "User ID: " + userId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inicializa RecyclerView para las habilidades
        skillsRecyclerView = view.findViewById(R.id.skills_recycler_view);
        skillsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        skillAdapter = new SkillAdapter(skillList);
        skillsRecyclerView.setAdapter(skillAdapter);

        // Referencias a las vistas
        nameTextView = view.findViewById(R.id.profile_name);
        professionTextView = view.findViewById(R.id.profile_profession);
        locationTextView = view.findViewById(R.id.profile_location);
        genderTextView = view.findViewById(R.id.genero);
        ageTextView = view.findViewById(R.id.next_followup);
        emailTextView = view.findViewById(R.id.email_profile);
        phoneTextView = view.findViewById(R.id.phone_number);
        addressTextView = view.findViewById(R.id.direccion);
        profileImageView = view.findViewById(R.id.profile_image);
        Button sendMessageButton = view.findViewById(R.id.send_message_button);
        sendMessageButton.setOnClickListener(v -> checkOrCreateConversation());

        fetchProfileData();

        return view;
    }

    // Método para obtener los datos del perfil desde la API
    private void fetchProfileData() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://apployment.online/public/api/worker-profiles/" + workerId;
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String fullName = response.getString("names") + " " + response.getString("last_name");
                    String birthdate = response.getString("birthdate");
                    String gender = response.getString("gender");
                    String profileImage = "https://apployment.online/public/storage/" + response.getString("photo");
                    JSONObject contact = response.getJSONObject("contact");
                    String profession = response.getJSONArray("professions").getJSONObject(0).getString("name");

                    // Calcula la edad
                    int birthYear = Integer.parseInt(birthdate.substring(0, 4));
                    int age = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) - birthYear;

                    nameTextView.setText(fullName);
                    professionTextView.setText(profession);
                    locationTextView.setText(contact.getString("city"));
                    genderTextView.setText(gender);
                    ageTextView.setText(String.valueOf(age));
                    emailTextView.setText(contact.getString("email"));
                    phoneTextView.setText(contact.getString("phone"));
                    addressTextView.setText(contact.getString("address"));
                    Picasso.get().load(profileImage).into(profileImageView);
                    another_user_id = response.getJSONObject("user").getString("id");

                    // Cargar habilidades
                    skillList.clear();
                    JSONArray skillsArray = response.getJSONArray("skills");
                    for (int i = 0; i < skillsArray.length(); i++) {
                        skillList.add(skillsArray.getJSONObject(i).getString("name"));
                    }
                    skillAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                throwable.printStackTrace();
            }
        });
    }

    // Verifica si ya existe una conversación y, si no, crea una nueva
    private void checkOrCreateConversation() {
        AsyncHttpClient client = new AsyncHttpClient();
        String checkUrl = "https://apployment.online/public/api/check_conversation/" + another_user_id;
        client.post(checkUrl, new RequestParams("user_id", userId), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean exists = response.getBoolean("exists");
                    int conversationId = response.getInt("conversation_id");
                    if (exists) {
                        Toast.makeText(getContext(), "Conversación existente. ID: " + conversationId, Toast.LENGTH_SHORT).show();
                    } else {
                        createConversation();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                throwable.printStackTrace();
            }
        });
    }

    // Método para crear una nueva conversación
    private void createConversation() {
        AsyncHttpClient client = new AsyncHttpClient();
        String createUrl = "https://apployment.online/public/api/create_conversation";
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("user_id_worker", another_user_id);

        client.post(createUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int conversationId = response.getInt("conversation_id");
                    Toast.makeText(getContext(), "Nueva conversación creada. ID: " + conversationId, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                throwable.printStackTrace();
            }
        });
    }

    // Setter para el userId
    public void setUserId(String userId) {
        this.userId = userId;
    }
}

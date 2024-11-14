    package sv.edu.itca.apployment;

    import android.os.Bundle;

    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.RecyclerView;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import com.squareup.picasso.Picasso;

    import com.loopj.android.http.AsyncHttpClient;
    import com.loopj.android.http.JsonHttpResponseHandler;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.ArrayList;
    import java.util.List;

    import cz.msebera.android.httpclient.Header;
    import sv.edu.itca.apployment.adapter.SkillAdapter;

    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link ProfileFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public class ProfileFragment extends Fragment {

        private TextView nameTextView, professionTextView, locationTextView, genderTextView, ageTextView, emailTextView, phoneTextView, addressTextView;
        private ImageView profileImageView;

        // Agrega esto a tus variables de instancia
        private RecyclerView skillsRecyclerView;
        private SkillAdapter skillAdapter;
        private List<String> skillList = new ArrayList<>();

        private static final String ARG_WORKER_ID = "workerId";
        private int workerId;


        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        public ProfileFragment() {

        }

        public static ProfileFragment newInstance(int workerId) {
            ProfileFragment fragment = new ProfileFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_WORKER_ID, workerId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                workerId = getArguments().getInt(ARG_WORKER_ID);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_profile, container, false);
            // Inicializa el RecyclerView
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

            fetchProfileData();

            return view;
        }

        private void fetchProfileData() {
            AsyncHttpClient client = new AsyncHttpClient();
            String url = "https://apployment.online/public/api/worker-profiles/" + workerId;
            client.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        String firstName = response.getString("names");
                        String lastName = response.getString("last_name");
                        String fullName = firstName + " " + lastName;
                        String birthdate = response.getString("birthdate");
                        String gender = response.getString("gender");

                        // Construye la URL completa de la imagen
                        String profileImage = response.getString("photo");
                        String imageUrl = "https://apployment.online/public/storage/" + profileImage;

                        // Otros datos de contacto
                        JSONObject contact = response.getJSONObject("contact");
                        String email = contact.getString("email");
                        String phone = contact.getString("phone");
                        String address = contact.getString("address");

                        JSONArray professions = response.getJSONArray("professions");
                        String profession = professions.getJSONObject(0).getString("name");

                        // Calcular la edad
                        int birthYear = Integer.parseInt(birthdate.substring(0, 4));
                        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                        int age = currentYear - birthYear;

                        // Actualizar las vistas
                        nameTextView.setText(fullName);
                        professionTextView.setText(profession);
                        locationTextView.setText(contact.getString("city"));
                        genderTextView.setText(gender);
                        ageTextView.setText(String.valueOf(age));
                        emailTextView.setText(email);
                        phoneTextView.setText(phone);
                        addressTextView.setText(address);

                        // Cargar la imagen usando Picasso
                        Picasso.get().load(imageUrl).into(profileImageView);

                        // Procesar y mostrar las habilidades
                        JSONArray skillsArray = response.getJSONArray("skills");
                        skillList.clear();
                        for (int i = 0; i < skillsArray.length(); i++) {
                            JSONObject skillObject = skillsArray.getJSONObject(i);
                            String skillName = skillObject.getString("name");
                            skillList.add(skillName);
                        }
                        skillAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    throwable.printStackTrace();
                }
            });
        }
    }
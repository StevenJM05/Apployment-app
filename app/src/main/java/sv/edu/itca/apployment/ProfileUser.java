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
    import com.squareup.picasso.Picasso;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.ArrayList;
    import java.util.List;

    import cz.msebera.android.httpclient.Header;
    import sv.edu.itca.apployment.adapter.SkillAdapter;
    import sv.edu.itca.apployment.modelos.UserSession;

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link ProfileUser#newInstance} factory method to
     * create an instance of this fragment.
     */
    public class ProfileUser extends Fragment {

        private TextView nameTextView, professionTextView, locationTextView, genderTextView, ageTextView, emailTextView, phoneTextView, addressTextView;
        private ImageView profileImageView;
        private RecyclerView skillsRecyclerView;
        private SkillAdapter skillAdapter;
        private List<String> skillList = new ArrayList<>();
        private String userId;

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        public ProfileUser() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileUser.
         */
        // TODO: Rename and change types and number of parameters
        public static ProfileUser newInstance(String param1, String param2) {
            ProfileUser fragment = new ProfileUser();
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

            userId = UserSession.getInstance().getUserId();
            Toast.makeText(getContext(), "User ID: " + userId, Toast.LENGTH_SHORT).show();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_profile_user, container, false);

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


            fetchProfileData();

            return view;
        }
        private void fetchProfileData() {
            AsyncHttpClient client = new AsyncHttpClient();
            String url = "https://apployment.online/public/api/worker-profile/" + userId;
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
    }
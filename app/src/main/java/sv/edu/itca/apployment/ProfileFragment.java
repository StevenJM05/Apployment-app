package sv.edu.itca.apployment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private TextView nameTextView, professionTextView, locationTextView, genderTextView, ageTextView, emailTextView, phoneTextView, addressTextView;
    private ImageView profileImageView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
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
        client.get("https://apployment.online/public/api/worker-profiles/1", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String firstName = response.getString("names");
                    String lastName = response.getString("last_name");
                    String fullName = firstName + " " + lastName;
                    String birthdate = response.getString("birthdate");
                    String gender = response.getString("gender");
                    String profileImage = response.getString("photo");

                    JSONObject contact = response.getJSONObject("contact");
                    String email = contact.getString("email");
                    String phone = contact.getString("phone");
                    String address = contact.getString("address");

                    JSONArray professions = response.getJSONArray("professions");
                    String profession = ((JSONArray) professions).getJSONObject(0).getString("name");

                    int birthYear = Integer.parseInt(birthdate.substring(0, 4));
                    int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                    int age = currentYear - birthYear;

                    nameTextView.setText(fullName);
                    professionTextView.setText(profession);
                    locationTextView.setText(contact.getString("city"));
                    genderTextView.setText(gender);
                    ageTextView.setText(String.valueOf(age));
                    emailTextView.setText(email);
                    phoneTextView.setText(phone);
                    addressTextView.setText(address);
                    Picasso.get().load("https://apployment.online/public/" + profileImage).into(profileImageView);
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
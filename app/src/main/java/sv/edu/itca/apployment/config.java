package sv.edu.itca.apployment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class config extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public config() {
        // Required empty public constructor
    }

    public static config newInstance(String param1, String param2) {
        config fragment = new config();
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
        View view = inflater.inflate(R.layout.fragment_config, container, false);

        // Inicializar los botones y asignar los listeners
        Button btn_account = view.findViewById(R.id.btn_account);
        Button btn_about_us = view.findViewById(R.id.btn_about_us);
        Button btn_who_we_are = view.findViewById(R.id.btn_who_we_are);
        Button btn_privacy_policy = view.findViewById(R.id.btn_privacy_policy);

        btn_about_us.setOnClickListener(v -> navigateToFragment(new acercade()));
        btn_privacy_policy.setOnClickListener(v -> navigateToFragment(new politics()));
        btn_who_we_are.setOnClickListener(v -> navigateToFragment(new quienes()));
        btn_account.setOnClickListener(v -> navigateToFragment(new ProfileFragment()));

        return view;
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

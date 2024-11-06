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
        Button button2 = view.findViewById(R.id.button2);
        Button button1 = view.findViewById(R.id.button1);
        Button button3 = view.findViewById(R.id.button3);
        Button button = view.findViewById(R.id.button);

        button2.setOnClickListener(v -> navigateToFragment(new acercade()));
        button3.setOnClickListener(v -> navigateToFragment(new politics()));
        button1.setOnClickListener(v -> navigateToFragment(new quienes()));
        button.setOnClickListener(v -> navigateToFragment(new ProfileFragment()));

        return view;
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

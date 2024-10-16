package sv.edu.itca.apployment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workers, container, false);

        // Configuración de cada CardView
        int[] cardViewIds = {
                R.id.carta1, R.id.carta2, R.id.carta3, R.id.carta4, R.id.carta5
        };

        for (int id : cardViewIds) {
            CardView cardView = view.findViewById(id);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openWorkersFragment();
                    Toast.makeText(getActivity(), "Clic en la carta", Toast.LENGTH_SHORT).show();
                }
            });
        }


        //ICONOS DEL PERFIL Y NOTIFICACIONES
        ImageView icononotificaciones = view.findViewById(R.id.icononotificaciones);
        ImageView imagenperfil= view.findViewById(R.id.imagenperfil);

        //Para que el icono de notificaciones pueda darse clic
        icononotificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Notificaciones Clicadas", Toast.LENGTH_SHORT).show();
            }
        });

        //Para que el icono del perfil pueda darse clic
        imagenperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Perfil Clicado", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    // Método para abrir WorkersFragment
    private void openWorkersFragment() {
        WorkersFragment workersFragment = new WorkersFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, workersFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
        // Inflate the layout for this fragment
       /* View view = inflater.inflate(R.layout.fragment_workers, container, false);

        CardView cardView = view.findViewById(R.id.carta1);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkersFragment workersFragment = new WorkersFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, workersFragment);
                transaction.addToBackStack(null);
                transaction.commit();;
            }
        });

        CardView cardView2 = view.findViewById(R.id.carta2);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkersFragment workersFragment = new WorkersFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, workersFragment);
                transaction.addToBackStack(null);
                transaction.commit();;
            }
        });

        CardView cardView3 = view.findViewById(R.id.carta3);
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkersFragment workersFragment = new WorkersFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, workersFragment);
                transaction.addToBackStack(null);
                transaction.commit();;
            }
        });

        CardView cardView4 = view.findViewById(R.id.carta4);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkersFragment workersFragment = new WorkersFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, workersFragment);
                transaction.addToBackStack(null);
                transaction.commit();;
            }
        });

        CardView cardView5 = view.findViewById(R.id.carta5);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkersFragment workersFragment = new WorkersFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, workersFragment);
                transaction.addToBackStack(null);
                transaction.commit();;
            }
        });
        return view;*/


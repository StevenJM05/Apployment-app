package sv.edu.itca.apployment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Screenapployment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Screenapployment extends Fragment {

    private static int tiempoCarga=2000;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Screenapployment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Screenapployment newInstance(String param1, String param2) {
        Screenapployment fragment = new Screenapployment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Screenapployment.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            private void finish() {
            }
        }, tiempoCarga);*/
    }


        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_screenapployment, container, false);


        }
    }
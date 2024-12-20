package sv.edu.itca.apployment;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String userid = getIntent().getStringExtra("userId");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new WorkersFragment())
                .commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.navigation_trabajadores) {
                    selectedFragment = new WorkersFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", userid);
                    selectedFragment.setArguments(bundle);
                } else if (item.getItemId() == R.id.navigation_publicaciones) {
                    selectedFragment = new PublicationsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", userid);
                } else if (item.getItemId() == R.id.navigation_buscar) {
                    selectedFragment = new SearchFragment();
                } else if (item.getItemId() == R.id.navigation_message) {
                    selectedFragment = new ChatFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", userid);
                    selectedFragment.setArguments(bundle);
                }
                else if (item.getItemId() == R.id.navigation_perfil) {
                    selectedFragment = new config();

                    }
                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, selectedFragment)
                                .commit();
                    }
                    return true;
                }

        });

    }
}
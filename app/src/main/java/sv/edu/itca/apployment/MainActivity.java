package sv.edu.itca.apployment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                Intent ventana = null;
                if(item.getItemId()== R.id.navigation_trabajadores){
                    ventana = new Intent(MainActivity.this, activity_trabajadores.class);
                    startActivity(ventana);
                }
                if(item.getItemId()==R.id.navigation_publicaciones){
                    ventana = new Intent(MainActivity.this, activity_publicaciones.class);
                    startActivity(ventana);
                }
                if(item.getItemId()==R.id.navigation_buscar){
                    ventana = new Intent(MainActivity.this, activity_buscar.class);
                    startActivity(ventana);
                }
                if(item.getItemId()==R.id.navigation_perfil){
                    ventana  = new Intent(MainActivity.this, activity_perfil.class);
                    startActivity(ventana);
                }
            }
        });
    }
}
package sv.edu.itca.apployment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class CreateProfileFirst extends AppCompatActivity {
    private EditText names, lastnames, userid, birthdateEditText;
    private TextView saludo;
    private Spinner gender;
    private Button selectPhotoButton;
    private Button selectCamaraButton;
    private ImageView photoImageView;

    private static final int PICK_IMAGE = 1;
    private static final int CAPTURE_IMAGE = 2;

    // Request codes for permissions
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 102;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile_first);

        // Initialize views
        names = findViewById(R.id.names);
        lastnames = findViewById(R.id.last_name);
        userid = findViewById(R.id.user_id);
        gender = findViewById(R.id.gender);
        saludo = findViewById(R.id.SaludoRegistrar);
        selectPhotoButton = findViewById(R.id.selectPhotoButton);
        photoImageView = findViewById(R.id.photoImageView);
        selectCamaraButton = findViewById(R.id.selectCameraButton);
        birthdateEditText = findViewById(R.id.birthdate);

        // DatePicker setup
        birthdateEditText.setFocusable(false);
        birthdateEditText.setClickable(true);
        birthdateEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year1, month1, dayOfMonth) -> {
                        String date = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                        birthdateEditText.setText(date);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        Intent intent = getIntent();
        String IdUser = intent.getStringExtra("idUser");
        String nameUser = intent.getStringExtra("nameUser");
        if (nameUser != null && !nameUser.isEmpty()) {
            saludo.setText("Bienvenido " + nameUser + ", tranquilo ya casi acabamos");
        } else {
            saludo.setText("Bienvenido, tranquilo ya casi acabamos");
        }

        if (userid.getText().toString().isEmpty() && IdUser != null) {
            userid.setText(IdUser);
        }

        selectPhotoButton.setOnClickListener(v -> openGallery());
        selectCamaraButton.setOnClickListener(v -> openCamera());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void openCamera() {
        if (checkCameraPermission()) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, CAPTURE_IMAGE);
            }
        } else {
            requestCameraPermission();
        }
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
                break;
            case STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    photoImageView.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAPTURE_IMAGE) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                photoImageView.setImageBitmap(photo);
            }
        }
    }
}

package com.adi.logsingsql;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.adi.logsingsql.databinding.ActivitySignupBinding;
import com.adi.logsingsql.DatabaseHelper;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextAlamat;
    private EditText editTextTelepon;
    private EditText editTextNumber;
    private Button button;
    private Spinner spinner;
    private Spinner spinner2;
    private EditText editTextDateOfBirth;
    private DatePickerDialog datePickerDialog;
    ActivitySignupBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);
        editTextEmail = findViewById(R.id.signup_email);
        editTextName = findViewById(R.id.signup_nama);
        editTextUsername = findViewById(R.id.signup_username);
        editTextAlamat = findViewById(R.id.signup_alamat);
        editTextTelepon = findViewById(R.id.signup_Hp);
        editTextPassword = findViewById(R.id.signup_password);
        editTextNumber = findViewById(R.id.signup_confirm);
        button = findViewById(R.id.signup_button);
        spinner2 = findViewById(R.id.gender);
        spinner = findViewById(R.id.agama);
        editTextDateOfBirth = findViewById(R.id.signup_tanggal);

        // Mengambil nilai nama dan email dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedName = sharedPreferences.getString("name", "");
        String savedEmail = sharedPreferences.getString("email", "");

        editTextName.setText(savedName);
        editTextEmail.setText(savedEmail);

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextNumber.getText().toString();
                String name = editTextName.getText().toString();
                String username = editTextUsername.getText().toString();
                String alamat = editTextAlamat.getText().toString();
                String telepon = editTextTelepon.getText().toString();
                String tanggalLahir = editTextDateOfBirth.getText().toString();
                String jenisKelamin = spinner2.getSelectedItem().toString();
                String agama = spinner.getSelectedItem().toString();

                if (email.equals("") || password.equals("") || confirmPassword.equals("") || name.equals("") || username.equals("") || alamat.equals("") || telepon.equals("") || tanggalLahir.equals("")) {
                    Toast.makeText(SignupActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals(confirmPassword)) {
                        Boolean checkUserEmail = databaseHelper.checkEmail(email);

                        if (!checkUserEmail) {
                            // Insert data into the SQLite database
                            Boolean insert = databaseHelper.insertData(email, password, name, username, alamat, telepon, tanggalLahir, jenisKelamin, agama);

                            if (insert) {
                                Toast.makeText(SignupActivity.this, "Signup Successfully!", Toast.LENGTH_SHORT).show();

                                // Simpan preferensi (nama dan email) saat pengguna mendaftar
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("name", name);
                                editor.putString("email", email);
                                editor.apply();

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignupActivity.this, "Signup Failed!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignupActivity.this, "User already exists! Please login", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "Invalid Password!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // GET DATE
        editTextDateOfBirth.setInputType(InputType.TYPE_NULL);
        editTextDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(SignupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Set the selected date in the EditText
                        editTextDateOfBirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Inisialisasi Spinner untuk pilihan jenis kelamin
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.add("Laki-Laki");
        adapter.add("Perempuan");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);

        adapter = new ArrayAdapter<>(this, android.R

                .layout.simple_spinner_item);
        adapter.add("Islam");
        adapter.add("Kristen");
        adapter.add("Katolik");
        adapter.add("Hindu");
        adapter.add("Bhudha");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
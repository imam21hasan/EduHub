package com.example.eduhub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupAdminActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword,etPhone;
    private Button btnSignup;
    private TextView tvTitle, tvSignup;
    private ProgressDialog dialog;
    private DatabaseReference reference;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_admin);

        reference = FirebaseDatabase.getInstance().getReference("UserSignUp");

        role = getIntent().getStringExtra("ROLE");

        tvTitle = findViewById(R.id.tvTitleSignup);
        tvTitle.setText(role.substring(0, 1).toUpperCase() + role.substring(1) + " Signup");

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmailSignup);
        etPassword = findViewById(R.id.etPasswordSignup);
        etPhone=findViewById(R.id.etPhoneSignup);
        btnSignup = findViewById(R.id.btnSignup);
        tvSignup = findViewById(R.id.tvSignup);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        btnSignup.setOnClickListener(v -> createAccount());

        tvSignup.setOnClickListener(v -> {
            Intent intent = new Intent(SignupAdminActivity.this, LoginActivity.class);
            intent.putExtra("ROLE", role);
            startActivity(intent);
            finish();
        });
    }

    private void createAccount() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(pass) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
            return;
        }

        dialog.setMessage("Creating account...");
        dialog.show();

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("password", pass);
        userMap.put("phone", phone);
        userMap.put("role", role);


        reference.push().setValue(userMap).addOnCompleteListener(task -> {
            dialog.dismiss();
            if (task.isSuccessful()) {
                Toast.makeText(this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupAdminActivity.this, LoginActivity.class);
                intent.putExtra("ROLE", role);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Sign up not Successful", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

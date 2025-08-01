package com.example.eduhub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignupTcrActivity extends AppCompatActivity {

    private EditText etName, etEmail,etDesig, etPassword, etDept, etPhone;
    private Button btnSignup;
    private TextView tvTitle, tvSignup;
    private ProgressDialog dialog;
    private DatabaseReference reference;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_tcr);

        reference = FirebaseDatabase.getInstance().getReference("UserSignUp");
        role = getIntent().getStringExtra("ROLE");

        tvTitle = findViewById(R.id.tvTitleSignup);
        tvTitle.setText(role.substring(0, 1).toUpperCase() + role.substring(1) + " Signup");

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmailSignup);
        etDesig = findViewById(R.id.etDesignation);
        etDept = findViewById(R.id.etDepartment);
        etPassword = findViewById(R.id.etPasswordSignup);
        etPhone = findViewById(R.id.etPhoneSignup);
        btnSignup = findViewById(R.id.btnSignup);
        tvSignup = findViewById(R.id.tvSignup);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        btnSignup.setOnClickListener(v -> createAccount());

        tvSignup.setOnClickListener(v -> {
            Intent intent = new Intent(SignupTcrActivity.this, LoginActivity.class);
            intent.putExtra("ROLE", role);
            startActivity(intent);
            finish();
        });
    }

    private void createAccount() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String desig = etDesig.getText().toString().trim();
        String dept = etDept.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)
                || TextUtils.isEmpty(dept) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        dialog.setMessage("Verifying...");
        dialog.show();

        reference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean found = false;
                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            String dbDept = userSnap.child("dept").getValue(String.class);
                            String dbRole = userSnap.child("role").getValue(String.class);

                            if (dbDept != null && dbDept.equals(dept) &&
                                    dbRole != null && dbRole.equals("teacher")) {

                                HashMap<String, Object> updateMap = new HashMap<>();
                                updateMap.put("name", name);
                                updateMap.put("designation",desig);
                                updateMap.put("password", pass);
                                updateMap.put("phone", phone);

                                userSnap.getRef().updateChildren(updateMap)
                                        .addOnCompleteListener(task -> {
                                            dialog.dismiss();
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignupTcrActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SignupTcrActivity.this, LoginActivity.class);
                                                intent.putExtra("ROLE", role);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(SignupTcrActivity.this, "Failed to update data", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            dialog.dismiss();
                            Toast.makeText(SignupTcrActivity.this, "Email & Dept not approved by Admin", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dialog.dismiss();
                        Toast.makeText(SignupTcrActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


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

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etBlood, etPhone, etID;
    private Button btnSignup;
    private TextView tvTitle, tvSignup;
    private ProgressDialog dialog;
    private DatabaseReference reference;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        reference = FirebaseDatabase.getInstance().getReference("UserSignUp");
        role = getIntent().getStringExtra("ROLE");

        tvTitle = findViewById(R.id.tvTitleSignup);
        tvTitle.setText(role.substring(0, 1).toUpperCase() + role.substring(1) + " Signup");

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmailSignup);
        etPassword = findViewById(R.id.etPasswordSignup);
        etID = findViewById(R.id.etIdSignup);
        etBlood = findViewById(R.id.etBloodSignup);
        etPhone = findViewById(R.id.etPhoneSignup);
        btnSignup = findViewById(R.id.btnSignup);
        tvSignup = findViewById(R.id.tvSignup);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        btnSignup.setOnClickListener(v -> createAccount());

        tvSignup.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            intent.putExtra("ROLE", role);
            startActivity(intent);
            finish();
        });
    }

    private void createAccount() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String id = etID.getText().toString().trim();
        String blood = etBlood.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) ||
                TextUtils.isEmpty(id) || TextUtils.isEmpty(blood) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
            return;
        }

        dialog.setMessage("Verifying details...");
        dialog.show();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean matched = false;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String dbEmail = snap.child("email").getValue(String.class);
                    String dbId = snap.child("id").getValue(String.class);
                    String dbRole = snap.child("role").getValue(String.class);

                    if (email.equals(dbEmail) && id.equals(dbId) && dbRole.equals("student")) {
                        matched = true;

                        HashMap<String, Object> updateMap = new HashMap<>();
                        updateMap.put("name", name);
                        updateMap.put("blood", blood);
                        updateMap.put("phone", phone);
                        updateMap.put("password", pass);

                        snap.getRef().updateChildren(updateMap).addOnCompleteListener(task -> {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                intent.putExtra("ROLE", role);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "Signup Failed to update", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    }
                }

                if (!matched) {
                    dialog.dismiss();
                    Toast.makeText(SignupActivity.this, "Email or ID not approved by admin", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(SignupActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

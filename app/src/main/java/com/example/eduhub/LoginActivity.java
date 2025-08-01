package com.example.eduhub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignup, tvTitle;
    private DatabaseReference reference;
    private ProgressDialog dialog;
    private String role;
    SharedPreferences sharedPreferences;
    private String userKey,mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        reference = FirebaseDatabase.getInstance().getReference("UserSignUp");

        sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);

        role = getIntent().getStringExtra("ROLE");

        tvTitle = findViewById(R.id.tvTitle);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);

        tvTitle.setText(role.substring(0, 1).toUpperCase() + role.substring(1) + " Login");

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        btnLogin.setOnClickListener(v -> loginUser());

        tvSignup.setOnClickListener(v -> {
            if(role.equals("admin")){
                Intent i = new Intent(LoginActivity.this, SignupAdminActivity.class);
                i.putExtra("ROLE", role);
                startActivity(i);
            }
            else if(role.equals("student")){
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                i.putExtra("ROLE", role);
                startActivity(i);
            }else{
                Intent i = new Intent(LoginActivity.this, SignupTcrActivity.class);
                i.putExtra("ROLE", role);
                startActivity(i);
            }
        });
    }

    private void loginUser() {
        String user_email = etEmail.getText().toString().trim();
        String user_pass = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(user_email) || TextUtils.isEmpty(user_pass)) {
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
            return;
        }

        dialog.setMessage("Signing in...");
        dialog.show();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean found = false;
                SharedPreferences.Editor editor=sharedPreferences.edit();

                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    String email = userSnap.child("email").getValue(String.class);
                    String password = userSnap.child("password").getValue(String.class);

                    if (user_email.equals(email) && user_pass.equals(password)) {
                        userKey = userSnap.getKey();
                        mode=userSnap.child("role").getValue(String.class);
                        found = true;
                        editor.putString("profileKey",userKey);
                        editor.putBoolean("profilePermission",found);
                        editor.putString("role",mode);
                        editor.apply();
                        break;
                    }
                }

                dialog.dismiss();

                if (found) {
                    checkRoleAndNavigate(userKey);
                } else {
                    Toast.makeText(LoginActivity.this, "Unsuccessful Sign In", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkRoleAndNavigate(String userKey) {

        DatabaseReference roleRef = FirebaseDatabase.getInstance().getReference("UserSignUp")
                .child(userKey).child("role");

        roleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String savedRole = snapshot.getValue(String.class);


                if(role.equals(savedRole)){
                    if(savedRole.equals("student")){
                        Intent intent=new Intent(LoginActivity.this,StudentActivity.class);
                        startActivity(intent);
                    }
                    else if(savedRole.equals("teacher")){
                        Intent intent=new Intent(LoginActivity.this,FacultyMemberActivity.class);
                        startActivity(intent);
                    }
                    else if(savedRole.equals("admin")){
                        Intent intent=new Intent(LoginActivity.this,AdminActivity.class);
                        startActivity(intent);
                    }
                }

                else{
                    Toast.makeText(LoginActivity.this, "Not Match Role!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Firebase error", Toast.LENGTH_SHORT).show();
            }
        });
    }


   protected void onResume() {
        super.onResume();
        etEmail.setText("");
        etPassword.setText("");
    }

}

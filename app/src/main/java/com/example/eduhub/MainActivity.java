package com.example.eduhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnAdmin, btnStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdmin = findViewById(R.id.btnAdmin);
        btnStudent = findViewById(R.id.btnStudent);

        btnAdmin.setOnClickListener(v -> openLogin("admin"));
        btnStudent.setOnClickListener(v -> openLogin("student"));
    }

    private void openLogin(String role) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("ROLE", role);
        startActivity(intent);
    }
}

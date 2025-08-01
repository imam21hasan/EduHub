package com.example.eduhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    ImageView logout;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);

        logout=findViewById(R.id.logoutAdmin);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new StudentFragment()).commit();

        BottomNavigationView nav = findViewById(R.id.bottom_nav);

        nav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            if (item.getItemId() == R.id.nav_students) {
                selected = new StudentFragment();
            } else if (item.getItemId() == R.id.nav_teachers) {
                selected = new TeacherFragment();
            } else if (item.getItemId() == R.id.nav_fees) {
                selected = new FeeFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selected)
                    .commit();
            return true;
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("profileKey",null);
                editor.putBoolean("profilePermission", false);
                editor.putString("role",null);
                editor.apply();

                Toast.makeText(AdminActivity.this, "Log Out successfully", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
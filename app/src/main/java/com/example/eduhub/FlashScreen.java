package com.example.eduhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FlashScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String mode="";
    String key="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_flash_screen);

        sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        mode=sharedPreferences.getString("role",null);
        key=sharedPreferences.getString("profileKey",null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(mode!=null&& key!=null) {

                    if (mode.equals("admin") && key != null) {

                        startActivity(new Intent(FlashScreen.this, AdminActivity.class));
                        finish();
                    } else if (mode.equals("student") && key != null) {
                        startActivity(new Intent(FlashScreen.this, StudentActivity.class));
                        finish();
                    } else if (mode.equals("teacher") && key != null) {
                        startActivity(new Intent(FlashScreen.this,FacultyMemberActivity.class));
                        finish();
                    }
                }
                else {
                    startActivity(new Intent(FlashScreen.this,MainActivity.class));
                    finish();
                }
            }
        },1000);

    }
}
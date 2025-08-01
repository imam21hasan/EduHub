package com.example.eduhub;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnAdmin,btnTeacher, btnStudent;
    private static final String adminPassword = "12345";
    SharedPreferences sharedPreferences;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        key=sharedPreferences.getString("profileKey",null);

        btnAdmin = findViewById(R.id.btnAdmin);
        btnTeacher=findViewById(R.id.btnTeacher);
        btnStudent = findViewById(R.id.btnStudent);

        btnAdmin.setOnClickListener(v -> openLoginAdmin("admin"));
        btnTeacher.setOnClickListener(v -> openLoginTeacher("teacher"));
        btnStudent.setOnClickListener(v -> openLoginStudent("student"));

    }

    private void openLoginAdmin(String role) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Admin Access");
        builder.setMessage("Enter admin password:");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        int paddingLeftInDp = 15;
        final float scale = getResources().getDisplayMetrics().density;
        int paddingInPx = (int) (paddingLeftInDp * scale + 0.5f);
        input.setPadding(paddingInPx, input.getPaddingTop(), input.getPaddingRight(), input.getPaddingBottom());
        builder.setView(input);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String enteredPassword = input.getText().toString();

                if (enteredPassword.equals(adminPassword)) {
                    if(key==null){
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.putExtra("ROLE", role);
                        startActivity(intent);
                    }
                    else if(key!=null && role.equals("admin")){

                        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                        intent.putExtra("ROLE", role);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    private void openLoginTeacher(String role) {
        if(key==null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("ROLE", role);
            startActivity(intent);
        }
        else if(key!=null&& role.equals("teacher")){

            Intent intent = new Intent(MainActivity.this, FacultyMemberActivity.class);
            intent.putExtra("ROLE", role);
            startActivity(intent);
        }
    }

    private void openLoginStudent(String role) {
        if(key==null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("ROLE", role);
            startActivity(intent);
        }
        else if(key!=null&& role.equals("student")){

            Intent intent = new Intent(MainActivity.this, StudentActivity.class);
            intent.putExtra("ROLE", role);
            startActivity(intent);
        }
    }


}

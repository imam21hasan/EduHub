
package com.example.eduhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FacultyMemberActivity extends AppCompatActivity {

    private ImageView profilePicture, logout;
    private TextView tname,designation,department;

    private DatabaseReference ref;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_faculty_member);

        tname=findViewById(R.id.f_name);
        designation=findViewById(R.id.f_designation);
        department=findViewById(R.id.f_dept);

        logout=findViewById(R.id.t_logout);

        sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        String userKey = sharedPreferences.getString("profileKey", null);

        if (userKey == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ref = FirebaseDatabase.getInstance().getReference("UserSignUp").child(userKey);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String desig = snapshot.child("designation").getValue(String.class);
                    String dept = snapshot.child("dept").getValue(String.class);

                    tname.setText(name);
                    designation.setText(desig);
                    department.setText("Dept. of "+dept);

                } else {
                    Toast.makeText(FacultyMemberActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FacultyMemberActivity.this, "Database can't connect", Toast.LENGTH_SHORT).show();
            }
        });




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("profileKey",null);
                editor.putBoolean("profilePermission", false);
                editor.putString("role",null);
                editor.apply();

                Toast.makeText(FacultyMemberActivity.this, "Log Out successfully", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(FacultyMemberActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
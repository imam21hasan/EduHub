package com.example.eduhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Profile extends AppCompatActivity {

    private ImageView profilePicture;
    private TextView studentName, studentID, sessionY, bloodGroup, phoneNumber;
    private Button editProfile;

    private DatabaseReference ref;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        profilePicture = findViewById(R.id.profileImage);
        studentName = findViewById(R.id.studentName);
        studentID = findViewById(R.id.studentID);
        sessionY = findViewById(R.id.studentSession);
        bloodGroup = findViewById(R.id.studentBlood);
        phoneNumber = findViewById(R.id.studentPhone);
        editProfile = findViewById(R.id.editProfile);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
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
                    String id = snapshot.child("id").getValue(String.class);
                    String session = snapshot.child("session").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String blood = snapshot.child("blood").getValue(String.class);

                    studentName.setText(name);
                    studentID.setText(id);
                    sessionY.setText(session);
                    bloodGroup.setText(blood);
                    phoneNumber.setText(phone);
                } else {
                    Toast.makeText(Profile.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Database can't connect", Toast.LENGTH_SHORT).show();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Edit_Profile.class);
                startActivity(intent);
                finish();
            }
        });


    }
}

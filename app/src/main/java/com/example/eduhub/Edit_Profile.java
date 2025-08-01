package com.example.eduhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Edit_Profile extends AppCompatActivity {

    private ImageView newImage;
    private Button selectImageButton, editSubmitButton;
    private EditText editName, editBlood, editPhone;
    private DatabaseReference userRef;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        newImage = findViewById(R.id.newImage);
        selectImageButton = findViewById(R.id.selectImageButton);
        editName = findViewById(R.id.editName);
        editBlood = findViewById(R.id.editBlood);
        editPhone = findViewById(R.id.editPhone);
        editSubmitButton = findViewById(R.id.editSubmitButton);

        loadUserInfo();

        editSubmitButton.setOnClickListener(v -> editInfo());
    }

    private void loadUserInfo() {
        String key = sharedPreferences.getString("profileKey", null);

        if (key == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        userRef = FirebaseDatabase.getInstance().getReference("UserSignUp").child(key);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    editName.setText(snapshot.child("name").getValue(String.class));
                    editBlood.setText(snapshot.child("blood").getValue(String.class));
                    editPhone.setText(snapshot.child("phone").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Edit_Profile.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editInfo() {
        String key = sharedPreferences.getString("profileKey", null);

        if (key == null) {
            Toast.makeText(this, "Invalid User!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userName = editName.getText().toString().trim();
        String userBlood = editBlood.getText().toString().trim();
        String userPhone = editPhone.getText().toString().trim();

        userRef = FirebaseDatabase.getInstance().getReference("UserSignUp").child(key);

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", userName);
        updates.put("blood", userBlood);
        updates.put("phone", userPhone);

        userRef.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Edit_Profile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Edit_Profile.this, Profile.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Edit_Profile.this, "Update failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

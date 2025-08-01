package com.example.eduhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentActivity extends AppCompatActivity {

    private CardView teacher,logout,notice,profile,payment,bloodBank,location;

    private String deptName;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);


        teacher=findViewById(R.id.teacher);
        logout=findViewById(R.id.logout);
        notice=findViewById(R.id.notice);
        profile=findViewById(R.id.profile);
        payment=findViewById(R.id.payment);
        bloodBank=findViewById(R.id.blood);
        location=findViewById(R.id.location);

        sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        String userKey = sharedPreferences.getString("profileKey", null);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(StudentActivity.this, Profile.class);
                startActivity(intent);
            }
        });


        if (userKey != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserSignUp").child(userKey);

            userRef.child("dept").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        deptName = task.getResult().getValue(String.class);
                    } else {
                        Toast.makeText(getApplicationContext(), "Department not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No profile key found", Toast.LENGTH_SHORT).show();
        }


        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(StudentActivity.this, DetailsTeachers.class);
                intent.putExtra("dept",deptName);
                startActivity(intent);



               /*String studentEmail = sharedPreferences.getString("profileKey", null);

                if (studentEmail == null) {
                    Toast.makeText(StudentActivity.this, "Student email not found!", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserSignUp");

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            String email = snap.child("email").getValue(String.class);

                            if (email != null && email.equals(studentEmail)) {
                                String dept = snap.child("dept").getValue(String.class);

                                Intent intent = new Intent(StudentActivity.this, DetailsTeachers.class);
                                intent.putExtra("studentDept", dept);
                                startActivity(intent);
                                return;
                            }
                        }
                        Toast.makeText(StudentActivity.this, "Department not found for student!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(StudentActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });*/


            }
        });


        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(StudentActivity.this, NoticeActivity.class);
                startActivity(intent);
            }
        });


        bloodBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(StudentActivity.this, BloodBank.class);
                startActivity(intent);
            }
        });


        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(StudentActivity.this, Payment.class);
                startActivity(intent);
            }
        });


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String plusCode = "6MWQ+V34, Chandpur";

                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(plusCode));

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    String mapUrl = "https://www.google.com/maps/search/?api=1&query=" + Uri.encode(plusCode);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl));
                    startActivity(browserIntent);
                }
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

                Toast.makeText(StudentActivity.this, "Log Out successfully", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(StudentActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
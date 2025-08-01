package com.example.eduhub;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class DetailsTeachers extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TeacherAdapter teacherAdapter;
    private List<TeacherModel> teacherList;
    private DatabaseReference userRef;
    private String studentDept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_teachers);

        recyclerView = findViewById(R.id.teacherRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        teacherList = new ArrayList<>();
        teacherAdapter = new TeacherAdapter(teacherList);
        recyclerView.setAdapter(teacherAdapter);

        studentDept = getIntent().getStringExtra("dept");

        userRef = FirebaseDatabase.getInstance().getReference("UserSignUp");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teacherList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String role = snap.child("role").getValue(String.class);
                    String dept = snap.child("dept").getValue(String.class);

                    if ("teacher".equals(role) && dept != null && dept.equalsIgnoreCase(studentDept)) {
                        String name = snap.child("name").getValue(String.class);
                        String designation = snap.child("designation").getValue(String.class);
                        String email = snap.child("email").getValue(String.class);
                        String phone = snap.child("phone").getValue(String.class);

                        teacherList.add(new TeacherModel(name, designation, email, phone));
                    }
                }
                teacherAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}

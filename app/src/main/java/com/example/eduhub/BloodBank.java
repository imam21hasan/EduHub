package com.example.eduhub;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BloodBank extends AppCompatActivity {

    private SearchView bloodSearch;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private List<StudentModel> studentList;
    private List<StudentModel> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);

        bloodSearch = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerViewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new StudentAdapter(filteredList);
        recyclerView.setAdapter(adapter);

        fetchStudentsFromDatabase();

        bloodSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterByBloodGroup(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterByBloodGroup(newText);
                return true;
            }
        });

    }

    private void fetchStudentsFromDatabase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserSignUp");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    StudentModel student = snap.getValue(StudentModel.class);
                    if (student != null &&
                            "student".equalsIgnoreCase(student.getRole()) &&
                            student.getBlood() != null &&
                            !student.getBlood().trim().isEmpty()) {

                        studentList.add(student);
                    }
                }

                filteredList.clear();
                filteredList.addAll(studentList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BloodBank.this, "Error!" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterByBloodGroup(String query) {
        filteredList.clear();
        for (StudentModel student : studentList) {
            if (student.getBlood() != null &&
                    student.getBlood().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(student);
            }
        }
        adapter.notifyDataSetChanged();
    }
}

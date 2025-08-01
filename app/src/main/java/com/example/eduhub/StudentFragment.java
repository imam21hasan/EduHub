package com.example.eduhub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class StudentFragment extends Fragment {

    EditText emailEt, idEt, deptEt, sessionEt;
    Button addBtn, updateBtn, deleteBtn;
    DatabaseReference dbRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student, container, false);

        emailEt = view.findViewById(R.id.studentEmail);
        idEt = view.findViewById(R.id.studentId);
        deptEt = view.findViewById(R.id.studentDept);
        sessionEt = view.findViewById(R.id.studentSession);

        addBtn = view.findViewById(R.id.addStudentBtn);
        updateBtn = view.findViewById(R.id.updateStudentBtn);
        deleteBtn = view.findViewById(R.id.deleteStudentBtn);

        dbRef = FirebaseDatabase.getInstance().getReference("UserSignUp");

        addBtn.setOnClickListener(v -> checkEmailAndAdd("student"));
        updateBtn.setOnClickListener(v -> updateUser("student"));
        deleteBtn.setOnClickListener(v -> deleteUser("student"));

        return view;
    }

    private void checkEmailAndAdd(String role) {
        String email = emailEt.getText().toString().trim();
        Query query = dbRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(getContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    String uid = dbRef.push().getKey();

                    String deptInput = deptEt.getText().toString().trim().toUpperCase();

                    Map<String, Object> data = new HashMap<>();
                    data.put("email", email);
                    data.put("id", idEt.getText().toString().trim());
                    data.put("dept", deptInput);
                    data.put("session", sessionEt.getText().toString().trim());
                    data.put("role", role);
                    dbRef.child(uid).setValue(data);
                    Toast.makeText(getContext(), "Student added", Toast.LENGTH_SHORT).show();
                    clearFields();
                }
            }

            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void updateUser(String role) {
        String email = emailEt.getText().toString().trim();
        Query query = dbRef.orderByChild("email").equalTo(email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean updated = false;

                String deptInput = deptEt.getText().toString().trim().toUpperCase();

                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.child("role").getValue(String.class).equals(role)) {
                        child.getRef().child("id").setValue(idEt.getText().toString().trim());
                        child.getRef().child("dept").setValue(deptInput);
                        child.getRef().child("session").setValue(sessionEt.getText().toString().trim());
                        updated = true;
                    }
                }

                if (updated) {
                    Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(getContext(), "Student not found", Toast.LENGTH_SHORT).show();
                }
            }

            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void deleteUser(String role) {
        String email = emailEt.getText().toString().trim();
        Query query = dbRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean deleted = false;
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.child("role").getValue(String.class).equals(role)) {
                        child.getRef().removeValue();
                        deleted = true;
                    }
                }

                if (deleted) {
                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(getContext(), "Student not found", Toast.LENGTH_SHORT).show();
                }
            }

            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void clearFields() {
        emailEt.setText("");
        idEt.setText("");
        deptEt.setText("");
        sessionEt.setText("");
    }
}

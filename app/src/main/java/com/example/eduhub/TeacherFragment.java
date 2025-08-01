package com.example.eduhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class TeacherFragment extends Fragment {

    EditText emailEt, deptEt;
    Button addBtn, updateBtn, deleteBtn;
    DatabaseReference dbRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher, container, false);

        emailEt = view.findViewById(R.id.teacherEmail);
        deptEt = view.findViewById(R.id.teacherDept);

        addBtn = view.findViewById(R.id.addTeacherBtn);
        updateBtn = view.findViewById(R.id.updateTeacherBtn);
        deleteBtn = view.findViewById(R.id.deleteTeacherBtn);

        dbRef = FirebaseDatabase.getInstance().getReference("UserSignUp");

        addBtn.setOnClickListener(v -> checkEmailAndAdd("teacher"));
        updateBtn.setOnClickListener(v -> updateUser("teacher"));
        deleteBtn.setOnClickListener(v -> deleteUser("teacher"));

        return view;
    }

    private void checkEmailAndAdd(String role) {
        String email = emailEt.getText().toString();
        Query query = dbRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(getContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    String uid = dbRef.push().getKey();
                    Map<String, Object> data = new HashMap<>();
                    data.put("email", email);
                    data.put("dept", deptEt.getText().toString());
                    data.put("role", role);
                    dbRef.child(uid).setValue(data);
                    Toast.makeText(getContext(), "Teacher added", Toast.LENGTH_SHORT).show();
                    clearFields();
                }
            }
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUser(String role) {
        Query query = dbRef.orderByChild("email").equalTo(emailEt.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean found = false;
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.child("role").getValue(String.class).equals(role)) {
                        child.getRef().child("dept").setValue(deptEt.getText().toString());
                        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                        found = true;
                        clearFields();
                    }
                }
                if (!found) {
                    Toast.makeText(getContext(), "No matching teacher found to update", Toast.LENGTH_SHORT).show();
                }
            }
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteUser(String role) {
        Query query = dbRef.orderByChild("email").equalTo(emailEt.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean found = false;
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.child("role").getValue(String.class).equals(role)) {
                        child.getRef().removeValue();
                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        found = true;
                        clearFields();
                    }
                }
                if (!found) {
                    Toast.makeText(getContext(), "No matching teacher found to delete", Toast.LENGTH_SHORT).show();
                }
            }
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void clearFields() {
        emailEt.setText("");
        deptEt.setText("");
    }
}

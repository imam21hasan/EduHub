package com.example.eduhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FeeFragment extends Fragment {

    EditText semesterFee, improvementFee;
    Button updateFeesBtn;
    DatabaseReference dbRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fee, container, false);

        semesterFee = view.findViewById(R.id.semesterFee);
        improvementFee = view.findViewById(R.id.improvementFee);
        updateFeesBtn = view.findViewById(R.id.updateFeesBtn);

        dbRef = FirebaseDatabase.getInstance().getReference("AcademicFees");

        updateFeesBtn.setOnClickListener(v -> {
            String sFee = semesterFee.getText().toString();
            String iFee = improvementFee.getText().toString();

            if (sFee.isEmpty() || iFee.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("semesterFee", sFee);
            data.put("improvementFee", iFee);

            dbRef.setValue(data).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Fees updated", Toast.LENGTH_SHORT).show();
                    semesterFee.setText("");
                    improvementFee.setText("");
                } else {
                    Toast.makeText(getContext(), "Failed to update fees", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }
}

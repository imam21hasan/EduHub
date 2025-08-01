package com.example.eduhub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
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

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Payment extends AppCompatActivity {

    private Button semesterFeeBtn, improvementFeeBtn, paymentBtn;
    private TextView semesterFees, improvementFees, amount;
    private int sFee = 0, impFee = 0, totalFee = 0;
    private boolean semesterFeeAdded = false;
    private int improvementFeeCount = 0;

    String storeID = "Eduhub-2023";
    String apiKey = "HE5SD8X5wiFPwL9iDww4rx9fDNsmjkoSHNHy01KU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        semesterFees = findViewById(R.id.semesterFees);
        improvementFees = findViewById(R.id.improvementFees);
        amount = findViewById(R.id.totalAmount);

        semesterFeeBtn = findViewById(R.id.academicFeeBtn);
        improvementFeeBtn = findViewById(R.id.improvementFeeBtn);
        paymentBtn = findViewById(R.id.payment);

        fetchFeesFromDatabase();

        semesterFeeBtn.setOnClickListener(v -> {
            if (!semesterFeeAdded) {
                totalFee += sFee;
                semesterFeeAdded = true;
                amount.setText("Payable : " + totalFee + " Tk");
            } else {
                Toast.makeText(Payment.this, "Semester fee already added!", Toast.LENGTH_SHORT).show();
            }
        });

        improvementFeeBtn.setOnClickListener(v -> {
            if (improvementFeeCount < 2) {
                totalFee += impFee;
                improvementFeeCount++;
                amount.setText("Payable : " + totalFee + " Tk");
            } else {
                Toast.makeText(Payment.this, "Maximum 2 improvement fees allowed per semester", Toast.LENGTH_SHORT).show();
            }
        });

        paymentBtn.setOnClickListener(v -> {
            if (totalFee == 0) {
                Toast.makeText(this, "Please add a fee amount before payment", Toast.LENGTH_SHORT).show();
            } else {
                createPayment(String.valueOf(totalFee));
            }
        });
    }

    private void fetchFeesFromDatabase() {
        DatabaseReference feesRef = FirebaseDatabase.getInstance().getReference("AcademicFees");

        feesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String semFeeStr = snapshot.child("semesterFee").getValue(String.class);
                    String impFeeStr = snapshot.child("improvementFee").getValue(String.class);

                    if (semFeeStr != null && impFeeStr != null) {
                        sFee = Integer.parseInt(semFeeStr);
                        impFee = Integer.parseInt(impFeeStr);

                        semesterFees.setText("Semester Fee : " + sFee + " Tk");
                        improvementFees.setText("Improvement Fee : " + impFee + " Tk");
                    } else {
                        Toast.makeText(Payment.this, "Fee data missing", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Payment.this, "Academic Fees not declared!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Payment.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ Create UddoktaPay Payment
   private void createPayment(String amount) {
        new Thread(() -> {
            try {
                // ✅ UddoktaPay payment endpoint
                URL url = new URL("https://api.uddoktapay.com/api/checkout-v2");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0"); // ✅ Some APIs require this

                // ✅ Send data
                JSONObject data = new JSONObject();
                data.put("store_id", storeID);
                data.put("amount", amount);
                data.put("payment_type", "bkash");
                data.put("currency", "BDT");
                data.put("cancel_url", "https://toyo.paymently.io/cancel");
                data.put("success_url", "https://toyo.paymently.io/success");
                data.put("api_key", apiKey);

                OutputStream os = conn.getOutputStream();
                os.write(data.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();

                // ✅ Read response (success or error)
                java.util.Scanner s = new java.util.Scanner(
                        responseCode == HttpURLConnection.HTTP_OK ? conn.getInputStream() : conn.getErrorStream()
                ).useDelimiter("\\A");

                final String result = s.hasNext() ? s.next() : "";


                JSONObject response = new JSONObject(result);

                if (response.has("payment_url")) {
                    final String paymentURL = response.getString("payment_url");

                    runOnUiThread(() -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentURL));
                        startActivity(browserIntent);
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(Payment.this, "Payment URL not found.\nResponse: " + result, Toast.LENGTH_LONG).show();
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(Payment.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }


}

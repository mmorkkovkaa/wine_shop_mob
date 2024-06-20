package com.example.wine_shop.Users;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wine_shop.Prevalent.Prevalent;
import com.example.wine_shop.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {

    private EditText addressEditText;
    private Button payButton;
    private RadioGroup paymentOptions;
    private int totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        totalAmount = getIntent().getIntExtra("Total Amount", 0);

        paymentOptions = findViewById(R.id.payment_options);
        addressEditText = findViewById(R.id.address);
        payButton = findViewById(R.id.pay_button);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment();
            }
        });
    }

    private void processPayment() {
        String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("totalAmount", String.valueOf(totalAmount));
        orderMap.put("address", addressEditText.getText().toString());
        orderMap.put("status", "not shipped");

        orderRef.updateChildren(orderMap);

        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart");
        cartRef.child(Prevalent.currentOnlineUser.getPhone()).removeValue();

        Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Оплата прошла успешно")
                .setMessage("Спасибо за ваш заказ!")
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setIcon(R.drawable.success_picture)
                .show();
    }
}

package com.example.wine_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText nameInput,phoneInput, passwordInput;

    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        registerButton = (Button)findViewById(R.id.register_button);
        nameInput = (EditText) findViewById(R.id.register_username_input);
        phoneInput = (EditText) findViewById(R.id.register_phone_input);
        passwordInput = (EditText)  findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String username = nameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String password = passwordInput.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, "Add name", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Add phone", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Add password", Toast.LENGTH_SHORT).show();

        }
        else
        {
            loadingBar.setTitle("create user");
            loadingBar.setMessage("wait a minute...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            ValidatePhone(username, phone, password);

        }


    }

    private void ValidatePhone(String username, String phone, String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child("phone").exists()))
                {
                    HashMap<String , Object> userDataMap = new HashMap<>();
                    userDataMap.put("phone", phone);
                    userDataMap.put("name", username);
                    userDataMap.put("password", password);

                    RootRef.child("Users").child(phone).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Register Succsessful!", Toast.LENGTH_SHORT).show();

                                        Intent LoginIntent= new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(LoginIntent);
                                    }
                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Register is not succsessful!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Номер" + phone + "уже используется",Toast.LENGTH_SHORT).show();

                    Intent LoginIntent= new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(LoginIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
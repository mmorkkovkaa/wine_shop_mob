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
import android.widget.TextView;
import android.widget.Toast;

import com.example.wine_shop.Admin.AdminCategoryActivity;
import com.example.wine_shop.Model.Users;
import com.example.wine_shop.Prevalent.Prevalent;
import com.example.wine_shop.Users.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private Button loginButton;
    private EditText phoneInput, passwordInput;

    private TextView adminLink, notAdminLink;

    private ProgressDialog loadingBar;

    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginButton = (Button)findViewById(R.id.login_button);
        phoneInput = (EditText) findViewById(R.id.login_phone_input);
        passwordInput = (EditText)  findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);

        adminLink = (TextView)findViewById(R.id.admin_panel_link);
        notAdminLink= (TextView)findViewById(R.id.not_admin_panel_link);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);

                loginButton.setText("Вход для админа");
                parentDbName = "Admins";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);

                loginButton.setText("Входа");
                parentDbName = "Users";
            }
        });

    }

    private void loginUser() {
        String phone = phoneInput.getText().toString();
        String password = passwordInput.getText().toString();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Add phone", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Add password", Toast.LENGTH_SHORT).show();

        }
        else{
            loadingBar.setTitle("вход в приложение");
            loadingBar.setMessage("wait a minute...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            ValidateUser( phone, password);
        }

    }

    private void ValidateUser(String phone, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData = snapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            if(parentDbName.equals("Users")){
                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "Успешный вход!", Toast.LENGTH_SHORT).show();

                                Prevalent.currentOnlineUser = usersData;

                                Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(homeIntent);
                            }else if(parentDbName.equals("Admins")){
                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "Успешный вход!", Toast.LENGTH_SHORT).show();

                                Intent homeIntent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(homeIntent);
                            }
                        }
                        else{
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                else{
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Аккаунт с номером" + phone + "не существует", Toast.LENGTH_SHORT).show();

                    Intent RegisterIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(RegisterIntent);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
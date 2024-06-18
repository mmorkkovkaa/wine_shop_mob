package com.example.wine_shop.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.wine_shop.LoginActivity;
import com.example.wine_shop.R;
import com.example.wine_shop.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    private Button joinButton;
    private Button loginButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        joinButton = (Button) findViewById(R.id.main_join_button);
        loginButton = (Button) findViewById(R.id.main_login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(LoginIntent);
            }
        });


        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(RegisterIntent);
            }
        });

    }
}
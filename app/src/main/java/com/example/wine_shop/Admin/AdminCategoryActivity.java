package com.example.wine_shop.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.wine_shop.R;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView redWine, whiteWine, eliteWine, prinWine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        redWine=(ImageView) findViewById(R.id.red_wine);
        whiteWine=(ImageView) findViewById(R.id.white_wine);
        eliteWine=(ImageView) findViewById(R.id.elite_wine);
        prinWine=(ImageView) findViewById(R.id.prin_wine);


        redWine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "redWine");
                startActivity(intent);
            }
        });

        whiteWine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "whiteWine");
                startActivity(intent);
            }
        });

        eliteWine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "eliteWine");
                startActivity(intent);
            }
        });

        prinWine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "prinWine");
                startActivity(intent);
            }
        });
    }
}
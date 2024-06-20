package com.example.wine_shop.Users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wine_shop.R;

public class CategorySelectionActivity extends AppCompatActivity {

    private TextView closeButn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);
        closeButn = (TextView)findViewById(R.id.close_btn);

        findViewById(R.id.btn_red_wine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProductListActivity("redWine");
            }
        });

        findViewById(R.id.btn_white_wine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProductListActivity("whiteWine");
            }
        });

        findViewById(R.id.btn_elite_wine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProductListActivity("eliteWine");
            }
        });

        findViewById(R.id.btn_wine_accessories).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProductListActivity("prinWine");
            }
        });

        closeButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent closeIntent = new Intent(CategorySelectionActivity.this, HomeActivity.class);
                startActivity(closeIntent);
            }
        });
    }

    private void openProductListActivity(String category) {
        Intent intent = new Intent(CategorySelectionActivity.this, ProductListActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}

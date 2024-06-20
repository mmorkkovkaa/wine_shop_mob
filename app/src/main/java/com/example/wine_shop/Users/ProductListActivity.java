package com.example.wine_shop.Users;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wine_shop.Model.Products;
import com.example.wine_shop.Prevalent.Prevalent;
import com.example.wine_shop.R;
import com.example.wine_shop.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String category;

    private TextView closeButn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        category = getIntent().getStringExtra("category");
        closeButn = (TextView)findViewById(R.id.close_btn);
        recyclerView = findViewById(R.id.product_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadProducts();

        closeButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent closeIntent = new Intent(ProductListActivity.this, HomeActivity.class);
                startActivity(closeIntent);
            }
        });
    }

    private void loadProducts() {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsRef.orderByChild("category").equalTo(category), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                holder.txtProductName.setText(model.getPname());
                holder.txtProductPrice.setText("Цена: " + model.getPrice() + "$");
                holder.txtProductDescription.setText(model.getDescription());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Логика добавления в корзину
                        addToCart(model);
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items, parent, false);
                return new ProductViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void addToCart(Products product) {
        // Логика добавления продукта в корзину
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(Prevalent.currentOnlineUser.getPhone());
        String productId = product.getPid();

        HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productId);
        cartMap.put("pname", product.getPname());
        cartMap.put("price", product.getPrice());
        cartMap.put("quantity", "1");
        cartMap.put("discount", "0");

        cartRef.child(productId).updateChildren(cartMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProductListActivity.this, "Добавлено в корзину", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductListActivity.this, "Ошибка добавления в корзину", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
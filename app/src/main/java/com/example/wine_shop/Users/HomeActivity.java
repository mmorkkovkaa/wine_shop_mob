package com.example.wine_shop.Users;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wine_shop.LoginActivity;
import com.example.wine_shop.Model.Products;
import com.example.wine_shop.Prevalent.Prevalent;
import com.example.wine_shop.R;
import com.example.wine_shop.SettingsActivity;
import com.example.wine_shop.ViewHolder.ProductViewHolder;
//import com.example.wine_shop.databinding.ActivityHomeBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;



    private SharedPreferences sharedPreferences;

    DatabaseReference ProductsRef;

    private RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Menu");

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Корзина", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        userNameTextView.setText(Prevalent.currentOnlineUser.getName());
        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.wine_icon).into(profileImageView);

        recyclerView = findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    }


    protected void onStart(){
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef, Products.class).build();

        FirebaseRecyclerAdapter<Products , ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {

                holder.txtProductName.setText(model.getPname());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price : " + model.getPrice() + "$");
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart");

                        HashMap<String, Object> cartMap = new HashMap<>();
                        cartMap.put("pid", model.getPid());
                        cartMap.put("pname", model.getPname());
                        cartMap.put("price", model.getPrice());
                        cartMap.put("quantity", "1");
                        cartMap.put("discount", "0");

                        cartRef.child(Prevalent.currentOnlineUser.getPhone())
                                .child(model.getPid())
                                .updateChildren(cartMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(HomeActivity.this, "Добавлено в корзину", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(HomeActivity.this, "Ошибка добавления в корзину", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });

//                holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        model.addToCart(Prevalent.currentOnlineUser.getPhone());
//                        Toast.makeText(HomeActivity.this, "Добавлено в корзину", Toast.LENGTH_SHORT).show();
//                    }
//                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        } ;

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_cart){
            Intent setIntent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(setIntent);

        } else if(id == R.id.nav_orders){
            Intent setIntent = new Intent(HomeActivity.this, OrdersActivity.class);
            startActivity(setIntent);

        } else if(id == R.id.nav_categories){
            Intent setIntent = new Intent(HomeActivity.this, CategorySelectionActivity.class);
            startActivity(setIntent);

        } else if(id == R.id.nav_settings){
            Intent setIntent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(setIntent);

        } else if(id == R.id.nav_logout){
            clearSharedPreferences();
            Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clearSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
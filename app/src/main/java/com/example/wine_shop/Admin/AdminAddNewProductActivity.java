package com.example.wine_shop.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wine_shop.LoginActivity;
import com.example.wine_shop.R;
import com.example.wine_shop.RegisterActivity;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String categoryName, Description, Price, ProducteName, saveCurentDay, saveCurentTime, ProductRandomKey, downloadImageUrl;
    private ImageView productImage;
    private EditText prodName, prodDesc, prodPrice;
    private Button addNewProduct;
    private static final int GALLERYPICK = 1;
    private Uri ImageUri;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        inited();

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidationProductData();
            }
        });


    }

    private void ValidationProductData() {
        Description = prodDesc.getText().toString();
        Price = prodPrice.getText().toString();
        ProducteName = prodName.getText().toString();

        if(ImageUri == null){
            Toast.makeText(this, "добавьте изображение товара", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description)){
            Toast.makeText(this, "добавьте описание товара", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price)){
            Toast.makeText(this, "добавьте стоимость товара", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(ProducteName)){
            Toast.makeText(this, "добавьте название товара", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreProductInformation();
        }


    }

    private void StoreProductInformation() {

        loadingBar.setTitle("uplad images");
        loadingBar.setMessage("wait a minute...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat curretDate = new SimpleDateFormat("ddMMyyyy");
        saveCurentDay = curretDate.format(calendar.getTime());

        SimpleDateFormat curretTime = new SimpleDateFormat("HHmmss");
        saveCurentTime = curretTime.format(calendar.getTime());

        ProductRandomKey = saveCurentDay + saveCurentTime;

        final StorageReference filePath = ProductImagesRef.child(ImageUri.getPathSegments() + ProductRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Success", Toast.LENGTH_SHORT).show();

                // Получаем ссылку на загруженное изображение
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadImageUrl = uri.toString(); // Получаем URL загруженного изображения
                        Toast.makeText(AdminAddNewProductActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                        SaveProductInfoToDatabase(); // Сохраняем информацию о продукте в базу данных
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Обработка ошибки при загрузке изображения
                Toast.makeText(AdminAddNewProductActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });

    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();

        productMap.put("pid", ProductRandomKey);
        productMap.put("date", saveCurentDay);
        productMap.put("time", saveCurentTime);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", categoryName);
        productMap.put("price", Price);
        productMap.put("pname", ProducteName);

        ProductsRef.child(ProductRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){


                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this, "product done", Toast.LENGTH_SHORT).show();

                    Intent loginIntent= new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                    startActivity(loginIntent);

                }
                else{
                    String message  = task.getException().toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "error product not done" + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }
        });

    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        startActivityForResult(galleryIntent, GALLERYPICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERYPICK && resultCode == RESULT_OK && data != null){
            ImageUri = data.getData();
            productImage.setImageURI(ImageUri);
        }
    }

    private void inited() {
        categoryName = getIntent().getExtras().get("category").toString();
        productImage = findViewById(R.id.select_p_img);
        prodName = findViewById(R.id.product_name);
        prodDesc = findViewById(R.id.producte_description);
        prodPrice = findViewById(R.id.producte_prise);
        addNewProduct = findViewById(R.id.button_add_p);
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Imaged");

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        loadingBar = new ProgressDialog(this);


    }
}
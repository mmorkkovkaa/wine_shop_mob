package com.example.wine_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wine_shop.Prevalent.Prevalent;
import com.example.wine_shop.Users.HomeActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profImageView;

    private EditText nameEdit, phoneEdit, adressEdit;

    private TextView closeButton, saveButton;

    private String checker = "";

    private static final int GALLERY_PICK = 1;

    private Uri imageUri;

    private StorageReference storageProfilePictureRef;

    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        profImageView = (CircleImageView) findViewById(R.id.settings_acc_img);
        nameEdit = (EditText) findViewById(R.id.settings_name);
        phoneEdit = (EditText) findViewById(R.id.settings_phone);
        adressEdit = (EditText) findViewById(R.id.settings_adress);
        closeButton = (TextView) findViewById(R.id.close_settings_tw);
        saveButton = (TextView) findViewById(R.id.save_settings_tw);
        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile images");


        userInfoDisplay(profImageView, nameEdit, phoneEdit, adressEdit);


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent closeIntent = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(closeIntent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")) {
                    userInfoSave();
                } else {
                    updateOnlyUserInfo();
                }
            }
        });

        profImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_PICK);
            }
        });
    }

    private void userInfoDisplay(final CircleImageView profImageView, final EditText nameEdit, final EditText phoneEdit, final EditText addressEdit) {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("image").exists()) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profImageView);
                        nameEdit.setText(name);
                        phoneEdit.setText(phone);
                        addressEdit.setText(address);
                    } else if (dataSnapshot.child("address").exists()) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        nameEdit.setText(name);
                        phoneEdit.setText(phone);
                        addressEdit.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profImageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void userInfoSave() {

        if (TextUtils.isEmpty(nameEdit.getText().toString())) {
            Toast.makeText(this, "Заполните имя.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phoneEdit.getText().toString())) {
            Toast.makeText(this, "Заполните номер телефона", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(adressEdit.getText().toString())) {
            Toast.makeText(this, "Заполните адресс", Toast.LENGTH_SHORT).show();
        } else if (checker.equals("clicked")) {
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePictureRef.child(Prevalent.currentOnlineUser.getPhone() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        String myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", nameEdit.getText().toString());
                        userMap.put("phone", phoneEdit.getText().toString());
                        userMap.put("address", adressEdit.getText().toString());
                        userMap.put("image", myUrl);
                        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                        Toast.makeText(SettingsActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "No Image Selected.", Toast.LENGTH_SHORT).show();
        }


    }


    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", nameEdit.getText().toString());
        userMap.put("phone", phoneEdit.getText().toString());
        userMap.put("address", adressEdit.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile Info Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();


    }


}
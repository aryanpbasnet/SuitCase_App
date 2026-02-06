package com.example.suit_case;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import com.example.suit_case.databinding.ActivityAddItemsBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class AddItems extends AppCompatActivity {

    ActivityAddItemsBinding binding;
    DatabaseHelper itemDatabaseHelper;
    private Uri imageUri;

    public static Intent getIntent(Context context){
        return new Intent(context, AddItems.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imageUri = Uri.EMPTY;
        itemDatabaseHelper = new DatabaseHelper(this);

        binding.addPhoto.setOnClickListener(this::imagePicker);
        binding.addBtn.setOnClickListener(this::addItem);

        // Cancel button click listener to navigate back to MainActivity
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

    }

    // Add an item to the database
    private void addItem(View view){
        String item = binding.addItem.getText().toString();
        String itemPrice = binding.addPrice.getText().toString();
        String description = binding.addDescription.getText().toString();

        // null field validation
        if (item.isEmpty() || itemPrice.isEmpty() || description.isEmpty()){
            Toast.makeText(this, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = 0;
        try {
            price = Double.parseDouble(itemPrice);
        } catch (NumberFormatException e){
            Toast.makeText(this, "Price must be in numeric form", Toast.LENGTH_SHORT).show();
            return;
        }

        if (price <= 0){
            binding.addPrice.setError("Price can't be 0"); // item won't be added if price is less or equal to zero
            binding.addPrice.requestFocus();
            return;
        }

        if (itemDatabaseHelper.insertItems(item, price, description, imageUri.toString(), false)){
            Toast.makeText(this, "Item Added to SuitCase", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void imagePicker(View view) {
        ImagePickerUtility.imagePicker(view, AddItems.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null){
            imageUri = data.getData();
            binding.addPhoto.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
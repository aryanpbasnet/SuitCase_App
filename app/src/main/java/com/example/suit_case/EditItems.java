package com.example.suit_case;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.suit_case.databinding.ActivityEditItemsBinding;

public class EditItems extends AppCompatActivity {
    ActivityEditItemsBinding binding;
    private Uri imageUri;
    private int id;
    private boolean isPurchased;
    DatabaseHelper itemDatabaseHelper;

    public static final String ID = "id";
    public static final String ITEM = "item";
    public static final String PRICE = "price";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE = "image";
    public static final String PURCHASED = "purchased";

    public static Intent getIntent(Context context, SuitCaseModel suitCaseModel){
        Intent intent = new Intent(context, EditItems.class);
        intent.putExtra(ID, suitCaseModel.getId());
        intent.putExtra(ITEM, suitCaseModel.getItem());
        intent.putExtra(PRICE, suitCaseModel.getPrice());
        intent.putExtra(DESCRIPTION, suitCaseModel.getDescription());
        intent.putExtra(IMAGE, suitCaseModel.getImage().toString());
        intent.putExtra(PURCHASED, suitCaseModel.isPurchased());

        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        itemDatabaseHelper = new DatabaseHelper(this);

        // Retrieve data from the intent bundle
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt(ItemDetail.ID);
        isPurchased = bundle.getBoolean(ItemDetail.Purchased);
        String item = bundle.getString(ItemDetail.Item);
        String price = bundle.getString(ItemDetail.Price);
        String description = bundle.getString(ItemDetail.Description);
        imageUri = Uri.EMPTY;
        try {
            imageUri = Uri.parse(bundle.getString(ItemDetail.Image));
        } catch (NullPointerException e) {
            Toast.makeText(this, "Couldn't identify the image resource", Toast.LENGTH_SHORT).show();
        }

        binding.editItem.setText(item);
        binding.editPrice.setText(price);
        binding.editDescription.setText(description);
        binding.editPhoto.setImageURI(imageUri);

        binding.editPhoto.setOnClickListener(this::imagePicker);
        binding.editBtn.setOnClickListener(this::saveEditedItem);


    }

    private void imagePicker(View view){
        ImagePickerUtility.imagePicker(view, EditItems.this);
    }


    // Save the edited item
    private void saveEditedItem(View view){
        String item = binding.editItem.getText().toString();
        String itemPrice = binding.editPrice.getText().toString();
        String description = binding.editDescription.getText().toString();

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
            binding.editPrice.setError("Price can't be 0");
            binding.editPrice.requestFocus();
            return;
        }

        // Log and update the edited item in the database
        Log.d("EditItems", "saving: {" + "id: "+ id + ", item: "+ item +", price: "+ price +", description: "+ description +", imageUri: "+ imageUri.toString() +", isPurchased: "+ isPurchased +"}");
        if (itemDatabaseHelper.updateItem(id, item, price, description, imageUri.toString(), isPurchased)) {
            Toast.makeText(this, "Item Updated", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null){
            imageUri = data.getData();
            binding.editPhoto.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
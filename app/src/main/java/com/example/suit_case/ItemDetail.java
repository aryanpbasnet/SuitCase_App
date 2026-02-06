package com.example.suit_case;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.suit_case.databinding.ActivityItemDetailBinding;

public class ItemDetail extends AppCompatActivity {
    ActivityItemDetailBinding binding;
    SuitCaseModel suitCaseModel;
    DatabaseHelper itemDatabaseHelper;

    public static final String ID = "id";
    public static final String Item = "item";
    public static final String Price = "price";
    public static final String Description = "description";
    public static final String Image = "image";
    public static final String Purchased = "purchased";
    public static final int ITEM_EDIT_REQUEST = 10001;

    public static Intent getIntent(Context context, int id){
        Intent intent = new Intent(context, ItemDetail.class);
        intent.putExtra(ID, id);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        suitCaseModel = new SuitCaseModel();
        itemDatabaseHelper = new DatabaseHelper(this);


        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt(ItemDetail.ID);
        Log.d("ItemDetail: id:", id+"");

        suitCaseModel = retrieveData(id);   // Retrieve item details from the database
        binding.imageDetail.setImageURI(suitCaseModel.getImage());
        binding.itemDetail.setText(suitCaseModel. getItem());
        binding.priceDetail.setText(String.valueOf(suitCaseModel.getPrice()));
        binding.descriptionDetail.setText(suitCaseModel. getDescription());

        binding.editBtn.setOnClickListener(v -> onEditItem(v, suitCaseModel));
        binding.shareBtn.setOnClickListener(this::onShareItem);


    }

    private void onEditItem(View view, SuitCaseModel suitCaseModel){
        startActivity(EditItems.getIntent(getApplicationContext(), suitCaseModel));
    }

    private SuitCaseModel retrieveData(int id) {
        Cursor cursor = itemDatabaseHelper.getItemByID(id);
        cursor.moveToNext();

        SuitCaseModel suitCaseModels = new SuitCaseModel();
        suitCaseModels.setId(cursor.getInt(0));
        suitCaseModels.setItem(cursor.getString(1));
        suitCaseModels.setPrice(cursor.getDouble(2));
        suitCaseModels.setDescription(cursor.getString(3));
        suitCaseModels.setImage(Uri.EMPTY);
        try {
            Uri imageUri = Uri.parse(cursor.getString(4));
            suitCaseModels.setImage(imageUri);
        } catch (NullPointerException e){
            Toast.makeText(this, "Couldn't identify image resource", Toast.LENGTH_SHORT).show();
        }
        suitCaseModels.setPurchased(cursor.getInt(5) == 1);

        Log.d("ItemDetail:",suitCaseModels.toString());
        return suitCaseModels;
    }

    public void onShareItem(View view){
        String shareItem = "Item: " + suitCaseModel.getItem() + "\n" +
                "Price: " + suitCaseModel.getPrice() + "\n" +
                "Description: " + suitCaseModel.getDescription();


        Intent intent = new Intent(Intent.ACTION_SEND);  // Share the item details using Intent
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareItem);
        startActivity(Intent.createChooser(intent, "Share via"));
    }
}
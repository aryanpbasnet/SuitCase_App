package com.example.suit_case;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.suit_case.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;
    DatabaseHelper itemDatabaseHelper;
    ItemClickListener recyclerItemCLickListener;
    private ArrayList<SuitCaseModel> suitCaseModels;
    private SuitCaseAdapter suitCaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        suitCaseModels = new ArrayList<>();
        itemDatabaseHelper = new DatabaseHelper(this);

        // Set up RecyclerView and ItemTouchHelper
        setRecyclerView();
        setItemTouchHelper();


        // Handle FloatingActionButton click to open AddItems activity

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddItems.class);
                startActivity(i);
            }
        });

        // Set up NavigationView
        binding.navigationView.setNavigationItemSelectedListener(this);

        // Initialise ActionBarDrawerToggle for the Navigation Drawer

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
                R.string.open_navigation,
                R.string.close_navigation);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    // Handle NavigationView item selection
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;

            case R.id.feedback:
                sendFeedback();
                break;
            case R.id.shareApp:
                ShareConfirmationDialog();
                break;

            case R.id.logout:    // Handle logout option
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MainActivity.this, Login.class));
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void ShareConfirmationDialog(){  // Show a confirmation dialog for sharing the app
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Share SuitCase");
        builder.setMessage("Share SuitCase with your friends and family?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shareSuitCase();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Share the app
    private void shareSuitCase(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Download SuitCase on your mobile phones");
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    // Send users feedback to the developer via email
    private void sendFeedback(){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"abasnet@ismt.edu.np"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for SuitCase");
        intent.putExtra(Intent.EXTRA_TEXT, "Type your feedback here");

        try {
            startActivity(Intent.createChooser(intent, "Send Email"));
        } catch (ActivityNotFoundException e){
            Toast.makeText(this, "Can't find an email app", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    // Set up swipe gestures to delete or mark the items as purchased
    private void setItemTouchHelper(){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                SuitCaseModel suitCaseModel = suitCaseModels.get(position);
                if (direction==ItemTouchHelper.LEFT){
                    // Show a dialog to confirm whether users really want to delete the item
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Delete Item");
                    builder.setMessage("Are you sure you want to delete this item?");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            itemDatabaseHelper.deleteItem(suitCaseModel.getId());
                            suitCaseModels.remove(position);
                            suitCaseAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                            Toast.makeText(MainActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            suitCaseAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else if (direction==ItemTouchHelper.RIGHT){
                    // Show a dialog to confirm whether users really want to mark the item as purchased
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Purchase Item");
                    builder.setMessage("Are you sure you want to mark this item as purchased?");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            suitCaseModel.setPurchased(true);
                            itemDatabaseHelper.updateItem(suitCaseModel.getId(), suitCaseModel.getItem(),
                                    suitCaseModel.getPrice(), suitCaseModel.getDescription(),
                                    suitCaseModel.getImage().toString(),suitCaseModel.isPurchased());
                            suitCaseAdapter.notifyItemChanged(position);
                            Toast.makeText(MainActivity.this, "Item Purchased", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            suitCaseAdapter.notifyItemChanged(position);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            @Override

            // set up decorator for item viewholder
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightActionIcon(R.drawable.purchased)
                        .addSwipeRightBackgroundColor(Color.GREEN)
                        .addSwipeLeftActionIcon(R.drawable.delete)
                        .addSwipeLeftBackgroundColor(Color.RED)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }
        });
        itemTouchHelper.attachToRecyclerView(binding.itemsRecyclerView);
    }

    @Override
    protected void onStart(){
        super.onStart();
        retrieveData();
    }

    private void retrieveData(){
        Cursor cursor = itemDatabaseHelper.getAllItems();
        if (cursor==null){
            return;
        }
        suitCaseModels.clear();
        while (cursor.moveToNext()) {
            SuitCaseModel suitCaseModel = new SuitCaseModel();
            suitCaseModel.setId(cursor.getInt(0));
            suitCaseModel.setItem(cursor.getString(1));
            suitCaseModel.setPrice(cursor.getDouble(2));
            suitCaseModel.setDescription(cursor.getString(3));
            suitCaseModel.setImage(Uri.parse(cursor.getString(4)));

            suitCaseModels.add(cursor.getPosition(), suitCaseModel);
            suitCaseAdapter.notifyItemChanged(cursor.getPosition());
            Log.d("MainActivity", "Items" + suitCaseModel.getId() + "added at " + cursor.getPosition());
        }
    }

    private void setRecyclerView(){
            suitCaseAdapter = new SuitCaseAdapter(suitCaseModels, (view, position)->startActivity(ItemDetail.getIntent(getApplicationContext(),suitCaseModels.get(position).getId())));
            binding.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            binding.itemsRecyclerView.setAdapter(suitCaseAdapter);

        }
    }
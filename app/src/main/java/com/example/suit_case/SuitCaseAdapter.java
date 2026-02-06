package com.example.suit_case;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SuitCaseAdapter extends RecyclerView.Adapter<SuitCaseAdapter.ItemViewHolder> {
    private final ItemClickListener recyclerItemsClickListener;
    private ArrayList<SuitCaseModel> suitCaseModels;


    public SuitCaseAdapter(ArrayList<SuitCaseModel>suitCaseModels, ItemClickListener recyclerItemsClickListener){
        this.recyclerItemsClickListener = recyclerItemsClickListener;
        this.suitCaseModels = suitCaseModels;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_viewer,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuitCaseAdapter.ItemViewHolder holder, int position) {
        SuitCaseModel suitCaseModel = suitCaseModels.get(position);
        holder.txt_name.setText(suitCaseModel.getItem());
        holder.txt_price.setText(String.valueOf(suitCaseModel.getPrice()));
        holder.txt_description.setText(suitCaseModel.getDescription());
        Uri imageUri=suitCaseModel.getImage();

        if (suitCaseModel.isPurchased()) {
            // If the item is marked as purchased, change the background color
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.imageView.getContext(), R.color.purchased));
        }

        // Load and display the item's image if available
        if (imageUri !=null){
            holder.imageView.setImageURI(imageUri);
        }
    }

    @Override
    public int getItemCount() {
        return suitCaseModels.size();
    }

    // View holder class for individual items
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView txt_name,txt_price,txt_description;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item_img);
            txt_name=itemView.findViewById(R.id.item);
            txt_price=itemView.findViewById(R.id.item_price);
            txt_description=itemView.findViewById(R.id.item_description);
            itemView.setOnClickListener(this::itemViewOnClick);
        }
        private void itemViewOnClick(View view){
            recyclerItemsClickListener.OnItemClick(view,getAdapterPosition());
        }
    }
}

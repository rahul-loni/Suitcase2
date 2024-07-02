package com.example.suitcase2.Adapter;

import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase2.ItemsModel;
import com.example.suitcase2.R;

import java.util.ArrayList;

public class Items_Adapter extends RecyclerView.Adapter<Items_Adapter.ItemViewHolder> {
    private final RecyclerViewItemClick recyclerViewItemClick;
    private ArrayList<ItemsModel>itemsModels;

    public Items_Adapter(ArrayList<ItemsModel> itemsModels, RecyclerViewItemClick recyclerViewItemClick) {
        this.recyclerViewItemClick = recyclerViewItemClick;
        this.itemsModels = itemsModels;
    }

    @NonNull
    @Override
    public Items_Adapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item1,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Items_Adapter.ItemViewHolder holder, int position) {
     ItemsModel itemsModel=itemsModels.get(position);
     holder.txt_name.setText(itemsModel.getName());
     if (itemsModel.isPurchased()){
         holder.txt_name.setCompoundDrawablesWithIntrinsicBounds
                 (0,0,R.drawable.ic_check,0);

     }
     holder.txt_price.setText(String.valueOf(itemsModel.getPrice()));
     holder.txt_Description.setText(itemsModel.getDescription());
     Uri imageUri=itemsModel.getImage();
     if(imageUri !=null){
         holder.imageView.setImageURI(imageUri);
     }

    }

    @Override
    public int getItemCount() {
        return itemsModels.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
     ImageView imageView;
     TextView txt_name,txt_price,txt_Description;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item_image);
            txt_name=itemView.findViewById(R.id.item_dis);
            txt_price=itemView.findViewById(R.id.item_price);
            txt_Description=itemView.findViewById(R.id.itemDescription);
            itemView.setOnClickListener(this::itemViewOnClick);
        }

        private void itemViewOnClick(View view) {
            recyclerViewItemClick.onItemClick(view, getAdapterPosition());
        }
    }
}

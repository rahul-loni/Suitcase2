package com.example.suitcase2;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Add_Items extends AppCompatActivity {
     DatabaseHelper databaseHelper;
     private Uri imageUri;
     ImageView pic_Image,pic_map;
     Button add_item;
     TextView map_txt;
     EditText txt_item_name,txt_item_price,txt_item_description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseHelper=new DatabaseHelper(this);
        imageUri=Uri.EMPTY;
        pic_Image=findViewById(R.id.item_image);
        pic_map=findViewById(R.id.img_map);
        add_item=findViewById(R.id.btn_add_items);
        map_txt=findViewById(R.id.txt_map);
        txt_item_name=findViewById(R.id.add_item_name);
        txt_item_price=findViewById(R.id.add_item_price);
        txt_item_description=findViewById(R.id.add_item_description);

        pic_Image.setOnClickListener(this::picImage);
        pic_map.setOnClickListener(this::picMap);
        add_item.setOnClickListener(this::saveItems);


    }
    //save image in Database with image and Location
    private void saveItems(View view){
        String name=txt_item_name.getText().toString().trim();
        if (name.isEmpty()){
            txt_item_name.setError("Name Field is Empty");
            txt_item_name.requestFocus();
        }
        double price=0;
        try {
            price=Double.parseDouble(txt_item_price.getText().toString().trim());
        }catch (NullPointerException e){
            Toast.makeText(this, "Something wrong with price", Toast.LENGTH_SHORT).show();
        }catch (NumberFormatException e){
            Toast.makeText(this, "Price should be number ", Toast.LENGTH_SHORT).show();
        }
        if (price>=0){
            txt_item_price.setError("price should be grater than 0");
            txt_item_price.requestFocus();
        }
        String description=txt_item_description.getText().toString().trim();
        if (description.isEmpty()){
            txt_item_description.setError("Description field is empty  ");
            txt_item_description.requestFocus();
        }
        if (databaseHelper.insertItems(name,price,description,imageUri.toString(),false)){
            Toast.makeText(this, "Data Save Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    //Pic Image From Camera and Gallery
   private void picImage(View view){

   }

   //Pic Location from Google map
   private void picMap(View view){

   }
}
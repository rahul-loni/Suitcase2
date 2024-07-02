package com.example.suitcase2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Items_Description extends AppCompatActivity {
    public static final String ID="id";
    public static final String NAME="name";
    public static final String PRICE="price";
    public static final String DESCRIPTION="description";
    public static final String IMAGE="image";
    public static final String IS_PURCHASED="purchased";
    public static final int EDIT_ITEM_REQUEST=10001;
    public static Intent getIntent(Context context,int id){
        Intent intent=new Intent(context,Items_Description.class);
        intent.putExtra(ID,id);
        return intent;
    }
    ItemsModel item;
    DatabaseHelper itemsDbHelper;
    ImageView imageViewItem;
    TextView textViewName,textViewPrice,textViewDescription;
    Button buttonEditItem,buttonShareItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_items_description);

        imageViewItem=findViewById(R.id.des_img);
        textViewName=findViewById(R.id.dec_name);
        textViewPrice=findViewById(R.id.dec_price);
        textViewDescription=findViewById(R.id.dec_description);

        item=new ItemsModel();
        itemsDbHelper=new DatabaseHelper(this);

        Bundle bundle=getIntent().getExtras();
        int id=bundle.getInt(Items_Description.ID);
        Log.d("Items_Detail_Page: id:",id+"");

        item=retrieveData(id);
        imageViewItem.setImageURI(item.getImage());
        textViewName.setText(item.getName());
        textViewPrice.setText(String.valueOf(item.getPrice()));
        textViewDescription.setText(item.getDescription());

       buttonEditItem.setOnClickListener(v -> startEditItems(v ,item));

        //click Method of Share Button
       buttonShareItem.setOnClickListener(this::startShareItemActivity);
    }
    public void startEditItems(View v  , ItemsModel item){
        startActivity(EditItems.getIntent(getApplicationContext(),item));
    }
    private ItemsModel retrieveData(int id) {
        Cursor cursor=itemsDbHelper.getItemById(id);
        cursor.moveToNext();
        ItemsModel itemsModel=new ItemsModel();
        itemsModel.setId(cursor.getInt(0));
        itemsModel.setName(cursor.getString(1));
        itemsModel.setPrice(cursor.getDouble(2));
        itemsModel.setDescription(cursor.getString(3));
        itemsModel.setImage(Uri.EMPTY);
        try {
            Uri imageUri=Uri.parse(cursor.getString(4));
            itemsModel.setImage(imageUri);
        }catch (NullPointerException e){
            Toast.makeText
                            (this, "Error occurred is identifying image resource  ",
                                    Toast.LENGTH_SHORT)
                    .show();
        }
        itemsModel.setPurchased(cursor.getInt(5)==1);
        Log.d("Items_Details_Page",itemsModel.toString());
        return itemsModel;
    }
    public void startShareItemActivity(View view){
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"");
        intent.putExtra(Intent.EXTRA_TEXT,"Check Your Cool Application ");
        startActivity(Intent.createChooser(intent,"Share via"));
    }
}
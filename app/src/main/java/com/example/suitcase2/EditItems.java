package com.example.suitcase2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditItems extends AppCompatActivity {
    public static final String ID="id";
    public static final String NAME="name";
    public static final String PRICE="price";
    public static final String DESCRIPTION="description";
    public static final String IMAGE="image";
    public static final String IS_PURCHASED="purchased";

    private DatabaseHelper items_dbHelper;
    private Uri imageUri;
    private int id;
    private boolean isPurchased;
    Button btnEdit;
    EditText editItemName,editItemPrice,editItemDescription;
    ImageView editItemImage;

    public static Intent getIntent(Context context,ItemsModel itemsModel){
        Intent intent=new Intent(context,EditItems.class);
        intent.putExtra(ID ,itemsModel.getId());
        intent.putExtra(NAME,itemsModel.getName());
        intent.putExtra(PRICE,itemsModel.getPrice());
        intent.putExtra(DESCRIPTION,itemsModel.getDescription());
        intent.putExtra(IMAGE,itemsModel.getImage().toString());
        intent.putExtra(IS_PURCHASED,itemsModel.isPurchased());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_items);

        btnEdit=findViewById(R.id.btn_edit);
        editItemName=findViewById(R.id.edit_item_name);
        editItemPrice=findViewById(R.id.edit_item_price);
        editItemDescription=findViewById(R.id.edit_item_description);
        editItemImage=findViewById(R.id.edit_item_image);

        editItemImage.setOnClickListener(this::pickImage);
        btnEdit.setOnClickListener(this::saveItem);

    }
    private void pickImage(View view){
        ImagePickUtility.picImage(view,EditItems.this);
    }
    private void saveItem(View view){
        String name=editItemName.getText().toString().trim();
        if (name.isEmpty()){
           editItemName.setError("Name field is empty");
           editItemName.requestFocus();
        }
        double price =0 ;
        try {
            price=Double.parseDouble(editItemPrice.getText().toString().trim());
        }catch (NullPointerException e){
            Toast.makeText(this, "Something wrong with price ", Toast.LENGTH_SHORT).show();
        }catch (NumberFormatException e){
            Toast.makeText(this, "Price should be a number ", Toast.LENGTH_SHORT).show();
        }
        if (price<=0){
            editItemPrice.setError("Price should be greater than 0");
            editItemPrice.requestFocus();
        }
        String description= editItemDescription.getText().toString().trim();
        if (description.isEmpty()){
            editItemDescription.setError("Description is empty ");
            editItemDescription.requestFocus();
        }
        Log.d("EditItem","saving : {"+ "id:"+id+",name:"+name+",price:"+price+"" +
                ",description:"+description+",imageUri:"+imageUri.toString()+"" +
                ",isPurchased:"+isPurchased+"}");
        if (items_dbHelper.update(id,name,price,description,imageUri.toString(),isPurchased)){
            Toast.makeText(this, "Saves Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }else {
            Toast.makeText(this, "Failed to save ", Toast.LENGTH_SHORT).show();
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data!=null){
            imageUri=data.getData();
            editItemImage.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void finish(){
        super.finish();
    }
}
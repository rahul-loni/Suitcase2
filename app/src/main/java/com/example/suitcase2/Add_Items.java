package com.example.suitcase2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class Add_Items extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    private Uri imageUri;
    ImageView pic_Image, pic_map;
    Button add_item;
    TextView map_txt;
    EditText txt_item_name, txt_item_price, txt_item_description;

    WifiManager wifiManager;

    private final static int PLACE_PICKER_REQUEST = 999;
    private final static int IMAGE_PICKER_REQUEST = 998;

    public static Intent getIntent(Context context) {
        return new Intent(context, Add_Items.class);
    }

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

        databaseHelper = new DatabaseHelper(this);
        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        imageUri = Uri.EMPTY;

        pic_Image = findViewById(R.id.add_item_img);
        pic_map = findViewById(R.id.img_map);
        add_item = findViewById(R.id.btn_add_items);
        map_txt = findViewById(R.id.txt_map);
        txt_item_name = findViewById(R.id.add_item_name);
        txt_item_price = findViewById(R.id.add_item_price);
        txt_item_description = findViewById(R.id.add_item_description);

        pic_Image.setOnClickListener(this::picImage);
        pic_map.setOnClickListener(this::picMap);
        add_item.setOnClickListener(this::saveItems);


    }

    //save image in Database with image and Location
    private void saveItems(View view) {
        String name = txt_item_name.getText().toString().trim();
        if (name.isEmpty()) {
            txt_item_name.setError("Name Field is Empty");
            txt_item_name.requestFocus();
            return;
        }
        double price = 0;
        try {
            price = Double.parseDouble(txt_item_price.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Price should be number ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (price < 0) {
            txt_item_price.setError("Price should be grater than 0");
            txt_item_price.requestFocus();
            return;
        }
        String description = txt_item_description.getText().toString().trim();
        if (description.isEmpty()) {
            txt_item_description.setError("Description field is empty");
            txt_item_description.requestFocus();
            return;
        }
        String location = map_txt.getText().toString().trim();
        if (location.isEmpty()) {
            Toast.makeText(this, "Please select location", Toast.LENGTH_SHORT).show();
            return;
        }
        if (databaseHelper.insertItems(name, price, description, imageUri.toString(), false, location)) {
            Toast.makeText(this, "Data Save Successfully", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);

        }
    }

    //Pic Image From Camera and Gallery
    private void picImage(View view) {
        ImagePickUtility.picImage(view, Add_Items.this, IMAGE_PICKER_REQUEST);
    }

    //Pic Location from Google map
    private void picMap(View view) {
        Log.d("PickMap", "Here");
//        wifiManager.setWifiEnabled(false);

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);

            //Enable Wifi
            wifiManager.setWifiEnabled(true);

        } catch (GooglePlayServicesRepairableException e) {
            Log.e("Exception", e.getMessage());
            Toast.makeText(this, "An error occurred while picking location.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.d("Exception", e.getMessage());
            Toast.makeText(this, "An error occurred while picking location.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case PLACE_PICKER_REQUEST:
                        Place place = PlacePicker.getPlace(getApplicationContext(), data);

                        map_txt.setText(place.getAddress());

                    case IMAGE_PICKER_REQUEST:
                        imageUri = data.getData();
                        pic_Image.setImageURI(imageUri);
                }

            }
        }
    }
}
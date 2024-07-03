package com.example.suitcase2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class EditItems extends AppCompatActivity {
    public static final String ID = "id";

    private DatabaseHelper databaseHelper;
    private ItemsModel itemsModel;
    Button btnEdit;
    EditText editItemName, editItemPrice, editItemDescription;
    ImageView imgItemImage, imgLocation;
    TextView txtLocation;
    WifiManager wifiManager;
    private Bundle intentData;

    private final static int PLACE_PICKER_REQUEST = 999;
    private final static int IMAGE_PICKER_REQUEST = 998;

    public static Intent getIntent(Context context, ItemsModel itemsModel) {
        Intent intent = new Intent(context, EditItems.class);
        intent.putExtra(ID, itemsModel.getId());

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_items);

        databaseHelper = new DatabaseHelper(this);
        intentData = getIntent().getExtras();
        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        btnEdit = findViewById(R.id.btn_edit);
        editItemName = findViewById(R.id.edit_item_name);
        editItemPrice = findViewById(R.id.edit_item_price);
        editItemDescription = findViewById(R.id.edit_item_description);
        imgItemImage = findViewById(R.id.edit_item_image);
        imgLocation = findViewById(R.id.imgLocationPicker);
        txtLocation = findViewById(R.id.txtLocation);

        imgItemImage.setOnClickListener(this::pickImage);
        imgLocation.setOnClickListener(this::pickLocation);
        btnEdit.setOnClickListener(this::saveItem);
    }

    private void pickLocation(View view) {
        wifiManager.setWifiEnabled(false);

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

    @Override
    protected void onStart() {
        super.onStart();
        int id = intentData.getInt(ID);

        populateData(id);
    }

    private void populateData(int id) {
        Cursor cursor = databaseHelper.getItemById(id);
        if (cursor != null) {
            cursor.moveToFirst();
            itemsModel = new ItemsModel();
            itemsModel.setId(id);
            itemsModel.setName(cursor.getString(1));
            itemsModel.setPrice(cursor.getDouble(2));
            itemsModel.setDescription(cursor.getString(3));
            itemsModel.setImage(Uri.parse(cursor.getString(4)));
            itemsModel.setPurchased(cursor.getInt(5) == 1);
            itemsModel.setLocation(cursor.getString(6));
        } else {
            Toast.makeText(getApplicationContext(), "Could not load data",
                    Toast.LENGTH_SHORT).show();
        }

        editItemName.setText(itemsModel.getName());
        editItemPrice.setText(itemsModel.getPrice() + "");
        editItemDescription.setText(itemsModel.getDescription());
        imgItemImage.setImageURI(itemsModel.getImage());
        txtLocation.setText(itemsModel.getLocation());
    }


    private void pickImage(View view) {
        ImagePickUtility.picImage(view, EditItems.this, IMAGE_PICKER_REQUEST);
    }

    private void saveItem(View view) {
        String name = editItemName.getText().toString().trim();
        if (name.isEmpty()) {
            editItemName.setError("Name field is empty");
            editItemName.requestFocus();
            return;
        }
        itemsModel.setName(name);

        double price;
        try {
            price = Double.parseDouble(editItemPrice.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Price should be a number ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (price <= 0) {
            editItemPrice.setError("Price should be greater than 0");
            editItemPrice.requestFocus();
            return;
        }
        itemsModel.setPrice(price);

        String description = editItemDescription.getText().toString().trim();
        if (description.isEmpty()) {
            editItemDescription.setError("Description is empty ");
            editItemDescription.requestFocus();
            return;
        }
        itemsModel.setDescription(description);

        if (itemsModel.getLocation().trim().isEmpty()) {
            Toast.makeText(this, "Please select location", Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseHelper.update(itemsModel.getId(), itemsModel.getName(), itemsModel.getPrice(), itemsModel.getDescription(), itemsModel.getImage().toString(), itemsModel.isPurchased(), itemsModel.getLocation())) {
            Toast.makeText(this, "Saves Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            Toast.makeText(this, "Failed to save ", Toast.LENGTH_SHORT).show();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case PLACE_PICKER_REQUEST:
                        Place place = PlacePicker.getPlace(getApplicationContext(), data);
                        String address = place.getAddress().toString();
                        itemsModel.setLocation(address);
                        txtLocation.setText(address);

                    case IMAGE_PICKER_REQUEST:
                        Uri imageUri = data.getData();
                        itemsModel.setImage(imageUri);
                        imgItemImage.setImageURI(imageUri);
                }

            }
        }
    }

    public void finish() {
        super.finish();
    }
}
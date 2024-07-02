package com.example.suitcase2;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase2.Adapter.Items_Adapter;
import com.example.suitcase2.Adapter.RecyclerViewItemClick;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper items_dbHelper;
    private Items_Adapter itemsAdapter;
    private ArrayList<ItemsModel> itemsModels;
    private RecyclerViewItemClick recyclerViewItemClick;
    NavigationView nav;
    FloatingActionButton fab;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        nav = findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.nav_home) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Click to home ", Toast.LENGTH_SHORT).show();
                }
                if (id == R.id.nav_about) {
                    Toast.makeText(MainActivity.this, "Clickto about ", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        final DrawerLayout drawerLayout = findViewById(R.id.drawer);
        findViewById(R.id.nav_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        itemsModels = new ArrayList<>();
        items_dbHelper = new DatabaseHelper(this);
        setRecyclerView();
        setupItemTouchHelper();
        fab = findViewById(R.id.fab);
        recyclerView=findViewById(R.id.recycler);
        fab.setOnClickListener(view -> startActivity(Add_Items.getIntent(getApplicationContext())));

    }
    private void setupItemTouchHelper() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        ItemsModel itemsModel = itemsModels.get(position);
                        if (direction == ItemTouchHelper.LEFT) {
                            items_dbHelper.deleteItem(itemsModel.getId());
                            itemsModels.remove(position);
                            itemsAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                            Toast.makeText(MainActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                        } else if (direction == ItemTouchHelper.RIGHT) {
                            itemsModel.setPurchased(true);
                            items_dbHelper.update(
                                    itemsModel.getId(),
                                    itemsModel.getName(),
                                    itemsModel.getPrice(),
                                    itemsModel.getDescription(),
                                    itemsModel.getImage().toString(),
                                    itemsModel.isPurchased()
                            );
                            itemsAdapter.notifyItemChanged(position);
                            Toast.makeText(MainActivity.this, "Item is Updated ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    protected void onStart() {
        super.onStart();
        retrieveData();
    }
    private void retrieveData(){
        Cursor cursor=items_dbHelper.getAllItem();
        if (cursor==null){
            return;
        }
        itemsModels.clear();
        while (cursor.moveToNext()){
            ItemsModel itemsModel=new ItemsModel();
            itemsModel.setId(cursor.getInt(0));
            itemsModel.setName(cursor.getString(1));
            itemsModel.setPrice(cursor.getDouble(2));
            itemsModel.setDescription(cursor.getString(3));
            itemsModel.setImage(Uri.parse(cursor.getString(4)));

            itemsModels.add(cursor.getPosition(),itemsModel);
            itemsAdapter.notifyItemChanged(cursor.getPosition());
            Log.d("MainActivity","Items" +itemsModel.getId()+"added at "+cursor.getPosition());
        }
    }
    private void setRecyclerView(){
        itemsAdapter=new Items_Adapter(itemsModels,
                (view ,position)->startActivity(Items_Description.getIntent(
                        getApplicationContext(),
                        itemsModels.get(position).getId())
                ));
       recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemsAdapter);
    }
}
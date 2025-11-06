package com.example.rimasbiy;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeScreen extends AppCompatActivity {
    private SearchView searchV;
    private Button btnR7;
    private Button btnR8;
    private Button btnR9;
    private Button btnR10;
    private Button btnR11;
    private Button btnR12;
    private ImageView imageV5;
    private ImageView imageV6;
    private ImageView imageV7;
    private ImageView imageV8;
    private ImageView imageV9;
    private ImageView imageV10;
    private TextView tv4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        searchV = findViewById(R.id.searchV);
        btnR7 = findViewById(R.id.btvR7);
        btnR8 = findViewById(R.id.btnR8);
        btnR9 = findViewById(R.id.btnR9);
        btnR10 = findViewById(R.id.btnR10);
        btnR11 = findViewById(R.id.btnR11);
        btnR12 = findViewById(R.id.btnR12);
        imageV5 = findViewById(R.id.imageV5);
        imageV6 = findViewById(R.id.imageV6);
        imageV7 = findViewById(R.id.imageV7);
        imageV8 = findViewById(R.id.imageV8);
        imageV9 = findViewById(R.id.imageV9);
        imageV10 = findViewById(R.id.imageV10);
        tv4 = findViewById(R.id.tv4);
        btnR7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeScreen.this, IngredientsList.class);
                startActivity(i);
            }
        });
        btnR8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeScreen.this, IngredientsList.class);
                startActivity(i);
            }
        });
        btnR9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeScreen.this, IngredientsList.class);
                startActivity(i);
            }
        });
        btnR10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeScreen.this, IngredientsList.class);
                startActivity(i);
            }
        });
        btnR11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeScreen.this, IngredientsList.class);
                startActivity(i);
            }
        });
        btnR12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeScreen.this, IngredientsList.class);
                startActivity(i);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.the_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId()==R.id.itmFavorites)
        {
            Toast.makeText(this,"Favorites",Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId()==R.id.itmSettings)
        {
            Toast.makeText(this,"Settings",Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId()==R.id.itmitem)
        {
            Toast.makeText(this,"itemm",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}
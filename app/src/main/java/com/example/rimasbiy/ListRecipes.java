package com.example.rimasbiy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rimasbiy.MyRecipeTable.MyRecipeAdapter;
import com.example.rimasbiy.MyRecipeTable.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListRecipes extends AppCompatActivity {
  private   FloatingActionButton btnFav;
  private ListView lsViRecipes;
  private MyRecipeAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lsViRecipes = findViewById(R.id.lsViRecipes);
        adapter = new MyRecipeAdapter(this, R.layout.activity_list_recipes);
        lsViRecipes.setAdapter(adapter);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_recipes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnFav = findViewById(R.id.adFab);
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListRecipes.this, Add.class);
                startActivity(i);
            }
        });
    }
}
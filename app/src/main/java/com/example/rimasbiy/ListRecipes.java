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
import com.example.rimasbiy.data.AppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListRecipes extends AppCompatActivity {
  private   FloatingActionButton btnFav;
  private ListView lsViRecipes;
  private MyRecipeAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_recipes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lsViRecipes = findViewById(R.id.lsViRecipes);
        adapter = new MyRecipeAdapter(this, R.layout.activity_list_recipes);
        lsViRecipes.setAdapter(adapter);
        btnFav = findViewById(R.id.adFab);
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListRecipes.this, Add.class);
                startActivity(i);
            }
        });
    }
    //استخراج معطيات (حسب قاعدة البيانات وعرضها على listview)
    protected void onResume() {
        super.onResume();
        //  ....استخراج جميع الوصفات
        List<Recipe> recipes = AppDatabase.getInstance(this).myRecipeQuery().getAll();
        // .... تنظيف المنسق من جميع المعطيات السابقة
        adapter.clear();
        //اضافة المعطيات الجديدة
        adapter.addAll(recipes);
        //.... تحديث المنسق الجديد
        adapter.notifyDataSetChanged();
    }

}
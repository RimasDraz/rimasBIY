package com.example.rimasbiy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rimasbiy.MyRecipeTable.MyRecipeAdapter;
import com.example.rimasbiy.MyRecipeTable.Recipe;
import com.example.rimasbiy.data.AppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListRecipes extends AppCompatActivity {
  private   FloatingActionButton btnFav; // زر الاضافة
  private ListView lsViRecipes;// القائمة
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
    //استخدمناها لضمان تحديث البيانات (Refreshing) تلقائيا
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
    private void getAllFromFirebase( MyRecipeAdapter adapter) {
        //عنوان قاعدة البيانات
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // عنوان مجموعة المعطيات داخل قاعدة البيانات
        DatabaseReference myRef = database.getReference("tasks");
//إضافة listener مما يسبب الإصغاء لكل تغيير حتلنة عرض المعطيات//
        myRef.addValueEventListener(new ValueEventListener() {
            @Override//دالة معالج حدث تقوم بتلقى نسخة عن كل المعطيات عند أي تغيير
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear();//حذف كل المعطيات بالوسيط
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    //  استخراج كل المعطيات على وتحويلها لكائن ملائم//
                    Recipe recipe = taskSnapshot.getValue(Recipe.class);
                    adapter.add(recipe);//اضافة كل معطى (كائن) للمنسق
                }
                adapter.notifyDataSetChanged();//اعلام المنسق بالتغيير
                Toast.makeText(ListRecipes.this, "Data fetched successfully", Toast.LENGTH_SHORT).show();
            }
            @Override//بحالة فشل استخراج المعطيات
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListRecipes.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
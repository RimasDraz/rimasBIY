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
    protected void onCreate(Bundle savedInstanceState) {//بتشتغل أول ما تنفتح شاشة عرض الوصفات
        super.onCreate(savedInstanceState);


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_recipes);//تحديد ملف التننسيق للشاشة, بناء الكائنات
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lsViRecipes = findViewById(R.id.lsViRecipes);
        adapter = new MyRecipeAdapter(this, R.layout.activity_list_recipes);//  activity_list_recipes  تعريف الوسيط (Adabter)المسؤول عن تحويل كائنات الوصفة الى عناصر مرئية بناء على التصميم
        lsViRecipes.setAdapter(adapter);                                           //ربطه  بالlistview
//هون عم نربط عناصر الواجهة (EditText, Button...) بالكود عشان نقدر نتحكم فيهم
        btnFav = findViewById(R.id.adFab);
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//لما نضغطه ➜ ننتقل لشاشة Add
                Intent i = new Intent(ListRecipes.this, Add.class);
                startActivity(i);
            }
        });
    }
    //استخراج معطيات (حسب قاعدة البيانات وعرضها على listview)
    //استخدمناها لضمان تحديث البيانات (Refreshing) تلقائيا
    //تجيب كل الوصفات من قاعدة البيانات
    protected void onResume() {//بتشتغل كل مرة نرجع فيها لهاي الشاشة
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
        FirebaseDatabase database = FirebaseDatabase.getInstance();//تتصل بقاعدة البيانات
        // عنوان مجموعة المعطيات داخل قاعدة البيانات
        DatabaseReference myRef = database.getReference("Recipes");
//إضافة listener مما يسبب الإصغاء لكل تغيير حتلنة عرض المعطيات//
        //يسمع لأي تغيير يصير في Firebase ويحدث القائمة تلقائي
        myRef.addValueEventListener(new ValueEventListener() {//in database  myRef تقوم الدالة بتجديث واجهة المستخدم ul تلقائيا في كل مرة يتغير فيها اي معطى داخل المسار
            @Override//دالة معالج حدث تقوم بتلقى نسخة عن كل المعطيات عند أي تغيير
            public void onDataChange(@NonNull DataSnapshot snapshot) {//تحدث القائمة تلقائياً
                adapter.clear();//حذف كل المعطيات بالوسيط
                for (DataSnapshot recipeSnapshot : snapshot.getChildren()) {// يتم استخدام الحلقة للمرور على كل "وصفة" موجودة تحت المسار recipe
                    //  استخراج كل المعطيات على وتحويلها لكائن ملائم//
                    Recipe recipe = recipeSnapshot.getValue(Recipe.class);// للحصول على كائن الوصفة, نستخدم السطر حيث يقوم ال Firebase بتحويلها تلقائيا لكائن java
                    adapter.add(recipe);//اضافة كل معطى (كائن) للمنسق
                }
                adapter.notifyDataSetChanged();//اعلام المنسق بالتغيير
                Toast.makeText(ListRecipes.this, "Data fetched successfully", Toast.LENGTH_SHORT).show();
            }
            @Override
            //بحالة فشل استخراج المعطيات
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListRecipes.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
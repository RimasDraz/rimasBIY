package com.example.rimasbiy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rimasbiy.MyRecipeTable.MyRecipeAdapter;
import com.example.rimasbiy.MyRecipeTable.Recipe;
import com.example.rimasbiy.data.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * מסך הצגת רשימת המתכונים.
 * شاشة عرض قائمة الوصفات.
 */

//todo add bottom menu my recipes
public class ListRecipes extends AppCompatActivity {
    private ListView lsViRecipes;
    private MyRecipeAdapter adapter;
    private BottomNavigationView bottomNavigationView;
    private String group;// اسم المجموعة الي بطلع منها المعطيات

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_recipes);

        // אתחול רכיבי הממשק - تهيئة عناصر الواجهة
        lsViRecipes = findViewById(R.id.lsViRecipes);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // הגדרת המתאם (Adapter) - استخدام التصميم الصحيح لكل عنصر في القائمة
        adapter = new MyRecipeAdapter(this, R.layout.recipe_item_layout);
        lsViRecipes.setAdapter(adapter);

        // הגדרת מאזין לתפריט התחתון - إعداد مستمع لنقرات القائمة السفلية
        setupBottomNavigation();
    }
//معالجة حدث للقائمة السفلى
    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_all) {
                loadAllRecipes(); // הצגת הכל - عرض الكل
                return true;
            } else if (itemId == R.id.nav_favorites) {
                showFavorites(); // הצגת מועדפים - عرض المفضلة
                return true;
            } else if (itemId == R.id.nav_add) {
                // מעבר למסך הוספה - الانتقال لشاشة الإضافة
                startActivity(new Intent(ListRecipes.this, Add.class));
                return true;
            } else if (itemId == R.id.nav_logout) {
                // יציאה וחזרה למסך התחברות - تسجيل الخروج والعودة للبداية
                showYesNoDialog();

                return true;
            } else if (itemId == R.id.nav_myrecipes) {
                showMyRecipe();
                return true;
            }
            return false;
        });
    }
    //ظهور شاشة لتسجيل الدخول اذا بده يسجل دخول او لا
    public void showYesNoDialog()
    {
        //تجهيز بناء شبكة حوار "ديالوغ" بتلقى برامتر مؤشر للنشاط الحالي
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Log Out");//تحديد العنوان
        builder.setMessage("Are you sure?");//تحدي فحوى شباك الديالوغ
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {//اضافة زر مع اللسينر
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //معالجة حدث للموافقة
                Toast.makeText(ListRecipes.this,"signing out",Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ListRecipes.this, SignIn.class));
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ListRecipes.this,"signing out",Toast.LENGTH_SHORT).show();

            }
        });
        AlertDialog dialog=builder.create();//بناء شباك الديالوغ
        dialog.show();//عرض الشباك
    }

    // بتشتغل عند فتح التطبيق بعد oncreat & onstart
    //عند الرجوع للتطبيق اذا كنت تستعمل تطبيق اخر او بمحل ورجعت على التطبيق
    //عن اغلاق الشاشة اذا ظهر ديالوج او اتصال هاتفي غطى الشاشة ثم اختفى برجع النشاط للدالة هاي
    @Override
    protected void onResume() {
        super.onResume();
        // רענון הנתונים בכל פעם שחוזרים למסך - تحديث البيانات عند العودة للشاشة
        loadAllRecipes();
    }

    /**
     * טעינת כל המתכונים ממסד הנתונים המקומי (Room).
     * جلب كافة الوصفات من قاعدة البيانات المحلية.
     */
    private void loadAllRecipes() {
        group="recipes";
        getAllFromFirebase(adapter);
    }
    /**
     * פונקציה להצגת מועדפים בלבד.
     * دالة لعرض الوصفات المفضلة فقط.
     */
    private void showFavorites() {
        group="favorites";
        getAllFromFirebase(adapter);
    }
    /**
     * دالة لعرض وصفاتي فقط.
     */
    private void showMyRecipe(){
        String myId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        group="recipes/"+myId;
        getAllFromFirebase(adapter);
    }
    private void getAllFromFirebase( MyRecipeAdapter adapter) {// جلب البيانات وعرضها في التطبيق
        //عنوان قاعدة البيانات
        FirebaseDatabase database = FirebaseDatabase.getInstance();//تتصل بقاعدة البيانات
        // عنوان مجموعة المعطيات داخل قاعدة البيانات
        DatabaseReference myRef = database.getReference(group);
//إضافة listener مما يسبب الإصغاء لكل تغيير حتلنة عرض المعطيات//
        //يسمع لأي تغيير يصير في Firebase ويحدث القائمة تلقائي
        myRef.addValueEventListener(new ValueEventListener() {//in database  myRef تقوم الدالة بتجديث واجهة المستخدم ul تلقائيا في كل مرة يتغير فيها اي معطى داخل المسار
            @Override
            //دالة معالج حدث تقوم بتلقى نسخة عن كل المعطيات عند أي تغيير
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

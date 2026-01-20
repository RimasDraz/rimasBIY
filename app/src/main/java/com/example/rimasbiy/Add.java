package com.example.rimasbiy;


import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rimasbiy.MyRecipeTable.Recipe;
import com.example.rimasbiy.data.AppDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Add extends AppCompatActivity {
    private Button buttonsaverecipe;
    private TextInputEditText recipename;
    private TextInputEditText description;
    private TextInputEditText ingredients;
    private TextInputEditText instructions;
    private ImageView imagerecipe;
   // private ImageView ivSelectedImage; //صفة كمؤشر لهذا الكائن
    private Uri selectedImageUri;//صفة لحفظ عنوان الصورة بعد اختيارها
    private ActivityResultLauncher<String> pickImage;// ‏كائن لطلب الصورة من الهاتف
    String TAG="FilePermission";

    // مُشغّلات لطلب الأذونات
    private ActivityResultLauncher<String> requestReadMediaImagesPermission;
    private ActivityResultLauncher<String> requestReadMediaVideoPermission;
    private ActivityResultLauncher<String> requestReadExternalStoragePermission;

    @SuppressLint({"MissingInflatedId", "WrongViewCast", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        buttonsaverecipe = findViewById(R.id.buttonsaverecipe);
        recipename = findViewById(R.id.recipename);
        description = findViewById(R.id.description);
        ingredients = findViewById(R.id.ingredients);
        instructions = findViewById(R.id.instructions);
        imagerecipe = findViewById(R.id.imagerecipe);


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
        requestReadMediaImagesPermission=registerForActivityResult(new ActivityResultContracts.RequestPermission(),isGranted->
        {

            if (isGranted){
                Log.d(TAG,"READ_MEDIA_IMAGES permission granted");
                Toast.makeText(this,"تم منح إذن قراءة الصور", Toast.LENGTH_SHORT).show();

            } else {
                Log.d(TAG, "READ_MEDIA_IMAGES permission denied");
                Toast.makeText(this, "تم رفض إذن قراءة الصور", Toast.LENGTH_SHORT).show();
                // التعامل مع حالة رفض الإذن

            }
        });

        requestReadMediaVideoPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d(TAG, "READ_MEDIA_VIDEO permission granted");
                Toast.makeText(this, "تم منح إذن قراءة الفيديو", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "READ_MEDIA_VIDEO permission denied");
                Toast.makeText(this, "تم رفض إذن قراءة الفيديو", Toast.LENGTH_SHORT).show();
                // التعامل مع حالة رفض الإذن
            }
        });
        requestReadExternalStoragePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d(TAG, "READ_EXTERNAL_STORAGE permission granted");
                Toast.makeText(this, "تم منح إذن قراءة التخزين الخارجي", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "READ_EXTERNAL_STORAGE permission denied");
                Toast.makeText(this, "تم رفض إذن قراءة التخزين الخارجي", Toast.LENGTH_SHORT).show();
                // التعامل مع حالة رفض الإذن
            }
        });

// Initialize the ActivityResultLauncher for picking images
        pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    selectedImageUri = result;
                    imagerecipe.setImageURI(result);
                    imagerecipe.setVisibility(View.VISIBLE);
                }
            }
        });


        imagerecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage.launch("image/*"); // Launch the image picker
            }
        });


        checkAndRequestPermissions();

    }


            private boolean validateFields () {
                boolean flag = true;

                if (recipename.getText().toString().isEmpty()) {
                    recipename.setError("Invalid recipe name");
                    flag = false;
                }

                if (description.getText().toString().isEmpty()) {
                    description.setError("Description is required");
                    flag = false;
                }

                if (ingredients.getText().toString().isEmpty()) {
                    ingredients.setError("Ingredients are required");
                    flag = false;
                }

                if (instructions.getText().toString().isEmpty()) {
                    instructions.setError("Instructions are required");
                    flag = false;
                }
                if (flag) {
                    /**
                     بناء كائن
                     */
                    Recipe recipe = new Recipe();
                    /**
                     *استخراج القيم من الصفات واعطائها للحقولا
                     */
                    recipe.setName(recipename.getText().toString());
                    recipe.setDescription(description.getText().toString());
                    recipe.setIngredients(ingredients.getText().toString());
                    recipe.setInstructions      (instructions.getText().toString());
                    /**
                     * ادخال البيانات في قاعدة البيانات
                     */
                    AppDatabase.getInstance(this).myRecipeQuery().insert(recipe);
                    Toast.makeText(this, "Recipe saved successfully", Toast.LENGTH_SHORT).show();
                    //save via frirebase database
                    saveRecipe(recipe);
                }
                return flag;
            }
    private void checkAndRequestPermissions() {
        // فحص وطلب إذن READ_MEDIA_IMAGES (للإصدارات الحديثة)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // أندرويد 13+
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadMediaImagesPermission.launch(android.Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                Log.d(TAG, "READ_MEDIA_IMAGES permission already granted");
                Toast.makeText(this, "إذن قراءة الصور ممنوح بالفعل", Toast.LENGTH_SHORT).show();
            }

//        });

            buttonsaverecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validateFields();
//                    if (validateFields()) {
//                        buttonsaverecipe.setEnabled(false);
//                        buttonsaverecipe.setText("Saving...");
//                        //postDelayed تشغل ثريد
//                        buttonsaverecipe.postDelayed(new Runnable() {
//                            // انه تشغيل مقطع كود بنفس الوقت مع ثريد ثاني وخاصة مع التطييق الخاصة بنا
//                            @Override
//                            public void run() {
//                                buttonsaverecipe.setEnabled(true);
//                                buttonsaverecipe.setText("Save");
//                                finish();
//                            }
//                            //تشغل ثريد لمدة 2ث
//                        }, 2000);
//                    }
                }
            });
        }
    }
    public void saveRecipe(Recipe recipe) {//في قاعدة البيانات "recipe" الحصول على مرجع الى عقدة
        //تهيئة  Firebase Realtime Database // مؤشر لقاعدة البيانات
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        //مؤشر لجدول الوصفات
        DatabaseReference RecipesRef = database.child("users");
        //انشاء مفتاح فريد للوصفة الجديدة
        DatabaseReference newRecipeRef = RecipesRef.push();
        //تعيين  معرف الوصفة في الكائن Recipe
        recipe.setKey(newRecipeRef.getKey());
        //حفظ بيانات الوصفة في قاعدة البيانات
        //اضافة كائن "لمجموعة" الوصفات ومعالج حدث لفحص تجاح المطلوب
        //مفالج حدث لحفص هل تم المطلوب من قاعدة البيانات
        newRecipeRef.setValue(recipe).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Add.this, "Succeeded to add recipe", Toast.LENGTH_SHORT).show();
                        finish();
                        //تم حفظ البيانات بنجاح
                        Log.d(TAG, ":تم حفظ الوصفة بنجاح" + recipe.getId());
                        //تحديث واجهة المستخدم او تنفيذ اجراءات اخرى

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //معالجة الاخطاء
                        Log.e(TAG,"خطأ في حفظ الوصفة:"+e.getMessage(),e);
                        Toast.makeText(Add.this,"failed to add Recipe",Toast.LENGTH_SHORT).show();
                        //عرض رسالة خطأ للوصفة
                    }
                });
    }

}
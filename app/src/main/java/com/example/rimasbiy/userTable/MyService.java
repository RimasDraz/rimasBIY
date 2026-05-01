package com.example.rimasbiy.userTable;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.rimasbiy.MyRecipeTable.Recipe;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//تأخذ بيانات الوصفة وترفعها إلى قاعدة بيانات Firebase.

public class MyService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //read the data that received within the intent
        // 1. التأكد من أن الـ intent ليس فارغاً ويحتوي على بيانات الوصفة المطلوبة
        if (intent != null && intent.hasExtra("recipe_extra")) {
            // 2. استخراج كائن الوصفة (Recipe) ومسار الحفظ (group) من الـ intent
            Recipe recipe = (Recipe)  intent.getSerializableExtra("recipe_extra");
            String group=intent.getStringExtra("group");
            // 3. استدعاء الدالة المسؤولة عن رفع البيانات إلى Firebase
            saveRecipeToFirebase(recipe,group);
        }
        // START_NOT_STICKY means if the system kills the service, don't recreate it automatically
        // 4. إخبار النظام بعدم إعادة تشغيل الخدمة تلقائياً في حال تم إغلاقها بسبب نقص الذاكرة
        return START_NOT_STICKY;
    }


    private void saveRecipeToFirebase(Recipe recipe, String group) {
        // 5. إنشاء مرجع (Reference) في Firebase بناءً على المسار الممرر (مثل favorites/uid)
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(group);
        // 6. التحقق إذا كانت الوصفة جديدة (ليس لها مفتاح Key) لإنشاء مفتاح فريد لها
      if(recipe.getKey()==null||recipe.getKey().length()==0) {//اذا فش عنا key
          String key = myRef.push().getKey();// توليد مفتاح تلقائي من Firebase
          recipe.setKey(key);// تعيين المفتاح الجديد للوصفة
      }

// 7. حفظ بيانات الوصفة داخل المفتاح المخصص لها في Firebase
        myRef.child(recipe.getKey()).setValue(recipe).addOnCompleteListener(fbTask -> {
            // 8. التعامل مع نتيجة الرفع (نجاح أو فشل) وإظهار رسالة للمستخدم (Toast)
            if (fbTask.isSuccessful()) {
                // In a service, use context from getApplicationContext() for Toasts
                Toast.makeText(getApplicationContext(), "Sync Successful", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Sync Failed", Toast.LENGTH_SHORT).show();

            }
            // Stop the service once the work is done to save battery/RAM
            stopSelf();
        });
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // We are using a Started Service, not a Bound Service
    }
}


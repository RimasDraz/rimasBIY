package com.example.rimasbiy.userTable;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.rimasbiy.MyRecipeTable.Recipe;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
///تأخذ بيانات الوصفة وترفعها إلى قاعدة بيانات Firebase.

public class MyService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //read the data that received within the intent
        /// . التأكد من أن الـ intent ليس فارغاً ويحتوي على بيانات الوصفة المطلوبة
        if (intent != null && intent.hasExtra("recipe_extra")) {
            /// . استخراج كائن الوصفة (Recipe) ومسار الحفظ (group) من الـ intent
            Recipe recipe = (Recipe)  intent.getSerializableExtra("recipe_extra");
            String group=intent.getStringExtra("group");
            /// . استدعاء الدالة المسؤولة عن رفع البيانات إلى Firebase
            saveRecipeToFirebase(recipe,group);
        }
        // START_NOT_STICKY means if the system kills the service, don't recreate it automatically
        ///  إخبار النظام بعدم إعادة تشغيل الخدمة تلقائياً في حال تم إغلاقها بسبب نقص الذاكرة
        return START_NOT_STICKY;
    }
    /// .  الدالة المسؤولة عن رفع البيانات إلى Firebase
    private void saveRecipeToFirebase(Recipe recipe, String group) {
        ///إنشاء مرجع (Reference) في Firebase بناءً على المسار الممرر (مثل favorites/uid)
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(group);
        ///  التحقق إذا كانت الوصفة جديدة (ليس لها مفتاح Key) لإنشاء مفتاح فريد لها
      if(recipe.getKey()==null||recipe.getKey().length()==0) {///اذا فش عنا key
          String key = myRef.push().getKey();/// توليد مفتاح تلقائي من Firebase
          recipe.setKey(key);/// تعيين المفتاح الجديد للوصفة
      }

///  حفظ بيانات الوصفة داخل المفتاح المخصص لها في Firebase
        /// .addOnCompleteListener(...): هذه دالة "مراقب" (Listener). هي لا تنفذ عملية الحفظ، بل تنتظر حتى ينتهي Firebase من المحاولة، ثم تخبرك بالنتيجة (هل نجح الحفظ أم فشل؟).
        myRef.child(recipe.getKey()).setValue(recipe).addOnCompleteListener(fbRecipe -> {
            /// . التعامل مع نتيجة الرفع (نجاح أو فشل) وإظهار رسالة للمستخدم (Toast)
            if (fbRecipe.isSuccessful()) {
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

///  دالة onBind هي "نقطة الوصل" في حال كنت تريد ربط الـ Activity بالـ Service لتتبادلا البيانات بشكل مباشر.
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // We are using a Started Service, not a Bound Service
    }
}


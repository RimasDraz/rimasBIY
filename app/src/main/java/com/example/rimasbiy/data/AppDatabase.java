package com.example.rimasbiy.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Entity;

import com.example.rimasbiy.MyRecipeTable.MyRecipeQuery;
import com.example.rimasbiy.MyRecipeTable.Recipe;
import com.example.rimasbiy.userTable.Myuser;
import com.example.rimasbiy.userTable.MyuserQuery;
// هدفه: المدير العام. هو الذي ينشئ قاعدة البيانات، ويربط الجداول (Entities) مع الأوامر (DAOs)، ويضمن وجود نسخة واحدة فقط من قاعدة البيانات في التطبيق.
@Entity
// إذا قررت مستقبلاً إضافة عمود جديد (مثلاً إضافة "وقت التحضير" لجدول الوصفات)، لن يفهم أندرويد أن الهيكل تغير إلا إذا قمت بزيادة هذا الرقم (مثلاً من 5 إلى 6). version = 5 (رقم الإصدار)
// أنتِ تخبرين الأندرويد أن قاعدة البيانات هذه تحتوي على جدولين: واحد للمستخدمين (Myuser) وواحد للوصفات (Recipe).
    @Database(entities = {Myuser.class, Recipe.class}, version = 5)//السطر الذي يجدد اسم قاعدة البيانات
/**
 * االفئة المسؤولة عن بناء قاعدة البيانات بكل جداولها
 * وتوفر لنا كائن للتعامل مع قاعدة البيانات
 */
    public abstract class AppDatabase extends RoomDatabase {
        /**
         *كائن للتعامل مع قاعدة البيانات
         */
        private static AppDatabase db;
        /**
         * يعيد كائن لعمليات جدول المستعملين
         * @return
         */
        //هذه الدوال هي "البوابات". عندما تريد إضافة مستخدم أو حذف وصفة، ستنادي هذه الدوال لتعطيك الصلاحيات التي برمجتها داخل كلاسات الـ Query
        public abstract MyuserQuery myuserQuery();
    /**
     يعيد كائن لعمليات جدول الوصفات
     * @return
     */
    public abstract MyRecipeQuery myRecipeQuery();
    /**
     * بناء قاعدة بيانات واعادة كائن يؤشرعليها
     * @param context
     * @return
     */
    //الهدف هو التأكد من أن التطبيق لا ينشئ أكثر من نسخة واحدة من قاعدة البيانات في نفس الوقت.
        public static AppDatabase getInstance(Context context) {//الهدف هو التأكد من أن التطبيق لا ينشئ أكثر من نسخة واحدة من قاعدة البيانات في نفس الوقت. فتح قاعدة البيانات عملية تستهلك الكثير من ذاكرة الهاتف، لذا نريد "نسخة واحدة مشتركة" لكل التطبيق.
            if (db == null) {//. يسأل النظام: "هل قمنا بإنشاء قاعدة البيانات سابقاً؟".•إذا كان الجواب نعم (ليست null)، يتخطى البناء ويُعطينا النسخة الموجودة فوراً.•إذا كان الجواب لا (أول مرة يفتح التطبيق)، يدخل ليبنيها.•
              //يبني القاعدة. يأخذ اسم الملف الذي سيُخزن على الهاتف وهو "app_database".
                //fallbackToDestructiveMigration():•مهم جداً: إذا قمتِ بتغيير تصميم الجدول (مثلاً أضفتِ حقل جديد للمستخدم) ولم تغيري رقم الإصدار، هذا الأمر يخبر التطبيق: "بدلاً من أن يتوقف التطبيق عن العمل (Crash)، قم بمسح القاعدة القديمة وإنشاء واحدة جديدة بالهيكل الجديد".•
                //.allowMainThreadQueries():•تسمح للتطبيق بإجراء عمليات الحفظ والقراءة على "الخيط الرئيسي" (Main Thread).
                //build():•الأمر النهائي لبدء عملية الإنشاء الفعلي.•
                db = Room.databaseBuilder(context,AppDatabase.class, "app_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
            }
            return db;//إرجاع نسخة قاعدة البيانات الجاهزة للاستخدام.
        }
    }



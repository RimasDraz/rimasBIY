package com.example.rimasbiy.MyRecipeTable;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.view.View.GONE;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import com.example.rimasbiy.ListRecipes;
import com.example.rimasbiy.R;
import com.example.rimasbiy.RecipeReminderReceiver;
import com.example.rimasbiy.SmartRecipeAssistant;
import com.example.rimasbiy.userTable.MyService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// مكتبات معالجة الصور

// مكتبات Firebase Storage (للملفات والصور)

// مكتبات معالجة الأحداث والنجاح/الفشل

// مكتبات الملفات والنظام
import java.util.Calendar;

public class MyRecipeAdapter extends ArrayAdapter<Recipe> {
    private final int recipeLayout;
    private long selectedReminderTime;

    /**
     * العمل يبني الارتباط
     *
     * @param context==> رابط بالسياق
     * @param resource   ==> تصميم عنصر لعرض بيانات الكائن
     */
    public MyRecipeAdapter(@NonNull Context context, int resource) {//هو Adapterبياخد كائنات من نوع Recipe ويعرضهم داخل ListView
        super(context, resource);//context → رابط بالشاشة resource → تصميم العنصر (شكل كل وصفة)
        this.recipeLayout = resource;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View vitem = convertView;//العنصر المرئي (View) اللي بيمثل وصفة وحدة داخل الـ ListView. يعني كل سطر في القائمة = vitem
        if (vitem == null)
            vitem = LayoutInflater.from(getContext()).inflate(R.layout.recipe_item_layout, parent, false);
        ImageView cakeimg = vitem.findViewById(R.id.cakeimg);//ربط عناصر التصميم
        MaterialButton loveimageb = vitem.findViewById(R.id.loveimageb);
        MaterialButton shareimageb = vitem.findViewById(R.id.shareimageb);
        MaterialButton AI=vitem.findViewById(R.id.AI);
        MaterialButton Delete=vitem.findViewById(R.id.Delete);
        MaterialTextView nameCake = vitem.findViewById(R.id.nameCake);
        TextView disText = vitem.findViewById(R.id.disText);
        //current هو الوعاء الذي يحمل بيانات الوصفة التي حان دورها لتظهر أمام المستخدم حالاً.
        //current هو كائن (Object) من نوع Recipe.  "current" لأنه يمثل "الوصفة الحالية" التي يقوم الـ Adapter برسمها الآن على الشاشة.
        Recipe current = getItem(position);//هات الوصفة اللي رقمها position في القائمة.
        cakeimg.setImageBitmap(stringToBitmap(current.getImage()));
        /**
         * عرض المعطيات على حقول الرسم
         */
        nameCake.setText(current.getName());//عرض البيانات
        disText.setText(current.getDescription());
        //لسطر الواحد في الـ ListView --> الvitem
        //يعني الشكل اللي بيمثل وصفة وحدة (اسم + وصف + صورة)

//عالج حدث النقرة (Click Listener) لزر مشاركة الصورة (shareimageb
        shareimageb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSendSmsApp(current.getName(), "");// אם יש טלפון המשימה מעבירים במקום ה ״״
            }
        });
        String myId=FirebaseAuth.getInstance().getCurrentUser().getUid();///عملنا هاد الكود عشان ميمحاش وصفات مش الي
        if (current.getOwner().equals(myId)==false)
            Delete.setVisibility(GONE);

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showYesNoDialog(current);
                    }
                });
            }
        });
        ///current كائن الوصفة الحالية التي سيتم ضبط التذكير لها
        loveimageb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(current);/// تذكير بعد فترة انه هاي الوصفة عجبتني
            }
        });
        AI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), SmartRecipeAssistant.class);
                getContext().startActivity(i);
            }
        });
        return vitem;///رجّع العنصر
    }

    /**
     * פתיחת אפליקצית שליחת sms
     * @param msg   .. ההודעה שרוצים לשלוח
     * @param phone
     */
    public void openSendSmsApp(String msg, String phone) {
        //אינטנט מרומז לפתיחת אפליקצית ההודות סמס
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        //מעבירים מספר הטלפון
        smsIntent.setData(Uri.parse("smsto:" + phone));
        //ההודעה שנרצה שתופיע באפליקצית ה סמס
        smsIntent.putExtra("sms_body", msg);
        smsIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
        //פתיחת אפליקציית ה סמס
        getContext().startActivity(smsIntent);
    }
    ///اذا بدي اطلع من الحساب او لا`
    private void showYesNoDialog(Recipe current) {
        final Recipe recipe = current;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to delete this recipe?")
                .setPositiveButton("Yes", (dialog, id) -> {
                    ///تهيئة  Firebase Realtime Database // مؤشر لقاعدة البيانات
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    ///مؤشر لجدول الوصفات
                    DatabaseReference RecipesRef = database.child("recipes");/// يمثل مؤشرا او مرحعا لمسار محدد في شجرة البيانات
                    RecipesRef.child(current.getKey()).removeValue();                    })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel())
                .create()
                .show();
    }

//    //**
//     * دالة مساعدة لفتح قائمة تتلقى
//     * بارامتر للكائن الذي سبب فتح القائمة
//     *
//     * @param v
//     * @param Recipe
//     */
//    public void showPopUpMenu(View v, MyRecipeAdapter Recipe) {
//        // بناء قائمةPopUpMenu
//        PopupMenu popup = new PopupMenu(getContext(), v);
//        //ملف الثائمة
//        popup.inflate(R.menu.the_menu);
//        //اضافة معالج حدث لاختيار عنصر من القائمة
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                if (item.getItemId() == R.id.loveimageb) {
//                    //هنا نكتب رد فعل هذا العنصر من القائمة
//                    Toast.makeText(MyRecipeAdapter.this.getContext(), "like", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(getContext(), ListRecipes.class);
//                    getContext().startActivity(i);
//                }
//                    if (item.getItemId()==R.id.AI){
//                        Intent i=new Intent(getContext(), SmartRecipeAssistant.class);
//                    getContext().startActivity(i);}
//                    //to do
//                    if (item.getItemId() == R.id.shareimageb)
//                    {
//                        Toast.makeText(MyRecipeAdapter.this.getContext(), "share", Toast.LENGTH_SHORT).show();
//                    }
//
//                return true;
//            }
//        });
//        popup.show();//فتح وعرض القائمة
//    }
    private void showDateTimePicker(Recipe current) {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        //יצירת דיאלוג וטיפול באירוע הזמן שנבחר
        new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {//אירוע בחירת הזמן
            date.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(getContext(), (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);//הזמן שנבחר
                date.set(Calendar.MINUTE, minute);
                date.set(Calendar.SECOND, 0);
                 selectedReminderTime = date.getTimeInMillis();// הזמן שנבחר במלישניות//
                scheduleAlarm(current);
                //save by service
                Intent serviceIntent=new Intent(getContext(), MyService.class);//يتم إنشاء Intent صريح لاستهداف كلاس اسمه MyService. هذا الكلاس هو المسؤول عن معالجة البيانات في الخلفية/
                serviceIntent.putExtra("recipe_extra",current);//يتم إرسال كائن (Object) يمثل الوصفة الحالية.
                String myId= FirebaseAuth.getInstance().getCurrentUser().getUid();//هنا يتم جلب الـ UID الخاص بالمستخدم الحالي من FirebaseAuth لبناء مسار فريد لكل مستخدم داخل عقدة الـ favorites في قاعدة البيانات
                String group = "favorites_" + myId;///ذا السطر يقوم بإنشاء اسم فريد للمجموعة (ID) التي سيتم تخزين الوصفة تحتها. هو يدمج كلمة ثابتة وهي _favorites مع المعرف الخاص بالمستخدم myId.
                serviceIntent.putExtra("group",group);//يتم تمرير المسار الذي سيتم الحفظ فيه داخل قاعدة البيانات.
               getContext().startService(serviceIntent);//يتم إرسال الأوامر للنظام لبدء تشغيل الخدمة. بمجرد استدعاء هذا السطر، سينتقل التنفيذ إلى دالة onStartCommand داخل كلاس MyService.

                ///الوظيفة: فتح واجهة اختيار التاريخ (DatePicker) متبوعة فوراً بواجهة اختيار الوقت (TimePicker).
                ///يظهر تقويم للمستخدم لاختيار (السنة، الشهر، اليوم).
                ///عند الضغط على "موافق"، يتم استدعاء "مستمع الأحداث" الذي يفتح تلقائياً واجهة تحديد (الساعة، الدقيقة).
                ///يتم ضبط الحوارين ليبدآ من التاريخ والوقت الحاليين للجهاز باستخدام الكائن currentDate
                ///تم ضبط false في نهاية الكود ليعرض الوقت بنظام 12 ساعة (AM/PM)./
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
    ///ضبط منبه (Reminder) لوصفة معينة لإرسال إشعار للمستخدم في وقت محدد مسبقاً.
    private void scheduleAlarm(Recipe recipe) {
        ////  الوصول إلى خدمة المنبهات في نظام الأندرويد
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        //תזמון הפעלה של
        //RecipeReminderReceiver
        ////  إنشاء Intent لتحديد المستلم (Receiver) الذي سيعالج التنبيه عند حدوثه
        Intent intent = new Intent(getContext(), RecipeReminderReceiver.class);
        //מעבירים את הנתונים לברודקסט רסיבר
        ///  وضع اسم الوصفة داخل الـ Intent لإظهاره في عنوان الإشعار لاحقاً
        intent.putExtra("title", recipe.getName());
        ///  وضع وصف الوصفة داخل الـ Intent لإظهاره في نص الإشعار لاحقاً
        intent.putExtra("text", recipe.getDescription());
        //הכנת אובייקט תיזמון
        ///إنشاء PendingIntent: وهو تصريح للنظام بتنفيذ الـ Intent أعلاه في وقت مستقبلي
        /// (ID) استخدمنا معرف الوصفة كرمز فريد لمنع تداخل التنبيهات المختلفة
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), (int) recipe.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        //// التحقق من أن خدمة المنبهات متاحة
        if (alarmManager != null) {
            /// التعامل مع قيود إصدار أندرويد
            // יוצרים לפי גרסת מערכת הטלפון
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ///  إذا كان التطبيق يملك صلاحية المنبه الدقيق، يتم ضبطه ليعمل حتى في وضع السكون
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, selectedReminderTime, pendingIntent);
                } else {
                    /// . إذا لم تتوفر الصلاحية، يتم ضبط منبه عادي
                    alarmManager.set(AlarmManager.RTC_WAKEUP, selectedReminderTime, pendingIntent);
                }
            } else {
                ///  للإصدارات القديمة: ضبط المنبه بدقة تامة وبخاصية إيقاظ الجهاز
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, selectedReminderTime, pendingIntent);
            }
        }
    }
    /**
     * Decodes the image string and returns the corresponding Bitmap object.
     *
     * @param imageString the image string to decode
     * @return the decoded Bitmap object
     */
    //استرجاع الصورة من النص
    private Bitmap stringToBitmap(String imageString) {
        ///  التحقق مما إذا كان النص فارغاً أو غير موجود لتجنب الأخطاء
        if (imageString == null || imageString.isEmpty()) return null;
        try {
            ///  تحويل النص المشفر بصيغة Base64 إلى مصفوفة بايتات(byte array)
            byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
            ///  تحويل مصفوفة البايتات إلى كائن Bitmap(صورة) يمكن عرضه في التطبيق
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            /// في حال حدوث أي خطأ أثناء التحويل(مثل نص غير صالح)، يتم إرجاع null
            return null;
        }
    }
}



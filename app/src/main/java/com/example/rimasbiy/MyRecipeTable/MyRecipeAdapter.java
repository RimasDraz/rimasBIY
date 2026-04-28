package com.example.rimasbiy.MyRecipeTable;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.AlarmManager;
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
        MaterialTextView nameCake = vitem.findViewById(R.id.nameCake);
        TextView disText = vitem.findViewById(R.id.disText);
        Recipe current = getItem(position);//هات الوصفة اللي رقمها position في القائمة.
        cakeimg.setImageBitmap(stringToBitmap(current.getImage()));

        /**
         * عرض المعطيات على حقول الرسم
         */
        nameCake.setText(current.getName());//عرض البيانات
        disText.setText(current.getDescription());
        //لسطر الواحد في الـ ListView --> الvitem
        //يعني الشكل اللي بيمثل وصفة وحدة (اسم + وصف + صورة)

        shareimageb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSendSmsApp(current.getName(), "");// אם יש טלפון המשימה מעבירים במקום ה ״״
            }
        });
        loveimageb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(current);
            }
        });
        return vitem;//رجّع العنصر
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

    /**
     * دالة مساعدة لفتح قائمة تتلقى
     * بارامتر للكائن الذي سبب فتح القائمة
     *
     * @param v
     * @param Recipe
     */
    public void showPopUpMenu(View v, MyRecipeAdapter Recipe) {
        // بناء قائمةPopUpMenu
        PopupMenu popup = new PopupMenu(getContext(), v);
        //ملف الثائمة
        popup.inflate(R.menu.the_menu);
        //اضافة معالج حدث لاختيار عنصر من القائمة
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.loveimageb) {
                    //هنا نكتب رد فعل هذا العنصر من القائمة
                    Toast.makeText(MyRecipeAdapter.this.getContext(), "like", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getContext(), ListRecipes.class);
                    getContext().startActivity(i);
                }
                    if (item.getItemId()==R.id.AI){
                        Intent i=new Intent(getContext(), SmartRecipeAssistant.class);
                    getContext().startActivity(i);}
                    //to do
                    if (item.getItemId() == R.id.shareimageb)
                    {
                        Toast.makeText(MyRecipeAdapter.this.getContext(), "share", Toast.LENGTH_SHORT).show();
                    }

                return true;
            }
        });
        popup.show();//فتح وعرض القائمة
    }
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
                Intent serviceIntent=new Intent(getContext(), MyService.class);
                serviceIntent.putExtra("recipe_extra",current);
                serviceIntent.putExtra("group","favorites");
               getContext().startService(serviceIntent);

                //todo add tv to show time
                // tvReminderTime.setText(date.getTime().toString());//הצגת הזמן
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
    //العمليية التي تشغل الوقت
    private void scheduleAlarm(Recipe recipe) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        //תזמון הפעלה של
        //RecipeReminderReceiver
        Intent intent = new Intent(getContext(), RecipeReminderReceiver.class);
        //מעבירים את הנתונים לברודקסט רסיבר
        intent.putExtra("title", recipe.getName());//
        intent.putExtra("text", recipe.getDescription());//
        //הכנת אובייקט תיזמון
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), (int) recipe.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            //יוצרים לפי גרסת מערכת הטלפון
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, selectedReminderTime, pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, selectedReminderTime, pendingIntent);
                }
            } else {
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
        if (imageString == null || imageString.isEmpty()) return null;
        try {
            byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            return null;
        }
    }
}



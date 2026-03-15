package com.example.rimasbiy.MyRecipeTable;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.PermissionChecker;

import com.example.rimasbiy.R;
import com.example.rimasbiy.userTable.Myuser;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MyRecipeAdapter extends ArrayAdapter<Recipe> {
    private final int recipeLayout;
    /**
     *العمل يبني الارتباط
     * @param context==> رابط بالسياق
     * @param resource ==> تصميم عنصر لعرض بيانات الكائن
     */
    public MyRecipeAdapter(@NonNull Context context, int resource) {//هو Adapterبياخد كائنات من نوع Recipe ويعرضهم داخل ListView
        super(context, resource);//context → رابط بالشاشة resource → تصميم العنصر (شكل كل وصفة)
        this.recipeLayout=resource;
    }
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View vitem= convertView;//العنصر المرئي (View) اللي بيمثل وصفة وحدة داخل الـ ListView. يعني كل سطر في القائمة = vitem
        if (vitem==null)
            vitem= LayoutInflater.from(getContext()).inflate(R.layout.recipe_item_layout,parent,false);
        ImageView cakeimg=vitem.findViewById(R.id.cakeimg);//ربط عناصر التصميم
        MaterialButton loveimageb=vitem.findViewById(R.id.loveimageb);
        MaterialButton shareimageb=vitem.findViewById(R.id.shareimageb);
        MaterialTextView nameCake=vitem.findViewById(R.id.nameCake);
        TextView disText=vitem.findViewById(R.id.disText);

        Recipe current=getItem(position);//هات الوصفة اللي رقمها position في القائمة.
        /**
         * عرض المعطيات على حقول الرسم
         */
        nameCake.setText(current.getName());//عرض البيانات
        disText.setText(current.getDescription());
        return vitem;//رجّع العنصر
        //لسطر الواحد في الـ ListView --> الvitem
        //يعني الشكل اللي بيمثل وصفة وحدة (اسم + وصف + صورة)

        shareimageb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSendSmsApp(current.get,"");// אם יש טלפון המשימה מעבירים במקום ה ״״
            }
        });


    }

    /**
     *  פתיחת אפליקצית שליחת sms
     * @param msg .. ההודעה שרוצים לשלוח
     * @param phone
     */
    public void openSendSmsApp(String msg, String phone)
    {
        //אינטנט מרומז לפתיחת אפליקצית ההודות סמס
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        //מעבירים מספר הטלפון
        smsIntent.setData(Uri.parse("smsto:"+phone));
        //ההודעה שנרצה שתופיע באפליקצית ה סמס
        smsIntent.putExtra("sms_body",msg);
        smsIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
        //פתיחת אפליקציית ה סמס
        getContext().startActivity(smsIntent);
    }

    /**
     *  פתיחת אפליקצית שליחת whatsapp
     * @param msg .. ההודעה שרוצים לשלוח
     * @param phone
     */
    public void openSendWhatsAppV2(String msg, String phone)
    {
        //אינטנט מרומז לפתיחת אפליקצית ההודות סמס
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);;
        String url = null;
        try {
            url = "https://api.whatsapp.com/send?phone="+phone+"&text="+ URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            Toast.makeText(getContext(), "there is no whatsapp!!", Toast.LENGTH_SHORT).show();
        }
        sendIntent.setData(Uri.parse(url));
        sendIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        sendIntent.addCategory(Intent.CATEGORY_DEFAULT);
        //פתיחת אפליקציית ה סמס
        getContext().startActivity(sendIntent);
    }

    /**
     * ביצוע שיחה למפסר טלפון
     * todo הוספת הרשאה בקובץ המניפיסט
     * <uses-permission android:name="android.permission.CALL_PHONE" />
     * @param phone מספר טלפון שרוצים להתקשר אליו*/
    private void callAPhoneNymber(String phone){
        //בדיקה אם יש הרשאה לביצוע שיחה
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//בדיקת גרסאות
            //בדיקה אם ההרשאה לא אושרה בעבר
            if (checkSelfPermission(getContext(),Manifest.permission.CALL_PHONE) == PermissionChecker.PERMISSION_DENIED) {
                //רשימת ההרשאות שרוצים לבקש אישור
                String[] permissions = {Manifest.permission.CALL_PHONE};
                //בקשת אישור הרשאות (שולחים קוד הבקשה)
                //התשובה תתקבל בפעולה onRequestPermissionsResult
                requestPermissions((Activity) getContext(),permissions, 100);
            }
            else{
                //אינטנט מרומז לפתיחת אפליקצית ההודות סמס
                Intent phone_intent = new Intent(Intent.ACTION_CALL);
                phone_intent.setData(Uri.parse("tel:" + phone));
                getContext().startActivity(phone_intent);
            }
        }}
    /**
     * دالة مساعدة لفتح قائمة تتلقى
     * بارامتر للكائن الذي سبب فتح القائمة
     * @param v
     * @param Recipe
     */
    public  void showPopUpMenu(View v,MyRecipeAdapter Recipe)
    {
        // بناء قائمةPopUpMenu
        PopupMenu popup=new PopupMenu(this ,v);
        //ملف الثائمة
        popup.inflate(R.menu.popup_menu);

    }




}

package com.example.rimasbiy.MyRecipeTable;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.core.content.ContextCompat.startActivity;

import static java.nio.file.Files.delete;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import com.example.rimasbiy.ListRecipes;
import com.example.rimasbiy.R;
import com.example.rimasbiy.userTable.Myuser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
// مكتبات معالجة الصور
import com.squareup.picasso.Picasso;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

// مكتبات Firebase Storage (للملفات والصور)
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

// مكتبات معالجة الأحداث والنجاح/الفشل
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

// مكتبات الملفات والنظام
import java.io.File;
import java.io.IOException;

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
        //لسطر الواحد في الـ ListView --> الvitem
        //يعني الشكل اللي بيمثل وصفة وحدة (اسم + وصف + صورة)

        shareimageb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSendSmsApp(current.getName(),"");// אם יש טלפון המשימה מעבירים במקום ה ״״
            }
        });
        return vitem;//رجّع العنصر
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
        PopupMenu popup=new PopupMenu(getContext(),v);
        //ملف الثائمة
        popup.inflate(R.menu.the_menu);
        //اضافة معالج حدث لاختيار عنصر من القائمة
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.itmitem) ;
                {
                //هنا نكتب رد فعل هذا العنصر من القائمة
                Toast.makeText(MyRecipeAdapter.this.getContext(), "Add", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), ListRecipes.class);
                getContext().startActivity(i);}
              return true;
            }
        });
        popup.show();//فتح وعرض القائمة
    }
    /**
     * הורדת קובץ/תמונה לזיכרון של הטלפון (לא לאחסון)
     * @param fileURL כתובת הקובץ באחסון הענן
     * @param toView רכיב התמונה המיועד להצגת התמונה
     */
    private void downloadImageToMemory(String fileURL, final ImageView toView)
    {
        if(fileURL==null)return;
        // הפניה למיקום הקובץ באיחסון
        StorageReference httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(fileURL);
        final long ONE_MEGABYTE = 1024 * 1024;
        //הורדת הקובץ והוספת מאזין שבודק אם ההורדה הצליחה או לא
        httpsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                toView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 90, 90, false));
                Toast.makeText(getContext(), "downloaded Image To Memory", Toast.LENGTH_SHORT).show();
            }
            //מאזין אם ההורדה נכשלה
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(getContext(), "onFailure downloaded Image To Local File "+exception.getMessage(), Toast.LENGTH_SHORT).show();
                exception.printStackTrace();
            }
        });
    }
    /**
     * מחיקת פריט כולל התמונה מבסיס הנתונים
     * @param myRecipe הפריט שמוחקים
     */
   // private void delMyTaskFromDB_FB(Recipe myRecipe)
    {
        //הפנייה/כתובת  הפריט שרוצים למחוק
     //   FirebaseFirestore db=FirebaseFirestore.getInstance();
       // db.collection("MyUsers").
         //       document(FirebaseAuth.getInstance().getUid()).
         //       collection("subjects").
         //       document(Recipe.).
           //     collection("Tasks").document(Recipe.id).
              //  delete().//מאזין אם המחיקה בוצעה
             //   addOnCompleteListener(new OnCompleteListener<Void>() {
        //    @Override
           // public void onComplete(@NonNull Task<Void> task) {
        //   if(task.isSuccessful())
            //    {
            //        remove(Recipe);// מוחקים מהמתאם
            //        deleteFile(Myuser.getImage());// מחיקת הקובץ
             //       Toast.makeText(getContext(), "deleted", Toast.LENGTH_SHORT).show();
               // }
         //   }
      //  });
    //}
   // /**
  //   * מחיקת קובץ האיחסון הענן
  //   * @param fileURL כתובת הקובץ המיועד למחיקה
    // */
  //  private void deleteFile(String fileURL) {
        // אם אין תמונה= כתובת ריקה אז לא עושים כלום מפסיקים את הפעולה
  //      if(fileURL==null){
       //     Toast.makeText(getContext(), "Theres no file to delete!!!", Toast.LENGTH_SHORT).show();
      //      return;
       // }
        // הפניה למיקום הקובץ באיחסון
     //   StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(fileURL);
        //מחיקת הקובץ והוספת מאזין שבודק אם ההורדה הצליחה או לא
       // storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
        //    @Override
        //    public void onComplete(@NonNull Task<Void> task) {
         //       if(task.isSuccessful())
          //      {
          //          Toast.makeText(getContext(), "file deleted", Toast.LENGTH_SHORT).show();
        //        }
         //       else {
         //           Toast.makeText(getContext(), "onFailure: did not delete file "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            //    }
        //    }
      //  });
    }


}

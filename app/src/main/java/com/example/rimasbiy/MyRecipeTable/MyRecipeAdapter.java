package com.example.rimasbiy.MyRecipeTable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rimasbiy.R;
import com.example.rimasbiy.userTable.Myuser;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

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
    }




}

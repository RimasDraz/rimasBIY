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

public class MyRecipeAdapter extends ArrayAdapter<Recipe> {
    private final int recipeLayout;
    /**
     *العمل يبني الارتباط
     * @param context==> رابط بالسياق
     * @param resource ==> تصميم عنصر لعرض بيانات الكائن
     */
    public MyRecipeAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.recipeLayout=resource;
    }
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View vitem= convertView;
        if (vitem==null)
            vitem= LayoutInflater.from(getContext()).inflate(R.layout.recipe_item_layout,parent,false);
        ImageView cakeimg=vitem.findViewById(R.id.cakeimg);
        ImageButton loveimageb=vitem.findViewById(R.id.loveimageb);
        ImageButton shareimageb=vitem.findViewById(R.id.shareimageb);
        EditText nameCake=vitem.findViewById(R.id.nameCake);
        TextView disText=vitem.findViewById(R.id.disText);

        Recipe current=getItem(position);
        /**
         * عرض المعطيات على حقول الرسم
         */
        nameCake.setText(current.getName());
        disText.setText(current.getDescription());
        return vitem;
    }




}

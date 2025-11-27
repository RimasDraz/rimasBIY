package com.example.rimasbiy.MyRecipeTable;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

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
    public
}

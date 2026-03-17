package com.example.rimasbiy.MyRecipeTable;
import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import androidx.room.PrimaryKey;

import com.example.rimasbiy.signup;
import com.example.rimasbiy.userTable.Myuser;

import java.io.Serializable;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

 /**
 *  *فئة تمثل وصفة
 *  */
@Entity//تحديد الفئة كجدول قاعدة البيانات وجدول الي منرتيب فيه المعطيات
public class Recipe implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private String ingredients;//مكونات
    private String instructions;// تعليمات
    private String image;// صورة الوصفة
    private String key;//المعرف الفريد الخاص بـ Firebase لربط الكائن المحلي بالسحابي

    @NonNull
    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", instructions='" + instructions + '\'' +
                ", image='" + image + '\'' +
                ",key='"+ key+ '\''+
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey(){return key;}
   public void setKey(String key){ this.key=key;}
}
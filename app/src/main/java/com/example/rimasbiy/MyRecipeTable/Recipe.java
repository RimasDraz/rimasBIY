package com.example.rimasbiy.MyRecipeTable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

/**
 * פריט נתונים המייצג מתכון במסד הנתונים Room.
 * فئة تمثل وصفة في قاعدة بيانات Room.
 */
@Entity // הגדרת המחלקה כטבלה ב-Database - تعريف الفئة كجدول
public class Recipe implements Serializable {

    @PrimaryKey(autoGenerate = true) // מפתח ראשי שנוצר אוטומטית - مفتاح أساسي تلقائي
    private int id;
    private String owner;
    private String name;        // שם המתכון - اسم الوصفة
    private String description; // תיאור קצר - وصف قصير
    private String ingredients; // רכיבים - المكونات
    private String instructions;// הוראות הכנה - التعليمات
    private String image;       // נתיב או URL של התמונה - رابط الصورة
    private String key;         // מזהה ייחודי (לשעבר עבור Firebase) - معرف فريد
    private long reminderTime;  // זמן תזכורת במילישניות - وقت التذكير

    // --- Getters & Setters ---
    // פונקציות אלו נחוצות ל-Room כדי לגשת לנתונים - هذه الدوال ضرورية لـ Room للوصول للبيانات

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public long getReminderTime() { return reminderTime; }
    public void setReminderTime(long reminderTime) { this.reminderTime = reminderTime; }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Recipe{id=" + id + ", name='" + name + "'}";
    }
}

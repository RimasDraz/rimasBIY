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
@Entity
    @Database(entities = {Myuser.class, Recipe.class}, version = 1)
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
        public abstract MyuserQuery myuserQuery();
    /**
     * يعيد كائن لعمليات جدول المهام
     * @return
     */
    public abstract MyRecipeQuery myRecipeQuery();

    /**
     * بناء قاعدة بيانات واعادة كائن يؤشرعليها
     * @param context
     * @return
     */
        public static AppDatabase getInstance(Context context) {
            if (db == null) {
                db = Room.databaseBuilder(context,AppDatabase.class, "app_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
            }
            return db;
        }
    }



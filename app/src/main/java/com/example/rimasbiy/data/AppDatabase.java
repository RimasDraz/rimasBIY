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
    public abstract class AppDatabase extends RoomDatabase {
        private static AppDatabase db;
        public abstract MyuserQuery myuserQuery();
        public abstract MyRecipeQuery myRecipeQuery();
        public static AppDatabase getInstance(Context context) {
            if (db == null) {
                db = Room.databaseBuilder(context,AppDatabase.class, "app_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
            }
            return db;
        }
    }



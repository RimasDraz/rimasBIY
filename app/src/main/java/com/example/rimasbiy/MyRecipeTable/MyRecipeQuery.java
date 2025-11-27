package com.example.rimasbiy.MyRecipeTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface MyRecipeQuery {
    @Query("SELECT * FROM Recipe")
    List<Recipe> getAll();
    @Query("SELECT * FROM Recipe WHERE id IN(:ids)")
    List<Recipe> loadAllByIds(int[] ids);
    @Query("SELECT * FROM Recipe WHERE name = :name")
    Recipe findByName(String name);
    @Insert
    void insertAll(Recipe... recipes);
    @Delete
    void delete (Recipe recipe);
    @Query("DELETE FROM Recipe WHERE id = :id")
    void delete(int id);
    @Insert
    void insert(Recipe recipe);
    @Update
    void update(Recipe...values);


    }


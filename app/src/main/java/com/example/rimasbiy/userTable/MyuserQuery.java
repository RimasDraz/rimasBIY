package com.example.rimasbiy.userTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyuserQuery {
    @Query("SELECT * FROM myuser")
    List<Myuser> getAll();
    @Query("SELECT * FROM myuser WHERE id IN(:ids)")
    List<Myuser>loadAllByIds(int[] ids);
    @Query("SELECT * FROM myuser WHERE email = :email AND password = :password")
    Myuser checkEmail(String email,String password);
    @Query("SELECT * FROM myuser WHERE email = :email")
    Myuser checkEmail(String email);
    @Insert
    void insertAll(Myuser... myusers);
    @Delete
    void delete (Myuser myuser);
    @Query("DELETE FROM myuser WHERE id = :id")
    void delete(int id);
    @Insert
    void insert(Myuser myuser);
    @Update
    void update(Myuser...values);
}

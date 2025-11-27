package com.example.rimasbiy.userTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
/**
 * واجهة تحوي عمليات\دوال\استعلامات على قاعدة البيانات
 */

@Dao//لتحديد ان الواجهة تحتوي استعلامات على قاعدة بيانات
public interface MyuserQuery {  //استخراج جميع المستعملين
    @Query("SELECT * FROM myuser")
    List<Myuser> getAll();
   // id استخراج مستعمل حسب رقم المميز له
    @Query("SELECT * FROM myuser WHERE id IN(:ids)")
    List<Myuser>loadAllByIds(int[] ids);
    //هل المستعمل موجود حسب الايميل وكلمة السر
    @Query("SELECT * FROM myuser WHERE email = :email AND password = :password")
    Myuser checkEmailPassw(String email,String password);
    //فحص هل الايميل موجود من قبل
    @Query("SELECT * FROM myuser WHERE email = :email")
    Myuser checkEmail(String email);
    //اضافة مستعمل او مجموعة مستعملين
    @Insert
    void insertAll(Myuser... myusers);
    //حذف
    @Delete
    void delete (Myuser myuser);
    //حذف حسب الرقم المميز id
    @Query("DELETE FROM myuser WHERE id = :id")
    void delete(int id);
    //اضافة مستعمل واحد
    @Insert
    void insert(Myuser myuser);
    //تعديل مستعمل او قائمة مستعملين
    @Update
    void update(Myuser...values);
}


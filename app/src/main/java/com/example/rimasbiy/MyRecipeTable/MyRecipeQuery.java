package com.example.rimasbiy.MyRecipeTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
/**
 * @param  توثيق 1-الدالة
 *               2-تسهيل الفهم
 */

/**
 * واجهة استعلامات على جدول معطيات
 */
@Dao //بوابة للتعامل مع قاعدة البيانات
public interface MyRecipeQuery {
    /**
     * اعادة جميع معطيات جدول الوصفات
     * @return قائمة من الوصفات
     */
    @Query("SELECT * FROM Recipe")
    List<Recipe> getAll();

    /**
     * ارجاع الوصفات من جدول Recipe
     * تستخدم لما بدي اعرض كل الوصفات بالتطبيق
     * @param ids
     * @return
     */
    @Query("SELECT * FROM Recipe WHERE id IN(:ids)")
    List<Recipe> loadAllByIds(int[] ids);
    /**
     * ارجاع وصفات محددة حسب المجموعة المذكورة بالمصفوقة
     * @param name
     * @return
     */
    @Query("SELECT * FROM Recipe WHERE name = :name")
    Recipe findByName(String name);

    /**
     * ارجاع الوصفة حسب الاسم
     * @param recipes
     */
    @Insert// لادخال البيانات
    void insertAll(Recipe... recipes);//ثلث نقاط تعني مجموعة
    /**
     * لاضافة وصفة او اكثر
     * @param recipe
     */
    @Delete
    void delete (Recipe recipe);

    /**
     * حذف الوصفة
     * @param id
     */
    @Query("DELETE FROM Recipe WHERE id = :id")
    void delete(int id);

    /**
     * حذف
     * @param recipe
     */
    @Insert //اضافة بيانات
    void insert(Recipe recipe);
    @Update// تعديل بيانات موجودة في قاعدة البيانات بناءا على primary kiy
    //اي تغير معلومات صف موجود دون اضافة ما تضيف صف جديد
    void update(Recipe...values);
    }


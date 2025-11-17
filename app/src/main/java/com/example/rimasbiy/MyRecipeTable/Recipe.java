package com.example.rimasbiy.MyRecipeTable;
import androidx.annotation.NonNull;
import androidx.room.Entity;

import androidx.room.PrimaryKey;
@Entity
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private String ingredients;
    private String instructions;
    private String image;
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
                '}';
    }


}

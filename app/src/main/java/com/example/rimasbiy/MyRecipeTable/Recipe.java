package com.example.rimasbiy.MyRecipeTable;

import androidx.room.PrimaryKey;

public class Recipe {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private String ingredients;
    private String instructions;
    private String image;
}

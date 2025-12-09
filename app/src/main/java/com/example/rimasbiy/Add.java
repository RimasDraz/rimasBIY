package com.example.rimasbiy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rimasbiy.MyRecipeTable.Recipe;
import com.example.rimasbiy.data.AppDatabase;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Add extends AppCompatActivity {
    private Button buttonsaverecipe;
    private TextInputEditText recipename;
    private TextInputEditText description;
    private TextInputEditText ingredients;
    private TextInputEditText instructions;
    private ImageView imagerecipe;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        buttonsaverecipe = findViewById(R.id.buttonsaverecipe);
        recipename = findViewById(R.id.recipename);
        description = findViewById(R.id.description);
        ingredients = findViewById(R.id.ingredients);
        instructions = findViewById(R.id.instructions);
        imagerecipe = findViewById(R.id.imagerecipe);


    }
    private boolean validateFields() {
        boolean flag = true;

        if(recipename.getText().toString().isEmpty() ) {
            recipename.setError("Invalid recipe name");
            flag = false;
        }

        if(description.getText().toString().isEmpty()) {
            description.setError("Description is required");
            flag = false;
        }

        if(ingredients.getText().toString().isEmpty()) {
            ingredients.setError("Ingredients are required");
            flag = false;
        }

        if(instructions.getText().toString().isEmpty()) {
            instructions.setError("Instructions are required");
            flag = false;
        }
    if(flag)
    {
        /**
        بناء كائن
         */
        Recipe recipe = new Recipe();
        /**
        *استخراج القيم من الصفات واعطائها للحقولا
         */
        recipe.setName(recipename.getText().toString());
        recipe.setDescription(description.getText().toString());
        recipe.setIngredients(ingredients.getText().toString());
        recipe.setInstructions(instructions.getText().toString());
        /**
         * ادخال البيانات في قاعدة البيانات
         */
        AppDatabase.getInstance(this).myRecipeQuery().insert(recipe);
    }
        return flag;
    }
    public void onClick(View view) {
        Intent i=new Intent(Add.this, ListRecipes.class);
        startActivity(i);
    }
}
package com.example.rimasbiy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecipeDetails extends AppCompatActivity {
    private Button btnsave, btnstart;
    private ListView list;
    private TextView recipen, description, direction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnsave = findViewById(R.id.btnsave);
        btnstart = findViewById(R.id.btnstart);
        list = findViewById(R.id.list);
         recipen= findViewById(R.id.recipen);
        description = findViewById(R.id.description);
        direction = findViewById(R.id.direction);
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RecipeDetails.this, TimerScreen.class);
                startActivity(i);
            }
        });

    }
}
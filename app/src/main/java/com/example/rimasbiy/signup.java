package com.example.rimasbiy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class signup extends AppCompatActivity {
    private EditText EmailText;
    private EditText TextPhone;
    private EditText TextPassword;
    private  EditText TextPasswordd;
    private Button btnsign;
    private TextView textup;
    private TextView textVPP;
    private TextView textVP;
    private TextView textVE;
    private TextView textVPH;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;});

        EmailText=findViewById(R.id.EmailText);
        TextPhone=findViewById(R.id.TextPhone);
        TextPassword=findViewById(R.id.TextPassword);
        TextPasswordd=findViewById(R.id.TextPasswordd);
        textup=findViewById(R.id.textup);
        btnsign=findViewById(R.id.btnsign);
        textVPP=findViewById(R.id.textVPP);
        textVP=findViewById(R.id.textVP);
        textVE=findViewById(R.id.textVE);
        textVPH=findViewById(R.id.textVPH);


        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(signup.this, HomeScreen.class);
                startActivity(i);
            }
        });

    }
}
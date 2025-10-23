package com.example.rimasbiy;

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
    private  EditText TextPassword2;
    private Button btnsign;
    private TextView textup;



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
        TextPassword2=findViewById(R.id.TextPassword2);
        textup=findViewById(R.id.textup);


        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(signup.this, HomeScreen.class);
                startActivity(i);
            }
        });

    }
}
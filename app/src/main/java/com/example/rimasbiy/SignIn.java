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

public class SignIn extends AppCompatActivity {
private TextView tvAcount;
private EditText username;
private EditText TextPassword;
private Button btnLogin;
private TextView orr;
private Button btnSignup;

@Override
    protected void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_sign_in);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        return insets;});
    tvAcount=findViewById(R.id.tvAcount);
    username=findViewById(R.id.username);
    TextPassword=findViewById(R.id.TextPassword);
    btnLogin=findViewById(R.id.btnLogin);
    btnSignup=findViewById(R.id.btnSignup);

    btnSignup.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i=new Intent(SignIn.this, HomeScreen.class);
            startActivity(i);
        }
    });
    btnLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent r=new Intent(SignIn.this, signup.class);
            startActivity(r);
        }
    });



    }

}
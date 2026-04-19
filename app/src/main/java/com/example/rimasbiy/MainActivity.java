package com.example.rimasbiy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ImageView imgsplash;//صورة الخلفية

    @SuppressLint("MissingInflatedId")
    @Override
    //بتشتغل أول ما يفتح التطبيق
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);//عرض صورة الخلفية (Splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        final Handler handler = new Handler();//تنفيذ كود بعد مدة زمنية معينة
        handler.postDelayed(new Runnable() {//تحبر البرنامج يستنى شوي وبعدها ينفذ الكود الموجود
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, SignIn.class);//امر الانتقال تحديد البداية والوجهة
                startActivity(i);//الامر الفعلي الذي يفتح الشاشة االي بدنا ننقل عليها
            }
        }, 3000); // 3000 = 3 seconds in milliseconds وقت الانتظار





            }

    }

package com.example.rimasbiy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rimasbiy.data.AppDatabase;
import com.example.rimasbiy.userTable.Myuser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity {
    private EditText EmailText;
    /**
     * حقل الايميل
     */
    private EditText TextPhone;
    /**
     * حقل رقم الهاتف
     */
    private EditText TextPassword;
    /**
     * حقل الكلمة السر
     */
    private  EditText TextPasswordd;
    /**
     * تأكيد كلمة السر
     */
    private Button btnsign;
    /**
     * زر التسجيل
     */
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


        /**
         * عند الضغط على زر btnsign يتم التحقق من صحة الحقول باستخدام الدالة validateFields()
         * إذا كانت الحقول صالحة، يتم انشاء كائن جديد من Myuser وتم ادخال البيانات في الحقول
         * في قاعدة البيانات باستخدام AppDatabase.getInstance(signup.this).myuserQuery().insertAll(myuser);
         * ثم يتم انتقل إلى الصفحة الرئيسية HomeScreen
         * إذا كانت الحقول غير صالحة، يتم عرض رسالة خطأ باستخدام Toast
         */

        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(validateFields())
               {
                   Intent i=new Intent(signup.this, HomeScreen.class);
                   startActivity(i);
               }
               else {
                   Toast.makeText(signup.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }

    /**
     * الدالة تتحقق بما اذا كانت حقول الe-mail,phone,password,confirm password صالحين.
     * اذا كان اي من الحقول غير صالح, يتم تعيين خطأ في الحقل ويتم تعيين علامة خطأ.
     * اذا كان كل الحقول صالحين, يتم تعيين علامة صحيح في الحقل ويتم تعيين علامة صحيح.
     * اذا كانت جميع الحقول صالحة يتم انشاء كائن جديد my user
     * @return
     */
    private boolean validateFields() {
        boolean flag = true;

        String email = EmailText.getText().toString();
        String phone = TextPhone.getText().toString();
        String password = TextPassword.getText().toString();
        String confirmPassword = TextPasswordd.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EmailText.setError("Invalid email");
            flag = false;
        }

        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            TextPhone.setError("Invalid phone number");
            flag = false;
        }

        if (password.isEmpty() || password.length() < 8) {
            TextPassword.setError("Password must be at least 8 characters");
            flag = false;
        }

        if (!password.equals(confirmPassword)) {
            TextPasswordd.setError("Passwords do not match");
            flag = false;
        }
        if(flag)
        {
            Myuser myuser = new Myuser();
            myuser.setEmail(email);
            myuser.setPhone(phone);
            myuser.setPassword(password);
            AppDatabase.getInstance(signup.this).myuserQuery().insertAll(myuser);
        }

        return flag;
    }

}
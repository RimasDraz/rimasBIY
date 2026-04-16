package com.example.rimasbiy;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rimasbiy.data.AppDatabase;
import com.example.rimasbiy.userTable.Myuser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
private TextView tvAcount;// Have an account?
/**
 * حقل الايميل
 */
private EditText username;
/**
 * حقل كلمة المرور
 */
private EditText TextPassword;
private Button btnLogin;
private TextView orr;
private Button btnSignup;
private TextView textVi;//E-mail
 private TextView textV;//password

@Override
    protected void onCreate(Bundle savedInstanceState)//بتشتغل أول ما الشاشة تفتح ,بتربط كلاس SignIn مع ملف XML تبع الواجهة.
{
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_sign_in);//تحديد ملف التننسيق للشاشة, بناء الكائنات
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        return insets;});

    FirebaseAuth auth=FirebaseAuth.getInstance();
    if(auth.getCurrentUser()!=null)
    {

        Intent i= new Intent(SignIn.this,ListRecipes.class);
        startActivity(i);
        finish();
    }
//هون عم نربط عناصر الواجهة (EditText, Button...) بالكود عشان نقدر نتحكم فيهم
    tvAcount=findViewById(R.id.tvAcount);
    username=findViewById(R.id.username);
    TextPassword=findViewById(R.id.TextPassword);
    btnLogin=findViewById(R.id.btnLogin);
    btnSignup=findViewById(R.id.btnSignup);

    btnSignup.setOnClickListener(new View.OnClickListener() {// هون الانتقال بيصير من: SignIn ➜ signup لما المستخدم يضغط زر التسجيل
        @Override
        public void onClick(View v) {
            Intent i=new Intent(SignIn.this, signup.class);
            startActivity(i);
        }
    });
    btnLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {//  هون الانتقال بيصير من: SignIn ➜ ListRecipes اذا التسجيل نجح
            validateFields();
        }
    });
    }
    // explaination: validateFields() checks if the username and password fields are valid
    // if either field is empty or does not match the required pattern, an error is set on the field
    // and the flag is set to false, indicating that the fields are invalid
    // if the fields are valid, a new Myuser object is created and the flag is set to true, indicating that the fields are valid
private boolean validateFields(){ // بتفحص اذا الباسورد والايميل وبترجعىtrue إذا كلشي تمام false إذا في خطأ
    boolean flag=true;//المتغير بيحدد إذا البيانات صحيحة أو لا.
    String usernameText=username.getText().toString();
    String passwordText=TextPassword.getText().toString();

    if(usernameText.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(usernameText).matches()){
        username.setError("Invalid username");
        flag=false;

    }

    if(passwordText.isEmpty() || passwordText.length()<8){
        TextPassword.setError("Password must be at least 8 characters");
        flag=false;
    }
    if(flag)
    {
        //إنشاء كائن Myuser
        Myuser myuser = new Myuser();
        myuser.setEmail(usernameText);
        myuser.setPassword(passwordText);
        AppDatabase.getInstance(SignIn.this).myuserQuery().insertAll(myuser);//حفظه بقاعدة البيانات
        //كائن لعملية التسجيل
        FirebaseAuth auth=FirebaseAuth.getInstance();
        //الدخول بمساعدة الايميل والسسما
        auth.signInWithEmailAndPassword(usernameText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
           //تستنى Firebase يخلص عملية تسجيل الدخول وبعدها بتنفذ الكود الي جواaddOnCompleteListener
           @Override
           //هاي دالة بتشتغل لما Firebase يخلص عملية تسجيل الدخول
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignIn.this,"Sining in Succeeded", LENGTH_SHORT).show();
                    //الانتقال للصفحة الرئيسية
                    Intent i=new Intent(SignIn.this,ListRecipes.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(SignIn.this,"sining in failed", LENGTH_SHORT).show();
                    username.setError(task.getException().getMessage());
                }
            }
        });
    }
    return flag;
}
}
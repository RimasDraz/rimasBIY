package com.example.rimasbiy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.ai.FirebaseAI;
import com.google.firebase.ai.GenerativeModel;
import com.google.firebase.ai.java.GenerativeModelFutures;
import com.google.firebase.ai.type.Content;
import com.google.firebase.ai.type.GenerateContentResponse;
import com.google.firebase.ai.type.GenerativeBackend;

import java.util.concurrent.Executor;
//وظيفته ببساطة هي استقبال "موضوع" أو "أكلة" من المستخدم، وإرسالها إلى ذكاء Gemini الاصطناعي ليقوم بتوليد خطوات واضحة (Checklist) لكيفية تنفيذ هذه المهمة.
public class SmartRecipeAssistant extends AppCompatActivity {
    private GenerativeModelFutures model;
    private TextView tvSmartid;
    private Button btnSuggestSteps;
    private EditText etTaskTopic;
    private TextView tvAiResponse;
    private ProgressBar pbLoading;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_smart_recipe_assistant);
        btnSuggestSteps=findViewById(R.id. btnSuggestSteps);
        tvSmartid=findViewById(R.id. tvSmartid);
        etTaskTopic=findViewById(R.id.etTaskTopic);
        tvAiResponse=findViewById(R.id.tvAiResponse);
        pbLoading=findViewById(R.id.pbLoading);
        //تهيئة خدمة الواجهة الخلفية (Backend) الخاصة ببرمجية Gemini
        //إنشاء كائن (Instance) من نوع "GenerativeModel" باستخدام نموذج يدعم حالة الاستخدام الخاصة بك
        GenerativeModel ai = FirebaseAI.getInstance(GenerativeBackend.googleAI())
                .generativeModel("gemini-3-flash-preview");//gemini-3-flash-preview هذا النموذج يتميز بالسرعة والكفاءة العالية في الرد على النصوص.أنت تطلب من Firebase تزويدك بنسخة من نموذج
//"استخدم طبقة توافق Java (GenerativeModelFutures) التي توفر الدعم لواجهات برمجة التطبيقات من نوع ListenableFuture و Publisher."
        //ListenableFuture: هو نمط في البرمجة يُستخدم لتنفيذ المهام في الخلفية (Asynchronous). بدلاً من انتظار الرد وتوقف التطبيق، يخبرك الكود "سأعطيك النتيجة مستقبلاً فور جهوزيتها".
        //Publisher APIs: تتعلق بالبرمجة التفاعلية (Reactive Programming)، حيث يتم "نشر" البيانات أو النتائج كتدفق مستمر  بدلاً من قطعة واحدة.
        model = GenerativeModelFutures.from(ai); //GenerativeModelFutures. هذا الكلاس هو الذي يضمن أن التطبيق لن يتجمد (Freeze) أثناء انتظار رد الذكاء الاصطناعي من الإنترنت.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String text=etTaskTopic.getText().toString();
        btnSuggestSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askFirebaseAiGeminiForSteps(text);

            }
        });
    }
    /**
     *
     * @param topic
     */
    //هذه هي الدالة التي تُنفذ عندما يطلب المستخدم المساعدة
    private void askFirebaseAiGeminiForSteps (String topic) {
        pbLoading.setVisibility(View.VISIBLE);//إظهار دائرة التحميل لإعلام المستخدم أن التطبيق "يفكر"
        tvAiResponse.setText("");// تفريغ النص القديم
        btnSuggestSteps.setEnabled(false);//تعطيل الزر لمنع المستخدم من الضغط عليه عدة مرات وإرسال طلبات متكررة.
        askFirebaseAiGeminiForSteps("");
//هنا يتم دمج مدخلات المستخدم في جملة إنجليزية احترافية تطلب من Gemini تقسيم المهمة إلى خطوات واضحة.
        String promptStr = "I want to perform the following Recipe: '" + topic + "'. " +
                "Can you suggest a clear, step-by-step checklist to  effectively?";

//. بناء كائن المحتوى) المطلوب من قبل مكتبة
        Content prompt = new Content.Builder()
                .addText(promptStr)
                .build();

//إرسال الطلب واستلام "وعد" (Future) بالاستجابة المستقبليّة
        ListenableFuture<GenerateContentResponse> response = model.generateContent(prompt);
        Executor executor = this::runOnUiThread;//هذا يضمن أن تحديث النصوص والأزرار سيتم على الشاشة مباشرة بدون أخطاء
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {//إضافة "مراقب" (Callback) لانتظار النتيجة (نجاح أو فشل)
            @Override
            // في حالة النجاح
            public void onSuccess(GenerateContentResponse result) {
                pbLoading.setVisibility(View.GONE);//إخفاء مؤشر التحميل
                btnSuggestSteps.setEnabled(true);// إعادة تفعيل الزر
                // عرض النص المولد من الذكاء الاصطناعي في الواجهة
                tvAiResponse.setText(result.getText());
            }


            @Override
            // في حالة الفشل (مثلاً: انقطاع الإنترنت أو خطأ في الخادم)
            public void onFailure(Throwable t) {
                pbLoading.setVisibility(View.GONE);
                btnSuggestSteps.setEnabled(true);
                // عرض رسالة خطأ للمستخدم توضح المشكلة
                Toast.makeText(SmartRecipeAssistant.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, executor);//الـ executor هو "جسر العودة" من عملية البحث في الإنترنت إلى شاشة المستخدم، لضمان أن تظهر النتيجة بسلاسة وبدون "تعليق" أو "إغلاق مفاجئ" للتطبيق.
    }
}

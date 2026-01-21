package com.example.rimasbiy;

import android.graphics.Bitmap;
import android.icu.text.MessagePattern;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import kotlin.Result;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

/**
 * فئة مساعدة للتواصل مع خدمة الذكاء الاصطناعي التابعة google
 * Gemini
 */
public class GeminiHelper {
    public static final String GEMINI_Version = "gemini-2.0-flash";    // ‏إصدار ال gemini الذي يمكن استعماله
    private static String GEMINI_API_KEY = "your key";   // مفتاح التطبيق الذي نسخه من الموقع التابع gemini
    private static GeminiHelper instance;    // كائن وحيد الذي يساعدنا على عدم بناء أكثر من كائن لهذه الخدمة ويسمى singleton
    private GenerativeModel gemini;    // موديل الذكاء الاصطناعي

    // دالة بنائيه لبناء الموديل التابع gemini
    // ‏تحتاج دراع رقم النسخة أو الإصدار ومفتاح التطبيق للاستعمال
    private GeminiHelper() {
        gemini = new GenerativeModel(
                GEMINI_Version,
                GEMINI_API_KEY
        );
    }

    // ‏هذه العملية تساعد على عدم بناء أكثر من كائن لهذه الفئة بإرجاع مؤشر واحد
    public static GeminiHelper getInstance() {
        if (null == instance) {
            instance = new GeminiHelper();
        }
        return instance;
    }

    /*** ‏هذه العملية تتلقى جملة لإ بإرسالها لخدمة الذكاء الاصطناعي Gemini وتنتظر الرد
     * @param prompt   Geminiجملة الاستعلام أو الطلب من الذكاء الاصطناعي
     * @param callback Gemini كائن لمعالجة الرد */
    public void sendMessage(String prompt, ResponseCallback callback) {
        gemini.generateContent(prompt,
                new Continuation<GenerateContentResponse>() {
                    @NonNull
                    @Override
                    public CoroutineContext getContext() {
                        return EmptyCoroutineContext.INSTANCE;
                    }

                    //ده لك معالجة جواب خدمة الذكاء الاصطناعي Gemini للجملة التي أرسل ناها
                    @Override
                    public void resumeWith(@NonNull Object result) {
                        if (result instanceof Result.Failure) {
                            //Gemini رسالة بحالة فشل وصول الرد من خدمة الذكاء الاصطناعي
                            callback.onError(((Result.Failure) result).exception);
                        } else {
                            // إرسال النتيجة التي أعدتها خدمة الذكاء الاصطناعي كالجواب للطلب أو الجملة التي أرسلناها
                            callback.onResponse(((GenerateContentResponse) result).getText());
                        }
                    }
                }
        );

    }

    /**
     * ‏دالة للتعامل مع خدمة الذكاء الاصطناعي بعد إرسال صورة ونص خاص بهذه الصورة
     *
     * @param prompt   Gemini نص الاستعلام لخدمة الذكاء الاصطناعي
     * @param photo    Gemini الصورة التي نود إرسالها لخدمة الذكاء الاصطناعي
     * @param callback كائن لمعالجة رد خدمة الذكاء الاصطناعي Gemini
     */
    public void sendMessageWithPhoto(String prompt, Bitmap photo, ResponseCallback callback) {
        List<MessagePattern.Part> parts = new ArrayList<MessagePattern.Part>();
        parts.add(new TextPart(prompt));
        parts.add(new ImagePart(photo));
        Content[] content = new Content[1];
        content[0] = new Content(parts);


        gemini.generateContent(content,
                new Continuation<GenerateContentResponse>() {
                    @NonNull
                    @Override
                    public CoroutineContext getContext() {
                        return EmptyCoroutineContext.INSTANCE;
                    }


                    @Override
                    public void resumeWith(@NonNull Object result) {
                        if (result instanceof Result.Failure) {
                            callback.onError(((Result.Failure) result).exception);
                        } else {
                            callback.onResponse(((GenerateContentResponse) result).getText());
                        }
                    }
                }
        );
    }



}
package com.example.rimasbiy;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.ImagePart;
import com.google.ai.client.generativeai.type.Part;
import com.google.ai.client.generativeai.type.TextPart;
import com.google.android.gms.tasks.Continuation;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import kotlin.Result;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

public class GeminiHelper {
    // استخدم اسم الموديل الصحيح
    public static final String GEMINI_VERSION = "gemini-1.5-flash";
    private static final String GEMINI_API_KEY = "AIzaSyDO23pJ2gUnRvlAhkoQhOw3HACFUCQsEHc";

    private static GeminiHelper instance;
    private final GenerativeModelFutures model;
    private final Executor executor;

    private GeminiHelper() {
        // إنشاء الموديل الأساسي
        GenerativeModel gm = new GenerativeModel(GEMINI_VERSION, GEMINI_API_KEY);
        // تحويله لنسخة تدعم Java
        this.model = GenerativeModelFutures.from(gm);
        // تنفيذ العمليات في خيط منفصل لتجنب تعليق واجهة المستخدم
        this.executor = Executors.newSingleThreadExecutor();
    }

    public static synchronized GeminiHelper getInstance() {
        if (instance == null) {
            instance = new GeminiHelper();
        }
        return instance;
    }

    public void sendMessage(String prompt, ResponseCallback callback) {
        Content content = new Content.Builder()
                .addText(prompt)
                .build();

        // استخدام generateContent من النسخة المخصصة لـ Java
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                if (callback != null && result != null) {
                    callback.onResponse(result.getText());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (callback != null) {
                    callback.onError(t);
                }
            }
        }, executor);
    }
}

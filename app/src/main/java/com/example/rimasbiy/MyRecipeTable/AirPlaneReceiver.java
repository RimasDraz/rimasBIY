package com.example.rimasbiy.MyRecipeTable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

public class AirPlaneReceiver extends BroadcastReceiver {
    private Button save;
    public AirPlaneReceiver(Button buttonsaverecipe) {//الـ BroadcastReceiver عادةً لا يعرف شيئاً عن واجهة المستخدم (UI). لكي نجعله يتحكم في زر الـ "Save" الموجود في شاشة إضافة الوصفة، قمنا بتمرير هذا الزر كـ "بارامتر" عند إنشاء الكلاس. تمرير الزر من الـ Activity
        save=buttonsaverecipe;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Check if the action is Airplane Mode change
        if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(intent.getAction())) {
            boolean isEnabled = intent.getBooleanExtra("state", false);//هنا نسأل النظام: "هل وضع الطيران الآن يعمل (ON) أم لا (OFF)؟". القيمة تُخزن في المتغير isEnabled.
            if (isEnabled) {// إذا وضع الطيران شغال
                Toast.makeText(context, "System: Airplane Mode is ON. Sync is disabled.", Toast.LENGTH_LONG).show();
                save.setEnabled(false);//// إذا وضع الطيران شغال
                save.setText("AirPalne is on");//// إذا وضع الطيران شغال
            } else {/// إذا وضع الطيران شغال
                Toast.makeText(context, "System: Airplane Mode is OFF. Sync is back!", Toast.LENGTH_LONG).show();
                save.setEnabled(true);//// تفعيل الزر مجدداً
                save.setText("Save");//// إعادة النص الأصلي

            }
        }

    }
}
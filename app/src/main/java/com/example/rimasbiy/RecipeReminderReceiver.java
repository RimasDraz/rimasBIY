package com.example.rimasbiy;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
// RecipeReminderReceiver هو عبارة عن Broadcast Receiver، ووظيفته الأساسية هي استقبال "تنبيه" من النظام في وقت محدد مسبقاً لإظهار إشعار (Notification) للمستخدم لتذكيره بوصفة معينة
public class RecipeReminderReceiver extends BroadcastReceiver {
    // معرف القناة (ضروري للإشعارات في إصدارات أندرويد الحديثة)
    private static final String CHANNEL_ID = "Recipe_REMINDER_CHANNEL";
    /**
     * الدالة التي يتم استدعاؤها تلقائياً عند إطلاق المنبه (Alarm).
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        /// استخراج البيانات المرسلة مع التنبيه (عنوان الوصفة ونصها)
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");

// 2. إنشاء قناة الإشعارات (للتوافق مع إصدارات Oreo فأعلى)
        createNotificationChannel(context);
        // 3. تجهيز (Intent) لفتح شاشة عرض الوصفات عند النقر على الإشعار
        Intent resultIntent = new Intent(context, ListRecipes.class);
        //هذا السطر يخبر النظام: "عند التفاعل، قم بتشغيل النشاط (Activity) الموجود داخل resultIntent".
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

// 4. بناء تصميم الإشعار ومحتوياته
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)// الأيقونة الصغيرة
                .setContentTitle("Recipe Reminder: " + title)/// الأيقونة الصغيرة
                .setContentText(text)// النص الوصفي
                .setPriority(NotificationCompat.PRIORITY_HIGH)// النص الوصفي
                .setContentIntent(pendingIntent)// ماذا يحدث عند الضغط؟
                .setAutoCancel(true);// ماذا يحدث عند الضغط؟

// 5. إرسال الإشعار للنظام ليظهر للمستخدم
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // نستخدم الوقت الحالي كـ ID لضمان عدم تداخل الإشعارات إذا كان هناك أكثر من تذكير
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
    /**
     * دالة لإنشاء قناة الإشعارات (Notification Channel).
     * أندرويد يتطلب القنوات لتنظيم الإشعارات وإعطاء المستخدم تحكماً أكبر.
     */
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {// هذا الشرط يفحص إذا كان جهاز المستخدم يعمل بنظام أندرويد 8 (Oreo) أو أحدث. إذا كان أقدم، لا يحتاج لإنشاء قناة.
            CharSequence name = "Recipe Reminders";// الاسم الذي سيراه المستخدم في إعدادات الهاتف (مثلاً: "Recipe Reminders").
            String description = "Channel for Recipe Reminder Notifications";/// وصف يشرح للمستخدم ماذا تفعل هذه القناة.
            int importance = NotificationManager.IMPORTANCE_HIGH;//تحديد درجة الأهمية؛ "High" تعني أن الإشعار سيصدر صوتاً ويظهر في أعلى الشاشة (Pop-up)
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);//هنا نقوم بإنشاء "كائن القناة" الفعلي ونربطه بـ CHANNEL_ID الذي عرفناه في بداية الكلاس.
            channel.setDescription(description);

             // تسجيل القناة في نظام أندرويد
            //بدون هذا السطر، لن يظهر أي إشعار حتى لو كان الكود صحيحاً
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}



package com.example.rimasbiy;


import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rimasbiy.MyRecipeTable.AirPlaneReceiver;
import com.example.rimasbiy.MyRecipeTable.Recipe;
import com.example.rimasbiy.data.AppDatabase;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
import com.example.rimasbiy.userTable.MyService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
//import com.google.android.material.textfield.TextInputLayout;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
import android.net.Uri;

public class Add extends AppCompatActivity {

    //إنشاء طلب إذن
    private final ActivityResultLauncher<String> requestNotificationPermissionLauncher = registerForActivityResult( new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (!isGranted) {
                    Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private AirPlaneReceiver systemEventsReceiver;
    private Button buttonsaverecipe;
    private TextInputEditText recipename; // اسم الوصفة
    private TextInputEditText description; // وصف للوصفة
    private TextInputEditText ingredients;// مكونات
    private TextInputEditText instructions;// تعليمات
    private ImageView imagerecipe; // صورة الوصفة
   // private ImageView ivSelectedImage; //صفة كمؤشر لهذا الكائن
    private Uri selectedImageUri;//صفة لحفظ عنوان الصورة بعد اختيارها
    private ActivityResultLauncher<String> pickImage;// ‏كائن لطلب الصورة من الهاتف
    private Button button_select_image;// لاختيار الصورة
    private Button btnSetReminder;//כפתור לפתיחת חלון בחירת הזמן
    private long selectedReminderTime = 0;// הזמן שנבחר במלישניות
    private TextView tvReminderTime;// להצגת הזמן שנבחר

    String TAG="FilePermission";

    // مُشغّلات لطلب الأذونات
    private ActivityResultLauncher<String> requestReadMediaImagesPermission;
    private ActivityResultLauncher<String> requestReadMediaVideoPermission;
    private ActivityResultLauncher<String> requestReadExternalStoragePermission;

    @SuppressLint({"MissingInflatedId", "WrongViewCast", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {//هاي أول دالة بتشتغل لما تنفتح شاشة إضافة وصفة
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);//تحديد ملف التننسيق للشاشة, بناء الكائنات,
        buttonsaverecipe = findViewById(R.id.buttonsaverecipe);// البحث عن العنصر الذي يحمل المعرف IDالمسمى buttonsaverecipe في ملف التصميم وربطه بالمتغير البرمجيbuttonsaverecipe
        recipename = findViewById(R.id.recipename);
        description = findViewById(R.id.description);
        ingredients = findViewById(R.id.ingredients);
        instructions = findViewById(R.id.instructions);
        imagerecipe = findViewById(R.id.imagerecipe);
        button_select_image=findViewById(R.id.button_select_image);
        tvReminderTime=findViewById(R.id.tvReminderTime);
        btnSetReminder=findViewById(R.id.btnSetReminder);
        systemEventsReceiver=new AirPlaneReceiver(buttonsaverecipe);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
        //تسجيل طلب الأذونات دالة بتجهز كائن عشان:
        //يطلب إذن قراءة الصور
       //ويتأكد إذا المستخدم وافق أو رفض
        requestReadMediaImagesPermission=registerForActivityResult(new ActivityResultContracts.RequestPermission(),isGranted->
        {

            if (isGranted){
                Log.d(TAG,"READ_MEDIA_IMAGES permission granted");
                Toast.makeText(this,"تم منح إذن قراءة الصور", Toast.LENGTH_SHORT).show();

            } else {
                Log.d(TAG, "READ_MEDIA_IMAGES permission denied");
                Toast.makeText(this, "تم رفض إذن قراءة الصور", Toast.LENGTH_SHORT).show();
                // التعامل مع حالة رفض الإذن

            }
        });
//دالة مسؤولة عن: فتح معرض الصور
// لما المستخدم يختار صورة:  تخزن الرابط فيselectedImageUri تعرض الصورة في  ImageView
        requestReadMediaVideoPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d(TAG, "READ_MEDIA_VIDEO permission granted");
                Toast.makeText(this, "تم منح إذن قراءة الفيديو", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "READ_MEDIA_VIDEO permission denied");
                Toast.makeText(this, "تم رفض إذن قراءة الفيديو", Toast.LENGTH_SHORT).show();
                // التعامل مع حالة رفض الإذن
            }
        });
        requestReadExternalStoragePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d(TAG, "READ_EXTERNAL_STORAGE permission granted");
                Toast.makeText(this, "تم منح إذن قراءة التخزين الخارجي", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "READ_EXTERNAL_STORAGE permission denied");
                Toast.makeText(this, "تم رفض إذن قراءة التخزين الخارجي", Toast.LENGTH_SHORT).show();
                // التعامل مع حالة رفض الإذن
            }
        });

// Initialize the ActivityResultLauncher for picking images
        pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    selectedImageUri = result;
                    imagerecipe.setImageURI(result);
                    imagerecipe.setVisibility(View.VISIBLE);
                }
            }
        });


        imagerecipe.setOnClickListener(new View.OnClickListener() {//لما ينكبس على الـ ImageView نفّذ الكود اللي جواته
            @Override
            public void onClick(View v) {//هاي الدالة اللي بتشتغل عند الضغط.
                pickImage.launch("image/*"); // Launch the image picker(اختار أي نوع ملف من نوع صورة/*)
            }
        });

        btnSetReminder.setOnClickListener(v -> showDateTimePicker());//استدعاء إجراء عند النقر على زر btnSetReminder: (يتم الاستدعاء باستخدام دالة lamda السهمية <- هذا هو onClick)

        checkAndRequestPermissions();//استدعاء دالة الأذونات

        //تفعيل طلب الإذن
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
//فحص إذا كل الحقول معبّاية.
            private boolean validateFields () {
                boolean flag = true;

                if (recipename.getText().toString().isEmpty()) {
                    recipename.setError("Invalid recipe name");
                    flag = false;
                }

                if (description.getText().toString().isEmpty()) {
                    description.setError("Description is required");
                    flag = false;
                }

                if (ingredients.getText().toString().isEmpty()) {
                    ingredients.setError("Ingredients are required");
                    flag = false;
                }

                if (instructions.getText().toString().isEmpty()) {
                    instructions.setError("Instructions are required");
                    flag = false;
                }
                if (flag) {
                    /**
                     بناء كائن
                     */
                    Recipe recipe = new Recipe();//إنشاء كائن Recipe
                    /**
                     *استخراج القيم من الصفات واعطائها للحقولا
                     */
                    recipe.setName(recipename.getText().toString());//عبي القيم داخله
                    recipe.setDescription(description.getText().toString());
                    recipe.setIngredients(ingredients.getText().toString());
                    recipe.setInstructions(instructions.getText().toString());
                    recipe.setReminderTime(selectedReminderTime);
                    recipe.setOwner(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    //هذه الدالة يمكن استدعاؤها بعد استلام عنوان الصورة Uri وتحويلها لنص لحفظها في الصفة المخصصة لها في الكائن
                    if (selectedImageUri!=null)
                    {
                        //تحويل الصورة وحفظها
                        recipe.setImage(convertImageToString(selectedImageUri));                    }
                    /**
                     * ادخال البيانات في قاعدة البيانات
                     */
                    AppDatabase.getInstance(this).myRecipeQuery().insert(recipe);
                    Toast.makeText(this, "Recipe saved successfully", Toast.LENGTH_SHORT).show();
                    //save via frirebase database
                  saveRecipe(recipe);
                //save by service
                    Intent serviceIntent=new Intent(this, MyService.class);
                    serviceIntent.putExtra("recipe_extra",recipe);
                    serviceIntent.putExtra("group","recipes");
                    startService(serviceIntent);
                    finish();
                    scheduleAlarm(recipe);

                }
                return flag;
            }
    private void checkAndRequestPermissions() {//تفحص إذا التطبيق عنده إذن قراءة الصور
        // فحص وطلب إذن READ_MEDIA_IMAGES (للإصدارات الحديثة)
        //إذا الإذن مش موجود → تطلبه
        //إذا موجود → تطبع رسالة إنه موجود
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadMediaImagesPermission.launch(android.Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                Log.d(TAG, "READ_MEDIA_IMAGES permission already granted");
                Toast.makeText(this, "إذن قراءة الصور ممنوح بالفعل", Toast.LENGTH_SHORT).show();
            }
//        });
//بنخليه يراقب العنصر، وأول ما المستخدم يضغط → ينفّذ كود معيّن.
            buttonsaverecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validateFields();
//                    if (validateFields()) {
//                        buttonsaverecipe.setEnabled(false);
//                        buttonsaverecipe.setText("Saving...");
//                        //postDelayed تشغل ثريد
//                        buttonsaverecipe.postDelayed(new Runnable() {
//                            // انه تشغيل مقطع كود بنفس الوقت مع ثريد ثاني وخاصة مع التطييق الخاصة بنا
//                            @Override
//                            public void run() {
//                                buttonsaverecipe.setEnabled(true);
//                                buttonsaverecipe.setText("Save");
//                                finish();
//                            }
//                            //تشغل ثريد لمدة 2ث
//                        }, 2000);
//                    }
                }
            });
        }
    }
    //* Converts an image Uri to a Base64 string.
//*
//* @param uri The Uri of the image to convert.
//* @return The Base64 string representation of the image.
    //تحويل الصورة لنص
    public String convertImageToString(Uri uri) {
        InputStream inputStream = null;
        String imageString = null;
        // تحتوي هذه الدالة على وظيفة تحويل الصورة من مكان التخزين المؤقت إلى نص بنموذج Base64 ليتم تخزينه في قاعدة البيانات، وهذا يتيح للبرنامج عرض الصورة من قاعدة البيانات في وقت لاحق بدون الحاجة إلى فتح الصورة من جهاز المستخدم.
        try {
            inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                Toast.makeText(this, "Failed to process image", Toast.LENGTH_SHORT).show();
                return null;
            }
            // Compress image to keep Base64 string within reasonable limit
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return imageString;
        }
        catch (FileNotFoundException e) {
            Toast.makeText(this, "Failed file not found", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
    }
    //حفظ الوصفة في Firebase Realtime Database
    public void saveRecipe(Recipe recipe) {//في قاعدة البيانات "recipe" الحصول على مرجع الى عقدة
        //تهيئة  Firebase Realtime Database // مؤشر لقاعدة البيانات
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        //مؤشر لجدول الوصفات
        DatabaseReference RecipesRef = database.child("recipes");// يمثل مؤشرا او مرحعا لمسار محدد في شجرة البيانات
        //انشاء مفتاح فريد للوصفة الجديدة
        DatabaseReference newRecipeRef = RecipesRef.push();
        //تعيين  معرف الوصفة في الكائن Recipe
        recipe.setKey(newRecipeRef.getKey());
        //حفظ بيانات الوصفة في قاعدة البيانات
        //اضافة كائن "لمجموعة" الوصفات ومعالج حدث لفحص تجاح المطلوب
        //معالج حدث لحفص هل تم المطلوب من قاعدة البيانات
        newRecipeRef.setValue(recipe).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Add.this, "Succeeded to add recipe", Toast.LENGTH_SHORT).show();
                        finish();
                        //تم حفظ البيانات بنجاح
                        Log.d(TAG, ":تم حفظ الوصفة بنجاح" + recipe.getId());
                        //تحديث واجهة المستخدم او تنفيذ اجراءات اخرى

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //معالجة الاخطاء
                        Log.e(TAG,"خطأ في حفظ الوصفة:"+e.getMessage(),e);
                        Toast.makeText(Add.this,"failed to add Recipe",Toast.LENGTH_SHORT).show();
                        //عرض رسالة خطأ للوصفة
                    }
                });
    }
    // 2. قم بتسجيله في onStart (عندما يصبح النشاط مرئيًا)
    @Override
    protected void onStart() {
        super.onStart();
        // تسجيل الreceiver
        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(systemEventsReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 3. قم بإلغاء تسجيله في onStop (عندما يصبح النشاط غير مرئي)
        unregisterReceiver(systemEventsReceiver);
    }
    //إجراء يفتح مربع حوار (نافذة) لاختيار الوقت ويحفظ الوقت المحدد.
    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        //יצירת דיאלוג וטיפול באירוע הזמן שנבחר
        new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {//אירוע בחירת הזמן
            date.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);//הזמן שנבחר
                date.set(Calendar.MINUTE, minute);
                date.set(Calendar.SECOND, 0);
                selectedReminderTime = date.getTimeInMillis();// הזמן שנבחר במלישניות
                tvReminderTime.setText(date.getTime().toString());//הצגת הזמן
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
//العمليية التي تشغل الوقت
    private void scheduleAlarm(Recipe recipe) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //תזמון הפעלה של
        //RecipeReminderReceiver
        Intent intent = new Intent(this, RecipeReminderReceiver.class);
        //מעבירים את הנתונים לברודקסט רסיבר
        intent.putExtra("title", recipe.getName());//
        intent.putExtra("text", recipe.getDescription());//
        //הכנת אובייקט תיזמון
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) recipe.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            //יוצרים לפי גרסת מערכת הטלפון
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, selectedReminderTime, pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, selectedReminderTime, pendingIntent);
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, selectedReminderTime, pendingIntent);
            }
        }
    }




}
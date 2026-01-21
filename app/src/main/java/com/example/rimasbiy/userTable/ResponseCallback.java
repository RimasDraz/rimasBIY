package com.example.rimasbiy.userTable;

/**
 واجهة تحوي دوال لمعالجة وجود رد من جميني او الرد عند حدوث خطا
 */

public interface ResponseCallback {

        /**
         * معالجة جواب الطلب من Gemini
         * @param response جواب الطلب
         */
        public void onResponse(String response);

        /**
         * الرد بحالة وجود خطا
         * @param error
         */
        public void onError(Throwable error);



}

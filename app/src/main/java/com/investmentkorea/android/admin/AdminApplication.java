package com.investmentkorea.android.admin;

import android.app.Application;

import java.util.Calendar;

public class AdminApplication extends Application {
    
    //오늘 날짜
    public static int TODAY_YEAR;
    public static int TODAY_MONTH;
    public static int TODAY_DAY;

    @Override
    public void onCreate() {
        super.onCreate();

        Calendar cal = Calendar.getInstance();
        TODAY_YEAR = cal.get(Calendar.YEAR);
        TODAY_MONTH = cal.get(Calendar.MONTH)+1;
        TODAY_DAY = cal.get(Calendar.DATE);
    }
}

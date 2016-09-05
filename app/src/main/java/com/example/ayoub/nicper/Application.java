package com.example.ayoub.nicper;

import com.onesignal.OneSignal;

/**
 * Created by hr2 on 2016-08-08.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this).init();
    }
}

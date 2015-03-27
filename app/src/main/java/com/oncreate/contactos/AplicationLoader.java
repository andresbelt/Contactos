package com.oncreate.contactos;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by azulandres92 on 26/03/15.
 */
public class AplicationLoader extends Application {

    public static Context applicationContext = null;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = this;
    }
}

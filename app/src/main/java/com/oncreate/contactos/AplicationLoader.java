package com.oncreate.contactos;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.oncreate.contactos.util.GPSTracker;
import com.oncreate.contactos.util.Singleton;

/**
 * Created by azulandres92 on 26/03/15.
 */
public class AplicationLoader extends Application {

    public static Context applicationContext = null;
    private GPSTracker gps;
    private double Latitud,Longitud;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = this;


        gps = new GPSTracker(this);
        gps();
    }


    private void gps() {

        if (gps.getLatitude() != 0.0) {
            Latitud = gps.getLatitude();
            Longitud = gps.getLongitude();

            Singleton.getInstance().setLatitud(Latitud);
            Singleton.getInstance().setLongitud(Longitud);


        } else {

            Singleton.getInstance().setLatitud(0.0);
            Singleton.getInstance().setLongitud(0.0);
            Toast.makeText(this,"Para una mejor ubicación active el gps",Toast.LENGTH_LONG).show();

        }
    }
}

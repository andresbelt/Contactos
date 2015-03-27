package com.oncreate.contactos.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactosDb extends SQLiteOpenHelper {

    private static int version = 1;
    private static String name = "ContactosDB";
    private static CursorFactory factory = null;

    public ContactosDb(Context context) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(this.getClass().toString(), "Creando base de datos");

        db.execSQL("CREATE TABLE CONTACTOS(" +
                " _id INTEGER PRIMARY KEY," +
                " nombre TEXT NOT NULL, " +
                " telefono Var NOT NULL, " +
                " latitud VARCHAR NOT NULL, " +
                " longitud VARCHAR NOT NULL, " +
                " foto TEXT)");



        db.execSQL("CREATE UNIQUE INDEX _id ON CONTACTOS(_id ASC)");

        Log.i(this.getClass().toString(), "Tabla creada");

        Log.i(this.getClass().toString(), "Base de datos creada");

    /*
     * Insertamos datos iniciales
     */
        db.execSQL("INSERT INTO CONTACTOS(_id, nombre,telefono,latitud,longitud,foto) VALUES(1,'Andres Beltran','12345678','0.0','0.0','jeje')");
        db.execSQL("INSERT INTO CONTACTOS(_id, nombre,telefono,latitud,longitud,foto) VALUES(2,'Andres Beltran','12345678','0.0','0.0','jeje')");
        db.execSQL("INSERT INTO CONTACTOS(_id, nombre,telefono,latitud,longitud,foto) VALUES(3,'Andres Beltran','12345678','0.0','0.0','jeje')");
        db.execSQL("INSERT INTO CONTACTOS(_id, nombre,telefono,latitud,longitud,foto) VALUES(4,'Andres Beltran','12345678','0.0','0.0','jeje')");
        db.execSQL("INSERT INTO CONTACTOS(_id, nombre,telefono,latitud,longitud,foto) VALUES(5,'Andres Beltran','12345678','0.0','0.0','jeje')");
        db.execSQL("INSERT INTO CONTACTOS(_id, nombre,telefono,latitud,longitud,foto) VALUES(6,'Andres Beltran','12345678','0.0','0.0','jeje')");

        Log.i(this.getClass().toString(), "Datos iniciales HIPOTECA insertados");

        Log.i(this.getClass().toString(), "Base de datos creada");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}


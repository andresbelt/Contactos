package com.oncreate.contactos.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


public class ContactosDbHelper {

	//
	// Definimos constante con el nombre de la tabla
	//
	public static final String C_TABLA = "CONTACTOS" ;
	
    //
    // Definimos constantes con el nombre de las columnas de la tabla
    //
    public static final String C_COLUMNA_ID	= "_id";
    public static final String C_COLUMNA_NOMBRE = "nombre";
    public static final String C_COLUMNA_TELEFONO = "telefono";
    public static final String C_COLUMNA_LATITUD = "latitud";
    public static final String C_COLUMNA_LONGITUD = "longitud";
    public static final String C_COLUMNA_FOTO = "foto";


    private Context contexto;
    private ContactosDb dbHelper;
    private SQLiteDatabase db;

    //
    // Definimos lista de columnas de la tabla para utilizarla en las consultas a la base de datos
    //
    private String[] columnas = new String[]{
            C_COLUMNA_ID,
            C_COLUMNA_NOMBRE,
            C_COLUMNA_TELEFONO,
            C_COLUMNA_LATITUD,
            C_COLUMNA_LONGITUD,
            C_COLUMNA_TELEFONO,
            C_COLUMNA_FOTO,
            } ;

	public ContactosDbHelper(Context context)
	{
		this.contexto = context;
	}

	public ContactosDbHelper abrir() throws SQLException
	{

		dbHelper = new ContactosDb(contexto);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void cerrar()
	{
		dbHelper.close();
	}

	
    /**
     * Devuelve cursor con todos los registros y columnas de la tabla
     */
    public Cursor getCursor(String filtro) throws SQLException {

        if (db == null)
            abrir();

        Cursor c = db.query(true, C_TABLA, columnas, filtro, null, null, null, null, null);

        return c;
    }
	
	/**
	 * Devuelve cursor con todos las columnas del registro
	 */
	public Cursor getRegistro(long id) throws SQLException
	{
        if (db == null)
            abrir();

        Cursor c = db.query( true, C_TABLA, columnas, C_COLUMNA_ID + "=" + id, null, null, null, null, null);
		
		//Nos movemos al primer registro de la consulta
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	
	/**
	 * Inserta los valores en un registro de la tabla
	 */
	public long insert(ContentValues reg)
	{
		if (db == null)
			abrir();
		
		return db.insert(C_TABLA, null, reg);
	}
	
	/**
	 * Eliminar el registro con el identificador indicado
	 */
	public long delete(long id)
	{
		if (db == null)
			abrir();
		
		return db.delete(C_TABLA, "_id=" + id, null);
	}
	
    /**
     * Modificar el registro
     */
    public long update(ContentValues reg)
    {
        long result = 0;

        if (db == null)
            abrir();

        if (reg.containsKey(C_COLUMNA_ID))
        {
            //
            // Obtenemos el id y lo borramos de los valores
            //
            long id = reg.getAsLong(C_COLUMNA_ID);

            reg.remove(C_COLUMNA_ID);

            //
            // Actualizamos el registro con el identificador que hemos extraido
            //
            result = db.update(C_TABLA, reg, "_id=" + id, null);
        }
        return result;
    }

    /**
     * Comprueba si existe el registro
     */
    public boolean exists(long id) throws SQLException
    {
        boolean exists ;

        if (db == null)
            abrir();

        Cursor c = db.query( true, C_TABLA, columnas, C_COLUMNA_ID + "=" + id, null, null, null, null, null);

        exists = (c.getCount() > 0);

        c.close();

        return exists;
    }

    public ArrayList<Contactos> getContactos()
    {
        ArrayList<Contactos> hipotecas = new ArrayList<Contactos>();

        if (db == null)
            abrir();

        Cursor c = db.query(true, C_TABLA, columnas, null, null, null, null, null, null);

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
        {
            hipotecas.add(Contactos.cursorToContacto(contexto, c));
        }

        c.close();

        return hipotecas;
    }



}
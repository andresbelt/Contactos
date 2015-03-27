package com.oncreate.contactos;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class Contactos {

    private Context context;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    private Long id;
    private String nombre;
    private String telefono;
    private String latitud;
    private String longitud;
    private String foto;

    public Contactos(Context context)
    {
        this.context = context;
    }

    public Contactos(Context context, Long id, String nombre, String telefono, String latitud, String longitud, String foto) {
        this.context = context;
        this.id = id;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.foto = foto;
        this.telefono = telefono;

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }



    public static Contactos find(Context context, long id)
    {
        ContactosDbHelper dbAdapter = new ContactosDbHelper(context);

        Cursor c = dbAdapter.getRegistro(id);

        Contactos contacto = Contactos.cursorToContacto(context, c);

        c.close();

        return contacto;
    }

    public static Contactos cursorToContacto(Context context, Cursor c)
    {
        Contactos contacto = null;

        if (c != null)
        {
            contacto = new Contactos(context);

            contacto.setId(c.getLong(c.getColumnIndex(ContactosDbHelper.C_COLUMNA_ID)));
            contacto.setNombre(c.getString(c.getColumnIndex(ContactosDbHelper.C_COLUMNA_NOMBRE)));
            contacto.setTelefono(c.getString(c.getColumnIndex(ContactosDbHelper.C_COLUMNA_TELEFONO)));
            contacto.setLatitud(c.getString(c.getColumnIndex(ContactosDbHelper.C_COLUMNA_LATITUD)));
            contacto.setLongitud(c.getString(c.getColumnIndex(ContactosDbHelper.C_COLUMNA_LONGITUD)));
            contacto.setFoto(c.getString(c.getColumnIndex(ContactosDbHelper.C_COLUMNA_FOTO)));
        }

        return contacto ;
    }

    private ContentValues toContentValues()
    {
        ContentValues reg = new ContentValues();

        reg.put(ContactosDbHelper.C_COLUMNA_ID, this.getId());
        reg.put(ContactosDbHelper.C_COLUMNA_NOMBRE, this.getNombre());
        reg.put(ContactosDbHelper.C_COLUMNA_TELEFONO, this.getTelefono());
        reg.put(ContactosDbHelper.C_COLUMNA_LATITUD, this.getLatitud());
        reg.put(ContactosDbHelper.C_COLUMNA_LONGITUD, this.getLongitud());
        reg.put(ContactosDbHelper.C_COLUMNA_FOTO, this.getFoto());

        return reg;
    }

    public long save()
    {
        ContactosDbHelper dbAdapter = new ContactosDbHelper(this.getContext());

        // comprobamos si estamos insertando o actualizando según esté o no relleno el identificador
        if ((this.getId() == null) || (!dbAdapter.exists(this.getId())))
        {
            long nuevoId = dbAdapter.insert(this.toContentValues());

            if (nuevoId != -1)
            {
                this.setId(nuevoId);
            }
        }
        else
        {
            dbAdapter.update(this.toContentValues());
        }

        return this.getId();
    }

    public long delete()
    {
        // borramos el registro
        ContactosDbHelper dbAdapter = new ContactosDbHelper(this.getContext());

        return dbAdapter.delete(this.getId());
    }
}

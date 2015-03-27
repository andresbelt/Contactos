package com.oncreate.contactos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.oncreate.contactos.Database.Contactos;
import com.oncreate.contactos.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;


public class ContactosAdapter extends ArrayAdapter<Contactos> {

    private int i;
    private static Context context;
    private boolean b;

    private static class ViewHolder {
        TextView textNombre, telefono;
        ImageView foto;


    }

    public ContactosAdapter(Context context, ArrayList<Contactos> hipotecas, boolean b) {
        super(context, android.R.layout.simple_dropdown_item_1line, hipotecas);

        this.context = context;
        i = getImagenDrawable("card");
        this.b = b;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Contactos contacto = getItem(position);


        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            if (b) {

                convertView = inflater.inflate(R.layout.item_contacto, parent, false);
                viewHolder.textNombre = (TextView) convertView.findViewById(R.id.textNombre);
                viewHolder.telefono = (TextView) convertView.findViewById(R.id.textTelefono);
            } else {
                convertView = inflater.inflate(R.layout.item_contacto_grid, parent, false);
                viewHolder.textNombre = (TextView) convertView.findViewById(R.id.textNombre);
                viewHolder.telefono = (TextView) convertView.findViewById(R.id.textTelefono);
                viewHolder.foto = (ImageView) convertView.findViewById(R.id.imageFoto);


            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textNombre.setText(contacto.getNombre());
        viewHolder.telefono.setText(contacto.getTelefono());

        if (!b) {
            File imgFile = new File(contacto.getFoto());

            Picasso.with(context).load(imgFile).error(i).into(viewHolder.foto);
        }

        return convertView;

    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }


    public static int getImagenDrawable(String nombreImagen) {
        return context.getResources().getIdentifier(nombreImagen, "drawable",
                context.getPackageName());
    }
}

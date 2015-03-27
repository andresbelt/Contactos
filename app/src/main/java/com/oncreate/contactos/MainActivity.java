package com.oncreate.contactos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, View.OnCreateContextMenuListener {


    private ContactosDbHelper dbAdapter;
    private ContactosAdapter contactosAdapter;
    private ListView lista;
    private GridView gridView;
    private static boolean b;

    public static final String C_MODO = "modo";

    public static final int VISUALIZAR = 1;
    public static final int CREAR = 2;
    public static final int EDITAR = 3;
    public static final int ELIMINAR = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        dbAdapter = new ContactosDbHelper(this);
        dbAdapter.abrir();

        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main);

            b = true;

            lista = (ListView) findViewById(R.id.lista);
            lista.setOnCreateContextMenuListener(this);
            lista.setOnItemClickListener(this);
            consultar(b);

        } else {
            setContentView(R.layout.activity_main);
            b = false;
            gridView = (GridView) findViewById(R.id.gridView);
            gridView.setOnCreateContextMenuListener(this);
            gridView.setOnItemClickListener(this);
            consultar(b);

        }


    }


    private void consultar(boolean b) {


        contactosAdapter = new ContactosAdapter(this, dbAdapter.getContactos(), b);

        if (b)
            lista.setAdapter(contactosAdapter);
        else
            gridView.setAdapter(contactosAdapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        verContacto(id);

    }


    private void verContacto(long id) {


        Intent i = new Intent(this, DetalleActivity.class);
        i.putExtra(C_MODO, VISUALIZAR);
        i.putExtra(ContactosDbHelper.C_COLUMNA_ID, id);

        startActivityForResult(i, VISUALIZAR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.clear();

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nuevo:
                Intent i = new Intent(this, DetalleActivity.class);
                i.putExtra(C_MODO, CREAR);
                startActivityForResult(i, CREAR);
                break;

        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //
        // Nos aseguramos que es la petición que hemos realizado
        //
        switch (requestCode) {
            case CREAR:
                if (resultCode == RESULT_OK)
                    consultar(b);
                break;

            case VISUALIZAR:
                if (resultCode == RESULT_OK)
                    consultar(b);

                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle(contactosAdapter.getItem(((AdapterView.AdapterContextMenuInfo) menuInfo).position).getNombre());
        menu.add(Menu.NONE, VISUALIZAR, Menu.NONE, R.string.menu_visualizar);
        menu.add(Menu.NONE, EDITAR, Menu.NONE, R.string.menu_editar);
        menu.add(Menu.NONE, ELIMINAR, Menu.NONE, R.string.menu_eliminar);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Intent i;

        switch (item.getItemId()) {
            case ELIMINAR:
                borrar(info.id);
                return true;

            case VISUALIZAR:
                verContacto(info.id);
                return true;

            case EDITAR:
                i = new Intent(this, DetalleActivity.class);
                i.putExtra(C_MODO, EDITAR);
                i.putExtra(ContactosDbHelper.C_COLUMNA_ID, info.id);

                startActivityForResult(i, EDITAR);
                return true;
        }
        return super.onContextItemSelected(item);
    }


    private void borrar(final long id) {
        /*
		 * Borramos el registro y refrescamos la lista
		 */
        AlertDialog.Builder dialogEliminar = new AlertDialog.Builder(this);

        dialogEliminar.setIcon(android.R.drawable.ic_dialog_alert);
        dialogEliminar.setTitle(getResources().getString(R.string.contacto_eliminar_titulo));
        dialogEliminar.setMessage(getResources().getString(R.string.contacto_eliminar_mensaje));
        dialogEliminar.setCancelable(false);



        dialogEliminar.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int boton) {
                dbAdapter.delete(id);
                Toast.makeText(MainActivity.this, R.string.contacto_eliminar_confirmacion, Toast.LENGTH_SHORT).show();
                consultar(b);
            }
        });

        dialogEliminar.setNegativeButton(android.R.string.no, null);

        dialogEliminar.show();
    }


}

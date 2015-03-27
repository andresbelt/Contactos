package com.oncreate.contactos.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oncreate.contactos.util.AndroidUtils;
import com.oncreate.contactos.AplicationLoader;
import com.oncreate.contactos.Database.Contactos;
import com.oncreate.contactos.Database.ContactosDbHelper;
import com.oncreate.contactos.R;
import com.oncreate.contactos.util.Singleton;

import java.io.File;
import java.io.FileOutputStream;


public class DetalleActivity extends ActionBarActivity implements View.OnClickListener {


    private static String Ruta;
    //
    // Modo del formulario
    //
    private int modo;

private double latitud,longitud;
    public static String currentPicturePath;

    //
    // Identificador del registro que se edita cuando la opción es MODIFICAR
    //
    private long id;
    private Contactos contacto = new Contactos(this);

    //
    // Elementos de la vista
    //
    private static ImageView imagen;
    private EditText nombre;
    private EditText telefono;
    private  GoogleMap map;
    private Button boton_guardar;
    private Button boton_cancelar;
    private Button boton_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();

        if (extra == null) return;

        nombre = (EditText) findViewById(R.id.textNombre);
        telefono = (EditText) findViewById(R.id.textTelefono);

        boton_guardar = (Button) findViewById(R.id.boton_guardar);
        boton_cancelar = (Button) findViewById(R.id.boton_cancelar);
        boton_image = (Button) findViewById(R.id.addImagen);
        imagen = (ImageView) findViewById(R.id.imageView);
        if (extra.containsKey(ContactosDbHelper.C_COLUMNA_ID)) {
            id = extra.getLong(ContactosDbHelper.C_COLUMNA_ID);
            consultar(id);
        }


        map  = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        establecerModo(extra.getInt(MainActivity.C_MODO));


        boton_guardar.setOnClickListener(this);

        boton_cancelar.setOnClickListener(this);
        boton_image.setOnClickListener(this);
        fillMap();

    }

    private void establecerModo(int m) {
        this.modo = m;

        if (modo == MainActivity.VISUALIZAR) {
            setTitle(nombre.getText().toString());
           setEdicion(false);
            mapa(false);
        } else if (modo == MainActivity.CREAR) {
            setTitle(R.string.Contacto_crear_titulo);
           setEdicion(true);
            mapa(true);
        } else if (modo == MainActivity.EDITAR) {
           setTitle(R.string.Contacto_editar_titulo);
           setEdicion(true);
            mapa(false);
        }
    }


    private void fillMap() {
        if (map != null) {

            try {

                double Latitud = this.latitud;
                double Longitud = this.longitud;

                LatLng latLng = new LatLng(Latitud, Longitud);
                MarkerOptions mk = new MarkerOptions()
                        // .icon(BitmapDescriptorFactory.fromBitmap(atmPic))
                        .draggable(false).position(latLng);
                map.addMarker(mk);
                LatLng currentLoc = new LatLng(Latitud, Longitud);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc,
                        16));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void consultar(long id) {
        //
        // Consultamos la hipoteca por el identificador
        //
        contacto = Contactos.find(this, id);

        nombre.setText(contacto.getNombre());
        telefono.setText(contacto.getTelefono());

    }

    private void mapa(boolean o){

   if(o){

       latitud = Singleton.getInstance().getLatitud();
       longitud = Singleton.getInstance().getLongitud();
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);

       map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

           @Override
           public void onMarkerDragStart(Marker marker) {
           }

           @Override
           public void onMarkerDragEnd(Marker marker) {
               Log.d("error", "latitude : "+ marker.getPosition().latitude);
               marker.setSnippet(String.valueOf(marker.getPosition().latitude));
               map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

           }

           @Override
           public void onMarkerDrag(Marker marker) {
           }

       });}
   else{

       latitud = Double.parseDouble(contacto.getLatitud());
       longitud =  Double.parseDouble(contacto.getLongitud());
       map.getUiSettings().setAllGesturesEnabled(false);


   }
    }


    private void setEdicion(boolean opcion) {
        nombre.setEnabled(opcion);
        telefono.setEnabled(opcion);


        // Controlamos visibilidad de botonera
        LinearLayout v = (LinearLayout) findViewById(R.id.botonera);

        if (opcion) {
            boton_image.setVisibility(View.VISIBLE);
            v.setVisibility(View.VISIBLE);
        } else {
            boton_image.setVisibility(View.GONE);
            v.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.clear();

        if (modo == MainActivity.VISUALIZAR)
            getMenuInflater().inflate(R.menu.menu_detalle, menu);

        else
            getMenuInflater().inflate(R.menu.menu_crear, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guardar:
                guardar();
                break;
            case R.id.cancelar:
                setResult(RESULT_CANCELED, null);
                finish();
                break;
            case R.id.editar:
                establecerModo(MainActivity.EDITAR);

                break;
            case R.id.borrar:
                borrar(id);
                break;
        }
        return true;
    }


    private void borrar(final long id) {
        /*
         * Borramos el registro con confirmación
		 */
        AlertDialog.Builder dialogEliminar = new AlertDialog.Builder(this);

        dialogEliminar.setIcon(android.R.drawable.ic_dialog_alert);
        dialogEliminar.setTitle(getResources().getString(R.string.contacto_eliminar_titulo));
        dialogEliminar.setMessage(getResources().getString(R.string.contacto_eliminar_mensaje));
        dialogEliminar.setCancelable(false);

        dialogEliminar.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int boton) {
                contacto.delete();
                Toast.makeText(DetalleActivity.this, R.string.contacto_eliminar_confirmacion, Toast.LENGTH_SHORT).show();
                /*
                 * Devolvemos el control
				 */
                setResult(RESULT_OK);
                finish();
            }
        });

        dialogEliminar.setNegativeButton(android.R.string.no, null);

        dialogEliminar.show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.boton_guardar:
                guardar();
                break;
            case R.id.boton_cancelar:
                cancelar();
                break;
            case R.id.addImagen:
                addimagen();
                break;

        }
    }

    private void addimagen() {
        mostrarDialog(this, this.getResources().getString(R.string.seleccion_fotos), " ", this.getResources().getString(R.string.seleccion_camara), this.getResources().getString(R.string.seleccion_galeria)).show();

    }

    public static AlertDialog mostrarDialog(final Activity activity, String titulo, String mensaje, String mensajeAceptar, String mensajeCancelar) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.ThemeTransparent);
        alertDialogBuilder.setTitle(titulo)
                .setCancelable(true)
                .setPositiveButton(mensajeAceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        openCamera(activity);

                    }
                })
                .setNegativeButton(mensajeCancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        openGallery(activity);

                    }
                });


        return alertDialogBuilder.create();

    }



    public static void openGallery(Activity activity) {
        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/*");
            activity.startActivityForResult(photoPickerIntent, 14);
        } catch (Exception e) {
        }
    }

    public static void openCamera(Activity activity) {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File image = AndroidUtils.generatePicture();

            if (image != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                currentPicturePath = image.getAbsolutePath();
            }
            activity.startActivityForResult(takePictureIntent, 13);
        } catch (Exception e) {
            //	FileLog.e("tmessages", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 13) {
                AndroidUtils.addMediaToGallery(currentPicturePath);
                Bitmap b = AndroidUtils.startCrop(currentPicturePath, null);
                currentPicturePath = null;
                try {

                    saveImage(b);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (requestCode == 14) {
                if (data == null || data.getData() == null) {
                    return;
                }
                Bitmap b = AndroidUtils.startCrop(null, data.getData());
                try {

                    saveImage(b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void saveImage(Bitmap myBitmap) throws Exception {

        File myDir = new File(Environment.getExternalStorageDirectory(), AplicationLoader.applicationContext.getPackageName());
        if (!myDir.exists()) {
            myDir.mkdir();
        }

       String a  = String.valueOf(Math.random());
        String nombreimagen = a + ".jpg";
        File file = new File(myDir, nombreimagen);
        //Log.e("error", uid);
        if (file.exists())
            file.delete();

        FileOutputStream stream = new FileOutputStream(file);
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        if (file != null) {

            Ruta = file.getAbsolutePath();
            imagen.setImageBitmap(myBitmap);
        }
    }



    private void guardar() {

        String mNombre = nombre.getText().toString();
       String mTelefono = telefono.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(mNombre)) {
            nombre.setError(this.getResources().getString(R.string.nombre_requerido));
            focusView = nombre;
            cancel = true;
        }

        if (TextUtils.isEmpty(mTelefono)) {
            telefono.setError(this.getResources().getString(R.string.tel_requerido));
            focusView = nombre;
            cancel = true;
        }


        if (TextUtils.isEmpty(Ruta)) {
           Toast.makeText(this, this.getResources().getString(R.string.imagen_requerida),Toast.LENGTH_LONG).show();
            cancel = true;
        }

        if (cancel) {
            if(focusView != null)
            focusView.requestFocus();
        } else {
            contacto.setNombre(mNombre);
            contacto.setTelefono(mTelefono);
            contacto.setFoto(Ruta);
            contacto.setLatitud(String.valueOf(latitud));
            contacto.setLongitud(String.valueOf(longitud));
            contacto.save();

            if (modo == MainActivity.CREAR) {
                // Toast.makeText(this, R.string.hipoteca_crear_confirmacion, Toast.LENGTH_SHORT).show();
            } else if (modo == MainActivity.EDITAR) {
                // Toast.makeText(this, R.string.hipoteca_editar_confirmacion, Toast.LENGTH_SHORT).show();
            }

            setResult(RESULT_OK);
            finish();
        }
    }

    private void cancelar() {

        if (modo == MainActivity.CREAR) {
            setResult(RESULT_CANCELED, null);
            finish();

        } else {
            establecerModo(MainActivity.VISUALIZAR);
            consultar(id);
        }

    }
}

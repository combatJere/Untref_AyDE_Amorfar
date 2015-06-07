package com.altosoftuntref.amorfar;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import Persitencia.BaseDeDatosContract;
import Persitencia.DAOs.DAOs.Implementacion.MenuesDAOImpl;
import Persitencia.DAOs.MenuesDAO;
import adapter.PlatosCursorAdapter;
import dialogs.NombrePlatoDialogFragment;
import inversiondecontrol.ServiceLocator;


public class SeleccionPlatoActivity extends ActionBarActivity implements NombrePlatoDialogFragment.NuevoPlatoDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_plato);
        this.instanciarGridViewPlatos();
    }

    /**
     * Instancia el GridView que muestra los platos previamente creados que pueden elegirse.
     */
    private void instanciarGridViewPlatos(){
        Cursor cursor = ServiceLocator.getInstance().getMenuesDao(getBaseContext()).getAllPlatosGuardadosCursor();
//        cursor.moveToFirst();
//        String s = cursor.getString(cursor.getColumnIndex(BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE));
//        Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
        GridView gridview = (GridView) findViewById(R.id.gridView_seleccionPlato_platos);
        PlatosCursorAdapter platosCursorAdapter = new PlatosCursorAdapter(getBaseContext(), cursor, 0);
        gridview.setAdapter(platosCursorAdapter);
    }

    /**
     * Muestra el Dialo CrearNuevoPlatoDialog, que permite crear un nuevo plato
     * y aregarlo a la lista de platos.
     */
    public void showCrearNuevoPlatoDialog (View view){
        NombrePlatoDialogFragment nombrePlatoDialogFragment = new NombrePlatoDialogFragment();
        nombrePlatoDialogFragment.show(getFragmentManager(), "nombrePlatoDialog");
    }

    /**
     * Es ejecutado por el dialog CrearNuevoPlatoDialog en el momento en que este es confirmado.
     * Recive el nombre del plato a Guardar en la BDD
     * @param nombrePlato
     */
    @Override
    public void onConfirmarClick(String nombrePlato) {
        this.guardarNuevoPlato(nombrePlato);
    }

    /**
     * Si no existe un plato con este nombre en la BDD, lo guarda como un nuevo plato
     * con el nombre recivido.
     * @param nombrePlato: nombre de un plato que se quiere guardar.
     */
    public void guardarNuevoPlato(String nombrePlato){
        boolean guardadoExitoso = false;
        MenuesDAO menuesDao = ServiceLocator.getInstance().getMenuesDao(getBaseContext());

        if(nombrePlato.length() < 4){
            Toast.makeText(getBaseContext(), R.string.nombre_plato_invalido, Toast.LENGTH_LONG).show();
        }else {
            if (menuesDao.existePlato(nombrePlato)) {
                Toast.makeText(getBaseContext(), R.string.plato_existente, Toast.LENGTH_LONG).show();
            } else {
                guardadoExitoso = menuesDao.guardarPlato(nombrePlato);
                if (!guardadoExitoso) {
                    Toast.makeText(getBaseContext(), R.string.error_al_guardar_plato, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

//    private boolean nombreYaExiste(String nombrePlatoAAnalizar){
//        boolean nombreExiste = false;
//        Cursor cursor = MenuesDAOImpl.getInstance(getBaseContext()).getAllNombrePlatosCursor();
//        String nombrePlatoActual;
//
//        while(cursor.moveToNext() && !nombreExiste) {
//            nombrePlatoActual = cursor.getString(cursor.getColumnIndex(BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE));
//            nombreExiste = nombrePlatoAAnalizar.equals(nombrePlatoActual);
//        }
//        cursor.close();
//
//        return nombreExiste;
//
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_seleccion_plato, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}

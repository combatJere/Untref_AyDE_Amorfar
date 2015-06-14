package com.altosoftuntref.amorfar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.Set;

import Persitencia.DAOs.MenuesDAO;
import Utilidades.TransformadorIntSetArray;
import adapter.PlatosCursorAdapter;
import dialogs.NombrePlatoDialogFragment;
import inversiondecontrol.ServiceLocator;


public class SeleccionPlatoActivity extends Activity implements NombrePlatoDialogFragment.NuevoPlatoDialogListener {

    public final static String SAVED_SET_ID_PLATOS = "amorfar.SeleccionPlatoActivity.SET_ID_PLATOS";
    public final static String SAVED_ID_PLATO_A_INTERCAMBIAR = "amorfar.SeleccionPlatoActivity.ID_PLATO_A_INTERCAMBIAR";

    public final static String EXTRA_RETURNED_IDPLATOS_ACTUALIZADOS ="amorfar.SeleccionPlatoActivity.EXTRA_RETURNED_IDPLATOS_ACTUALIZADOS";
    private PlatosCursorAdapter platosCursorAdapter;
    private Set<Integer> idPlatosElejidos;
    private int idPlatoAintercambiar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            //valores salvados, si se esta recreando la actividad.
            this.recuperarDatosSalvados(savedInstanceState);
        } else {
            //Default values, si inicia por primera vez.
            this.instanciarConNuevosValores();
        }

        setContentView(R.layout.activity_seleccion_plato);
        this.instanciarGridViewPlatos();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(SAVED_ID_PLATO_A_INTERCAMBIAR, idPlatoAintercambiar);
        savedInstanceState.putIntArray(SAVED_SET_ID_PLATOS, TransformadorIntSetArray.getInstance().setIntAArrayInt(idPlatosElejidos));
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Recupera los valores de una instancia anterior.
     * @param savedInstanceState
     */
    private void recuperarDatosSalvados(Bundle savedInstanceState){
        idPlatoAintercambiar = savedInstanceState.getInt(SAVED_ID_PLATO_A_INTERCAMBIAR);
        int[] platosGuardadosArray = savedInstanceState.getIntArray(SAVED_SET_ID_PLATOS);
        idPlatosElejidos = TransformadorIntSetArray.getInstance().arrayIntASetInt(platosGuardadosArray);
    }


    /**
     * Recive el conjunto de platos elegidos actuales.
     * Si la actividad recien inicia, se obtienen los valores recividos de CrearMenuActivity.
     */
    private void instanciarConNuevosValores(){
        idPlatoAintercambiar = getIntent().getIntExtra(CrearMenuActivity.EXTRA_ID_PLATO_A_INTERCAMBIAR, 0);
        int[] idPlatosElejidosArray = getIntent().getIntArrayExtra(CrearMenuActivity.EXTRA_ID_PLATOS_ACTUALES_A_INTERC);
        idPlatosElejidos = TransformadorIntSetArray.getInstance().arrayIntASetInt(idPlatosElejidosArray);
    }


    /**
     * Instancia el GridView que muestra los platos previamente creados que pueden elegirse.
     * Solo muestra los platos que no estan elegidos.
     */
    private void instanciarGridViewPlatos(){
        Cursor cursorAllPlatos = ServiceLocator.getInstance().getMenuesDao(getBaseContext()).
                getPlatosGuardadosExcepto(idPlatosElejidos);

        GridView gridViewPlatos = (GridView) findViewById(R.id.gridView_seleccionPlato_platos);
        platosCursorAdapter = new PlatosCursorAdapter(getBaseContext(), cursorAllPlatos, 0);
        gridViewPlatos.setAdapter(platosCursorAdapter);
        gridViewPlatos.setOnItemClickListener(onPlatoClick);
    }


    /**
     * actualiza el Gridview.
     */
    private void actualizarGridViewPlatos(){
        Cursor cursorPlatosActualizados = ServiceLocator.getInstance().getMenuesDao(getBaseContext()).
                getPlatosGuardadosExcepto(idPlatosElejidos);
        platosCursorAdapter.changeCursor(cursorPlatosActualizados);
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
    public void onConfirmarDialogClick(String nombrePlato) {
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
                if (guardadoExitoso) {
                    this.actualizarGridViewPlatos();
                }else{
                    Toast.makeText(getBaseContext(), R.string.error_al_guardar_plato, Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    /**
     * Quita el plato que se reemplazo del conjunto de platos elegidos y agrega el nuevo plato elegido
     * y cierra la actividad devolviendo el nuevo conjunto de platos.
     */
    private AdapterView.OnItemClickListener onPlatoClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

            int idPlatoElejido = (int) id; //(int)gridViewPlatosMultiCheck.getAdapter().getItemId(position);

            Iterator<Integer> iterator = idPlatosElejidos.iterator();

            Integer idAPlatoARemover = null;
            while(iterator.hasNext() && idAPlatoARemover == null){
                Integer idActual = iterator.next();
                if (idActual.equals(idPlatoAintercambiar)){
                    idAPlatoARemover = idActual;
                }
            }
            idPlatosElejidos.remove(idAPlatoARemover);
            idPlatosElejidos.add(idPlatoElejido);

            Intent intent = new Intent();
            int[] idPlatosElejidosArray = TransformadorIntSetArray.getInstance().setIntAArrayInt(idPlatosElejidos);
            intent.putExtra(EXTRA_RETURNED_IDPLATOS_ACTUALIZADOS, idPlatosElejidosArray);
            setResult(RESULT_OK, intent);
            finish();
        }
    };


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

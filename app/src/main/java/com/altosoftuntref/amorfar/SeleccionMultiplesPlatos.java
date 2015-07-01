package com.altosoftuntref.amorfar;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import Persitencia.DAOs.MenuesDAO;
import Utilidades.TransformadorIntSetArray;
import adapter.PlatosMultipleChoiceAdapter;
import dialogs.NombrePlatoDialogFragment;
import inversiondecontrol.ServiceLocator;
import layouts.customs.GridViewItem;


public class SeleccionMultiplesPlatos extends Activity implements NombrePlatoDialogFragment.NuevoPlatoDialogListener{

    public final static String EXTRA_RETURNED_IDPLATOS = "com.altosoftuntref.amorfar.multiplesPlatos.RETURN_IDPLATOS";
    private final static String SAVED_CANTIDAD_PLATOS = "com.altosoftuntref.amorfar.multiplesPlatos.CANTIDAD_PLATOS";
    private final static String SAVED_SET_PLATOS_ELEJIDOS = "com.altosoftuntref.amorfar.multiplesPlatos.SET_PLATOS_ELEJIDOS";

    private  int cantPlatosRestantes;
    private Set<Integer> idPlatosElejidos;
    private TextView textViewCantidadPlatosRestantes;
    private GridView gridViewPlatosMultiCheck;
    private PlatosMultipleChoiceAdapter platosCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check whether we are recreating a previously destroyed instance.
        if (savedInstanceState != null) {
            //valores salvados, si se esta recreando la actividad.
            cantPlatosRestantes = savedInstanceState.getInt(SAVED_CANTIDAD_PLATOS);
            int[] platosGuardadosArray = savedInstanceState.getIntArray(SAVED_SET_PLATOS_ELEJIDOS);
            idPlatosElejidos = TransformadorIntSetArray.getInstance().arrayIntASetInt(platosGuardadosArray);
        } else {
            //Default values, si inicia por primera vez.
            cantPlatosRestantes = getIntent().getIntExtra(CrearMenuActivity.EXTRA_CANTIDAD_PLATOS, 0);
            idPlatosElejidos = new HashSet<Integer>();
        }

        setContentView(R.layout.activity_seleccion_multiples_platos);
//        cantPlatosRestantes = getIntent().getIntExtra(CrearMenuActivity.EXTRA_CANTIDAD_PLATOS, 0);

        this.instanciarGridViewSeleccionMultiple();
        this.instanciarTextViewPlatosRestantes();
        this.actualizarEstadoIcono();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(SAVED_CANTIDAD_PLATOS, cantPlatosRestantes);
        savedInstanceState.putIntArray(SAVED_SET_PLATOS_ELEJIDOS, TransformadorIntSetArray.getInstance().setIntAArrayInt(idPlatosElejidos));
        super.onSaveInstanceState(savedInstanceState);
    }


    private void instanciarTextViewPlatosRestantes(){
        textViewCantidadPlatosRestantes = (TextView) findViewById(R.id.textView_seleccionMultiplesPlatos_CANTplatosRestantes);
        textViewCantidadPlatosRestantes.setText(String.valueOf(cantPlatosRestantes));
    }


    private void instanciarGridViewSeleccionMultiple(){
        Cursor cursorAllPlatos = ServiceLocator.getInstance().getMenuesDao(getBaseContext()).getAllPlatosGuardadosCursor();
        gridViewPlatosMultiCheck = (GridView) findViewById(R.id.gridView_seleccionMultiplesPlatos_platos);
        platosCursorAdapter = new PlatosMultipleChoiceAdapter(getBaseContext(), cursorAllPlatos, 0, idPlatosElejidos);
        gridViewPlatosMultiCheck.setAdapter(platosCursorAdapter);
        gridViewPlatosMultiCheck.setOnItemClickListener(onPlatoClick);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridViewPlatosMultiCheck.setNumColumns(4);
        }
    }

    /**
     * Se ejecuta cada vez que se toca en algun item del gridview.
     * Agrega (o quita) el plato del conjunto de platos elejidos, y resalta el elemento en el gridview.
     */
    private AdapterView.OnItemClickListener onPlatoClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

            int idPlatoElejido = (int) id; //(int)gridViewPlatosMultiCheck.getAdapter().getItemId(position);
            GridViewItem itemElegido = (GridViewItem) view;
            FrameLayout centroBoton = (FrameLayout) itemElegido.findViewById(R.id.centro_boton);

            if(!idPlatosElejidos.contains(idPlatoElejido)){

                if (cantPlatosRestantes > 0) {

                    itemElegido.setBackgroundColor(getResources().getColor(R.color.sombra_gridViewElement));
                    centroBoton.setBackgroundColor(getResources().getColor(R.color.gridViewItemCentro_background_checked));
                    idPlatosElejidos.add(idPlatoElejido);
                    cantPlatosRestantes--;
                    textViewCantidadPlatosRestantes.setText(String.valueOf(cantPlatosRestantes));

                }else{
                    Toast.makeText(getApplicationContext(), "Ya se eligiron todos los platos", Toast.LENGTH_SHORT).show();
                }

            }else{
                itemElegido.setBackgroundColor(Color.TRANSPARENT);
                centroBoton.setBackgroundColor(getResources().getColor(R.color.gridViewItemCentro_background));
                idPlatosElejidos.remove(idPlatoElejido);
                cantPlatosRestantes++;
                textViewCantidadPlatosRestantes.setText(String.valueOf(cantPlatosRestantes));
            }

            actualizarEstadoIcono();
        }
    };


    private void actualizarEstadoIcono(){
        ImageView botonContinuarImageView = (ImageView)findViewById(R.id.imageView_seleccionMultiplesPlatos_continuar);
        if(cantPlatosRestantes == 0){
            botonContinuarImageView.clearColorFilter();
        }else{
            botonContinuarImageView.setColorFilter(R.color.floating_action_button, PorterDuff.Mode.MULTIPLY);
        }
    }


    /**
     * OnCLick
     * Si la cantidad de platos elejidos es la correcta, cierra la actividad y devuelve los id de
     * los platos elejidos.
     * @param view
     */
    public void devolverPlatosElejidos(View view){
        if(cantPlatosRestantes == 0) {
            Intent intent = new Intent();
            int[] idPlatosElejidosArray = TransformadorIntSetArray.getInstance().setIntAArrayInt(idPlatosElejidos);
            intent.putExtra(EXTRA_RETURNED_IDPLATOS, idPlatosElejidosArray);
            setResult(RESULT_OK, intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Faltan elejir " +cantPlatosRestantes +" platos.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Para que la actividad padre, CrearMenu, sepa que no fue cancelado y por lo tanto nunca
     * se envio el menu.
     */
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }


    /**
     * Refresca el gridview
     */
    private void actualizarGridViewPlatos(){
        Cursor cursorPlatosActualizados = ServiceLocator.getInstance().getMenuesDao(getBaseContext()).getAllPlatosGuardadosCursor();
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
            Toast.makeText(getBaseContext(), R.string.nombre_plato_invalido, Toast.LENGTH_SHORT).show();
        }else {
            if (menuesDao.existePlato(nombrePlato)) {
                Toast.makeText(getBaseContext(), R.string.plato_existente, Toast.LENGTH_SHORT).show();
            } else {

                guardadoExitoso = menuesDao.guardarPlato(nombrePlato);
                if (guardadoExitoso) {
                    this.actualizarGridViewPlatos();
                }else{
                    Toast.makeText(getBaseContext(), R.string.error_al_guardar_plato, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_seleccion_multiples_platos, menu);
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

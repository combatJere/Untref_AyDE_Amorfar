package com.altosoftuntref.amorfar;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import Configuraciones.Configuraciones;
import Utilidades.TransformadorFechasSingleton;
import Utilidades.TransformadorHorariosSingleton;
import Utilidades.TransformadorIntSetArray;
import adapter.PlatosCursorAdapter;
import dialogs.CantidadPlatosDialogFragment;
import dialogs.TimePickerFragment;
import inversiondecontrol.ServiceLocator;


public class CrearMenuActivity extends Activity implements TimePickerFragment.TimePickerFragmentListenerJer, CantidadPlatosDialogFragment.CantidadPlatosDialogListener {
    public final static String EXTRA_HORA_TIMEPICKER = "com.altosoftuntref.amorfar.HORA_TIMEPICKER";
    public final static String EXTRA_MINUTOS_TIMEPICKER = "com.altosoftuntref.amorfar.MINUTOS_TIMEPICKER";
    public final static String EXTRA_CANTIDAD_PLATOS = "com.altosoftuntref.amorfar.CANTIDAD_PLATOS";
    public final static String EXTRA_ID_PLATO_A_INTERCAMBIAR = "com.altosoftuntref.amorfar.PLATO_A_INTERCAMBIAR";
    public final static String EXTRA_ID_PLATOS_ACTUALES_A_INTERC = "com.altosoftuntref.amorfar.ID_PLATOS_ACTUALES";

    public final static int OBTENER_PLATOS = 10;
    public final static int OBTENER_PLATOS_CAMBIADOS = 11;

    private final static String SAVED_DIA = "com.altosoftuntref.amorfar.SAVED_DIA";
    private final static String SAVED_MES = "com.altosoftuntref.amorfar.SAVED_MES";
    private final static String SAVED_ANIO = "com.altosoftuntref.amorfar.SAVED_ANIO";
    private final static String SAVED_HORA = "com.altosoftuntref.amorfar.SAVED_HORA";
    private final static String SAVED_MINUTOS = "com.altosoftuntref.amorfar.HORA_MINUTOS";
    private final static String SAVED_CANTIDAD_PLATOS = "com.altosoftuntref.amorfar.CANTIDAD_PLATOS";
    private final static String SAVED_SET_ID_PLATOS_MENU = "com.altosoftuntref.amorfar.SET_ID_PLATOS_MENU";
    private final static String SAVED_HAY_CAMBIOS = "com.altosoftuntref.amorfar.HAY_CAMBIOS";

    private PlatosCursorAdapter platosCursorAdapter;
    private Set<Integer> idPlatosDelMenu;
    private int cantidadPlatos;
    private int dia;
    private int mes;
    private int anio;
    private Integer horaComida;
    private Integer minutosComida;
    private boolean hayCambios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check whether we are recreating a previously destroyed instance.
        if (savedInstanceState != null) {
            //valores salvados, si se esta recreando la actividad.
            this.recuperarDatosSalvados(savedInstanceState);
        } else {
            //Default values, si inicia por primera vez.
            this.obtenerFecha();

            boolean existeAlmuerzoEnBDD = ServiceLocator.getInstance().getMenuesDao(getBaseContext()).existeAlmuerzo(dia, mes, anio);
            if(existeAlmuerzoEnBDD){
                hayCambios = false;
                instanciarConValoresGuardados();
                Toast.makeText(getBaseContext(), "Almuerzo ya creado!", Toast.LENGTH_LONG).show();
            }else {
                hayCambios = true;
                this.reiniciarVotacion();
                this.instanciarConNuevosValores();
                this.seleccionCantidadYPlatos();
            }
        }

        setContentView(R.layout.activity_crear_menu);
        this.actualizarTextviewFecha();
        this.actualizarTextviewHorario();
        this.crearGridViewPlatos();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(SAVED_DIA, dia);
        savedInstanceState.putInt(SAVED_MES, mes);
        savedInstanceState.putInt(SAVED_ANIO, anio);
        savedInstanceState.putInt(SAVED_CANTIDAD_PLATOS, cantidadPlatos);
        savedInstanceState.putInt(SAVED_HORA, horaComida);
        savedInstanceState.putInt(SAVED_MINUTOS, minutosComida);
        int[] idPlatosArray =TransformadorIntSetArray.getInstance().setIntAArrayInt(idPlatosDelMenu);
        savedInstanceState.putIntArray(SAVED_SET_ID_PLATOS_MENU, idPlatosArray);
        savedInstanceState.putBoolean(SAVED_HAY_CAMBIOS, hayCambios);
        super.onSaveInstanceState(savedInstanceState);
    }


//    NO SIRVE PORQUE ESTO LO EJECUTA EN EL onStart() Y LOS VALORES LOS USO EN onCreate().
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState){
//        super.onSaveInstanceState(savedInstanceState);
//        horaComida = savedInstanceState.getInt(SAVED_HORA);
//        minutosComida = savedInstanceState.getInt(SAVED_MINUTOS);
//    }


    /**
     * Recupera los valores de una Instancia anterior.
     * @param savedInstanceState
     */
    private void recuperarDatosSalvados(Bundle savedInstanceState){
        //valores salvados, si se esta recreando la actividad.
        dia = savedInstanceState.getInt(SAVED_DIA);
        mes = savedInstanceState.getInt(SAVED_MES);
        anio = savedInstanceState.getInt(SAVED_ANIO);
        cantidadPlatos = savedInstanceState.getInt(SAVED_CANTIDAD_PLATOS);
        horaComida = savedInstanceState.getInt(SAVED_HORA);
        minutosComida = savedInstanceState.getInt(SAVED_MINUTOS);
        int[] platosGuardadosArray = savedInstanceState.getIntArray(SAVED_SET_ID_PLATOS_MENU);
        idPlatosDelMenu = TransformadorIntSetArray.getInstance().arrayIntASetInt(platosGuardadosArray);
        hayCambios = savedInstanceState.getBoolean(SAVED_HAY_CAMBIOS);
    }


    /**
     * llamado cuando el almuerzo ya existia, Obtiene los datos del mismo
     */
    private void instanciarConValoresGuardados(){
        this.obtenerCantidadPlatos();
        this.obtenerHorarioComida();
        this.obtenerIdPlatosDelMenu();
    }


    /**
     * llamado si el almuerzo no existia, le asigna valores por defecto.
     */
    private void instanciarConNuevosValores(){
        cantidadPlatos = Configuraciones.CANTIDAD_PLATOS_POR_DEFECTO;
        horaComida = Configuraciones.HORA_COMIDA_POR_DEFECTO;
        minutosComida = Configuraciones.MINUTOS_COMIDA_POR_DEFETO;
        idPlatosDelMenu = new HashSet<Integer>();
    }


    /**
     * Obtiene la fecha y se la setea a los atributos correspondientes.
     */
    private void obtenerFecha() {
        Calendar c = Calendar.getInstance();
        this.dia = c.get(Calendar.DAY_OF_MONTH);
        this.mes = c.get(Calendar.MONTH) + 1;
        this.anio = c.get(Calendar.YEAR);
    }


    private void obtenerCantidadPlatos() {
        cantidadPlatos = ServiceLocator.getInstance().getMenuesDao(getBaseContext()).getCantidadPlatos(dia, mes, anio);
    }


    private void obtenerIdPlatosDelMenu() {
            idPlatosDelMenu = ServiceLocator.getInstance().getMenuesDao(getBaseContext()).getCodigosDePlatosDelMenuSet(dia, mes, anio);
    }


    private void obtenerHorarioComida(){
        int[] horario = ServiceLocator.getInstance().getMenuesDao(getBaseContext()).getHorarioAlmuerzo(dia, mes, anio);
        this.horaComida = horario[0];
        this.minutosComida = horario[1];
    }


    /**
     * OnClick.
     * Instancia y muestra el Dialog con el TimePicker que permite cambiar
     * el horario de la comida.
     */
    public void showTimepickerHorarioComida(View view) {
        Bundle bundleHoraYMinutos = new Bundle();
        bundleHoraYMinutos.putInt(EXTRA_HORA_TIMEPICKER, horaComida);
        bundleHoraYMinutos.putInt(EXTRA_MINUTOS_TIMEPICKER, minutosComida);

        DialogFragment miTimepicker = new TimePickerFragment();
        miTimepicker.setArguments(bundleHoraYMinutos);
        miTimepicker.show(this.getFragmentManager(), "timePicker");
    }


    public void crearGridViewPlatos(){
        Cursor platosDelMenu = ServiceLocator.getInstance().getMenuesDao(getBaseContext()).getPlatos(idPlatosDelMenu);
        GridView platosGridView = (GridView) findViewById(R.id.gridView_crearMenu_platos);
        platosCursorAdapter = new PlatosCursorAdapter(getBaseContext(),platosDelMenu,0);
        platosGridView.setAdapter(platosCursorAdapter);
        platosGridView.setOnItemClickListener(onPlatoClick);
    }


    public void actualizarGridViewPlatos(){
        Cursor platosDelMenu = ServiceLocator.getInstance().getMenuesDao(getBaseContext()).getPlatos(idPlatosDelMenu);
        platosCursorAdapter.changeCursor(platosDelMenu);
    }


    /**
     * Es ejecutado por el Dialog TimePickerDialog, en el momento que este
     * es confirmado.
     * Setea el nuevo horario elegido en los atributos correspondientes.
     * @param hora hora seleccionada por el usuario en Dialog
     * @param minutos minutos seleccionados por el ususario en Doalog
     */
    @Override
    public void onHorarioSet(int hora, int minutos) {
        this.horaComida = hora;
        this.minutosComida = minutos;
        this.actualizarTextviewHorario();
        hayCambios = true;
    }


    /**
     * lleva al proceso de elejir la cantidad de platos que se ofrecera (Dialog) en ese menu, y cuando esto
     * se ha hecho, a la eleccion de los mismos SeleccionMultiplesPatos.activity.
     */
    private void seleccionCantidadYPlatos() {
        CantidadPlatosDialogFragment cantidadPlatosDialog = new CantidadPlatosDialogFragment();
        cantidadPlatosDialog.setCancelable(false);
        cantidadPlatosDialog.show(getFragmentManager(), "CantidadPlatosDialog");
    }


    /**
     * Es ejecutado por el Dialog cantidadDePlatosDialog en el momento que este es confirmado.
     * Setea la cantidad de platos elegidos en el atributo correspondiente.
     * @param cantidadPlatos cantidad de platos seleccionados en Dialog.
     */
    @Override
    public void onCantidadPlatosConfirmarClick(int cantidadPlatos) {
        this.cantidadPlatos = cantidadPlatos;
        Toast.makeText(getBaseContext(), "Cant platos: " + this.cantidadPlatos, Toast.LENGTH_LONG).show();
        this.irASeleccionMultiplesPlatos();
    }


    /**
     * Inicia la actividad SeleccionMultiplesPlatos.activity
     */
    private void irASeleccionMultiplesPlatos() {
        Intent intent = new Intent(this, SeleccionMultiplesPlatos.class);
        intent.putExtra(EXTRA_CANTIDAD_PLATOS, this.cantidadPlatos);
        startActivityForResult(intent, OBTENER_PLATOS);
    }


    /**
     * Actualiza el TextView que muestra la hora a partir de los atributos de la actividad
     * horaComida y minutosComida.
     */
    private void actualizarTextviewHorario() {
        TextView textViewHoraComida = (TextView) findViewById(R.id.textView_crearMenu_hora);

        String horaComidaEnTexto = TransformadorHorariosSingleton.getInstance()
                .getHorarioEnTexto(this.horaComida, this.minutosComida);

        textViewHoraComida.setText(horaComidaEnTexto);
    }


    /**
     * Setea la fecha correspondiente, en el TextView que muestra la fecha del menu.
     */
    private void actualizarTextviewFecha() {
        TextView textViewFecha = (TextView) findViewById(R.id.textView_crearMenu_fecha);
        String fechaEnString = TransformadorFechasSingleton.getInstance().getFechaEnTexto(dia, mes, anio);
        textViewFecha.setText(fechaEnString);
    }


    /**
     * OnClick.
     * Inicia la actividad SeleccionPlato.activity cuando se presiona el boton correspondiente.
     */
    public void irACambiarPlato(int platoAIntercambiar) {
        Intent intent = new Intent(this, SeleccionPlatoActivity.class);
        intent.putExtra(EXTRA_ID_PLATO_A_INTERCAMBIAR, platoAIntercambiar);
        int[] idPlatosActuales = TransformadorIntSetArray.getInstance().setIntAArrayInt(idPlatosDelMenu);
        intent.putExtra(EXTRA_ID_PLATOS_ACTUALES_A_INTERC, idPlatosActuales);
        startActivityForResult(intent, OBTENER_PLATOS_CAMBIADOS);
    }


    private AdapterView.OnItemClickListener onPlatoClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

            int idPlatoElejido = (int) id; //(int)gridViewPlatosMultiCheck.getAdapter().getItemId(position);
            irACambiarPlato(idPlatoElejido);
        }
    };


    /**
     * Realiza distintas acciones, dependiendo lo sucedido en SeleccionMultiplesPlatos.activity
     * Si se eligieron platos, guarda sus id en  el Set idPlatosMenu.
     * Si se cancelo, cierra esta actividad tambien.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == OBTENER_PLATOS){

            if (resultCode == RESULT_OK) {
                int[] idPlatosArray = data.getIntArrayExtra(SeleccionMultiplesPlatos.EXTRA_RETURNED_IDPLATOS);
                idPlatosDelMenu = TransformadorIntSetArray.getInstance().arrayIntASetInt(idPlatosArray);
                this.actualizarGridViewPlatos();

            } else if (resultCode == RESULT_CANCELED) {
                finish();
                Toast.makeText(getBaseContext(), R.string.menu_cancelado, Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode == OBTENER_PLATOS_CAMBIADOS){
            if(resultCode == RESULT_OK) {
                int[] idPlatosArray = data.getIntArrayExtra(SeleccionPlatoActivity.EXTRA_RETURNED_IDPLATOS_ACTUALIZADOS);
                idPlatosDelMenu = TransformadorIntSetArray.getInstance().arrayIntASetInt(idPlatosArray);
                this.actualizarGridViewPlatos();
                hayCambios = true;
            }
        }
    }


    /**
     * OnClick
     * Guarda el menu en la bdd
     * @param view
     */
    public void enviarMenu(View view){
        if(hayCambios) {
            boolean guardadoExitoso;
            ServiceLocator.getInstance().getMenuesDao(getBaseContext()).eliminarAlmuerzo(dia, mes, anio);
            guardadoExitoso = ServiceLocator.getInstance().getMenuesDao(getBaseContext()).
                    guardarAlmuerzo(dia, mes, anio, horaComida, minutosComida, idPlatosDelMenu);
            if (guardadoExitoso) {
                Toast.makeText(getBaseContext(), "Menu del dia enviado!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "Fallo al enviar", Toast.LENGTH_LONG).show();
            }
            finish();
        }else{
            Toast.makeText(getBaseContext(), "No hay cambios que enviar", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Reinicia las votaciones para empezar un nuevo almuerzo.
     */
    public void reiniciarVotacion(){
        this.actualizarPremios();
        ServiceLocator.getInstance().getUsuariosDAO(getBaseContext()).reiniciarVotacion();
    }


    /**
     * actualiza los premios dependiendo de la votacion actual, si no votaste te quita el premio
     */
    public void actualizarPremios(){
        ServiceLocator.getInstance().getUsuariosDAO(getBaseContext()).actualizarPremios();
    }


    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_crear_menu, menu);
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

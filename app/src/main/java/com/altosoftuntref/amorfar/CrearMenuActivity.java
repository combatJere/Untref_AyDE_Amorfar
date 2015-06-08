package com.altosoftuntref.amorfar;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import Configuraciones.Configuraciones;
import Persitencia.DAOs.DAOs.Implementacion.MenuesDAOImpl;
import Utilidades.TransformadorFechasSingleton;
import Utilidades.TransformadorHorariosSingleton;
import adapter.PlatosCursorAdapter;
import dialogs.CantidadPlatosDialogFragment;
import dialogs.TimePickerFragment;
import inversiondecontrol.ServiceLocator;


public class CrearMenuActivity extends ActionBarActivity implements TimePickerFragment.TimePickerFragmentListenerJer, CantidadPlatosDialogFragment.CantidadPlatosDialogListener {
    public final static String EXTRA_HORA_TIMEPICKER = "com.altosoftuntref.amorfar.HORA_TIMEPICKER";
    public final static String EXTRA_MINUTOS_TIMEPICKER = "com.altosoftuntref.amorfar.MINUTOS_TIMEPICKER";
    public final static String EXTRA_DIA = "com.altosoftuntref.amorfar.DIA";
    public final static String EXTRA_MES = "com.altosoftuntref.amorfar.MES";
    public final static String EXTRA_ANIO = "com.altosoftuntref.amorfar.ANIO";

    private final static String SAVED_HORA = "com.altosoftuntref.amorfar.SAVED_HORA";
    private final static String SAVED_MINUTOS = "com.altosoftuntref.amorfar.HORA_MINUTOS";
    private final static String SAVED_CANTIDAD_PLATOS = "com.altosoftuntref.amorfar.HORA_CANTIDAD_PLATOS";

    private PlatosCursorAdapter platosCursorAdapter;

    private int cantidadPlatos;
    private int dia;
    private int mes;
    private int anio;
    private Integer horaComida;
    private Integer minutosComida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check whether we are recreating a previously destroyed instance.
        if (savedInstanceState != null) {
            //valores salvados, si se esta recreando la actividad.
            cantidadPlatos = savedInstanceState.getInt(SAVED_CANTIDAD_PLATOS);
            horaComida = savedInstanceState.getInt(SAVED_HORA);
            minutosComida = savedInstanceState.getInt(SAVED_MINUTOS);
        } else {
            //Default values, si inicia por primera vez.
            cantidadPlatos = Configuraciones.CANTIDAD_PLATOS_POR_DEFECTO;
            horaComida = Configuraciones.HORA_COMIDA_POR_DEFECTO;
            minutosComida = Configuraciones.MINUTOS_COMIDA_POR_DEFETO;
        }

        setContentView(R.layout.activity_crear_menu);
        this.obtenerFecha();
        this.actualizarTextviewFecha();
        this.actualizarTextviewHorario();
        this.crearGridViewPlatos();
        this.showCantidadPlatosDialog();

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(SAVED_CANTIDAD_PLATOS, cantidadPlatos);
        savedInstanceState.putInt(SAVED_HORA, horaComida);
        savedInstanceState.putInt(SAVED_MINUTOS, minutosComida);
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

    /**
     * Muestra el Dialog que permite la cantidad de platos que se ofreceran
     * en ese menu
     */
    private void showCantidadPlatosDialog() {
        if (cantidadPlatos == 0) { //MODIFICARRRRRRRRRRRRRRRRRRRRRRRR
            CantidadPlatosDialogFragment cantidadPlatosDialog = new CantidadPlatosDialogFragment();
            cantidadPlatosDialog.show(getFragmentManager(), "CantidadPlatosDialog");
        }
    }

    public void crearGridViewPlatos(){
        Cursor platosDelMenu = ServiceLocator.getInstance().getMenuesDao(getBaseContext()).getPlatosDelMenu(dia, mes, anio);
        GridView platosGridView = (GridView) findViewById(R.id.gridView_crearMenu_platos);
        platosCursorAdapter = new PlatosCursorAdapter(getBaseContext(),platosDelMenu,0);
        platosGridView.setAdapter(platosCursorAdapter);
//        platosGridView.setOnItemClickListener(onPlatoClick);
    }

//    private void crearPlatosPorDafault(){
//        int cantidadPlatosMax = Configuraciones.CANTIDAD_PLATOS_MAX;
//        for(int i = -1; i>){
//
//        }
//    }


    /**
     * Es ejecutado por el Dialog TimePickerDialog, en el momento que este
     * es confirmado.
     * Setea el nuevo horario elegido en los atributos correspondientes.
     *
     * @param hora
     * @param minutos
     */
    @Override
    public void onHorarioSet(int hora, int minutos) {
        this.horaComida = hora;
        this.minutosComida = minutos;
        this.actualizarTextviewHorario();
    }

    /**
     * Es ejecutado por el Dialog cantidadDePlatosDialog en el momento que este es confirmado.
     * Setea la cantidad de platos elegidos en el atributo correspondiente.
     *
     * @param cantidadPlatos
     */
    @Override
    public void onCantidadPlatosConfirmarClick(int cantidadPlatos) {
        this.cantidadPlatos = cantidadPlatos;
        Toast.makeText(getBaseContext(), "Cant platos: " + this.cantidadPlatos, Toast.LENGTH_LONG).show();
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
     * Obtiene la fecha y se la setea a los atributos correspondientes.
     */
    private void obtenerFecha() {
        Calendar c = Calendar.getInstance();
//        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//        String fechaEnString = df.format(c.getTime());
        this.dia = c.get(Calendar.DAY_OF_MONTH);
        this.mes = c.get(Calendar.MONTH) + 1;
        this.anio = c.get(Calendar.YEAR);
    }

    /**
     * OnClick.
     * Inicia la actividad SeleccionPlato.activity cuando se presiona el boton correspondiente.
     */
    public void irASeleccionPlato(View view) {
        Intent intent = new Intent(this, SeleccionPlatoActivity.class);
//        intent.putExtra(EXTRA_DIA, dia);  LUEGO para que no aparezcan los platos ya elejidos
//        intent.putExtra(EXTRA_MES, mes);
//        intent.putExtra(EXTRA_ANIO, anio);
        startActivity(intent);
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

package com.altosoftuntref.amorfar;

import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Set;
import Configuraciones.Configuraciones;
import Utilidades.TransformadorFechasSingleton;
import Utilidades.TransformadorHorariosSingleton;
import Utilidades.TransformadorIntSetArray;
import adapter.PlatosSingleChoiceAdapter;
import dialogs.AdvertenciaVotoTardeDialog;
import dialogs.CantidadInvitadosDialogFragment;
import inversiondecontrol.ServiceLocator;


/**
 * @Pre El menu debe existir. de lo contrario, la actividad fallara.
 */
public class ElejirMenuActivity extends AppCompatActivity implements CantidadInvitadosDialogFragment.CantidadInvitadosDialogListener{

    public final static String EXTRA_CANTIDAD_INVITADOS = "amorfar.elejirMenu.CANTIDAD_INVITADOS";

    private final static String SAVED_NOMBRE_USUARIO_ID = "amorfar.elejirMenu.NOMBRE_USUARIO_ID";
    private final static String SAVED_ID_PLATO_ELEGIDO = "amorfar.elejirMenu.ID_PLATO_ELEGIDO";
    private final static String SAVED_DIA = "amorfar.elejirMenu.SAVED_DIA";
    private final static String SAVED_MES = "amorfar.elejirMenu.SAVED_MES";
    private final static String SAVED_ANIO = "amorfar.elejirMenu.SAVED_ANIO";
    private final static String SAVED_HORA = "amorfar.elejirMenu.SAVED_HORA";
    private final static String SAVED_MINUTOS = "amorfar.elejirMenu.HORA_MINUTOS";
    private final static String SAVED_SET_ID_PLATOS_MENU = "amorfar.elejirMenu.SET_ID_PLATOS_MENU";
    private final static String SAVED_CANTIDAD_INVITADOS = "amorfar.elejirMenu.CANTIDAD_INVITADOS";
    private final static String SAVED_HAY_CAMBIOS = "com.altosoftuntref.amorfar.HAY_CAMBIOS";

    private Menu menu;
    private PlatosSingleChoiceAdapter platosCursorAdapter;
    private Set<Integer> idPlatosDelMenu;
    private String nombreUsuarioID;
    private int idPlatoElejido;
    private int dia;
    private int mes;
    private int anio;
    private Integer horaComida;
    private Integer minutosComida;
    int cantidadInvitados;
    private boolean hayCambios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Check whether we are recreating a previously destroyed instance.
        if (savedInstanceState != null) {
            //valores salvados, si se esta recreando la actividad.
            this.recuperarDatosSalvados(savedInstanceState);
        } else {
            //Default values, si inicia por primera vez.
            nombreUsuarioID = getIntent().getStringExtra(MainActivity.EXTRA_NOMBRE_USUARIO_ID);
            this.obtenerFecha();
            this.obtenerMenu();
            this.instanciarConValoresGuardados();

            boolean platoYaElejido = ServiceLocator.getInstance().getUsuariosDAO(getBaseContext()).platoYaElegido(nombreUsuarioID);

            if(platoYaElejido){
                hayCambios = false;
                Toast.makeText(getBaseContext(), "Plato ya elegido!", Toast.LENGTH_LONG).show();
            }else {
                hayCambios = true;
            }
        }
        setContentView(R.layout.activity_elejir_menu);
        this.actualizarTextviewFecha();
        this.actualizarTextviewHorario();
        this.actualizarTextViewCantidadInvitados();
        this.cambiarEstadoBotonNoComo();
        this.crearGridViewPlatos();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(SAVED_NOMBRE_USUARIO_ID, nombreUsuarioID);
        savedInstanceState.putInt(SAVED_DIA, dia);
        savedInstanceState.putInt(SAVED_MES, mes);
        savedInstanceState.putInt(SAVED_ANIO, anio);
        savedInstanceState.putInt(SAVED_ID_PLATO_ELEGIDO, idPlatoElejido);
        savedInstanceState.putInt(SAVED_HORA, horaComida);
        savedInstanceState.putInt(SAVED_MINUTOS, minutosComida);
        int[] idPlatosArray = TransformadorIntSetArray.getInstance().setIntAArrayInt(idPlatosDelMenu);
        savedInstanceState.putInt(SAVED_CANTIDAD_INVITADOS, cantidadInvitados);
        savedInstanceState.putIntArray(SAVED_SET_ID_PLATOS_MENU, idPlatosArray);
        savedInstanceState.putBoolean(SAVED_HAY_CAMBIOS, hayCambios);
        super.onSaveInstanceState(savedInstanceState);
    }


    /**
     * Recupera los valores de una Instancia anterior.
     * @param savedInstanceState
     */
    private void recuperarDatosSalvados(Bundle savedInstanceState){
        nombreUsuarioID = savedInstanceState.getString(SAVED_NOMBRE_USUARIO_ID);
        dia = savedInstanceState.getInt(SAVED_DIA);
        mes = savedInstanceState.getInt(SAVED_MES);
        anio = savedInstanceState.getInt(SAVED_ANIO);
        idPlatoElejido = savedInstanceState.getInt(SAVED_ID_PLATO_ELEGIDO);
        horaComida = savedInstanceState.getInt(SAVED_HORA);
        minutosComida = savedInstanceState.getInt(SAVED_MINUTOS);
        int[] platosGuardadosArray = savedInstanceState.getIntArray(SAVED_SET_ID_PLATOS_MENU);
        idPlatosDelMenu = TransformadorIntSetArray.getInstance().arrayIntASetInt(platosGuardadosArray);
        cantidadInvitados = savedInstanceState.getInt(EXTRA_CANTIDAD_INVITADOS);
        hayCambios = savedInstanceState.getBoolean(SAVED_HAY_CAMBIOS);
    }


    /**
     * llamado cuando el voto ya existia, Obtiene los datos del mismo
     */
    private void instanciarConValoresGuardados(){
        idPlatoElejido = ServiceLocator.getInstance().getUsuariosDAO(getBaseContext()).getIdPlatoElegido(nombreUsuarioID);
        cantidadInvitados = ServiceLocator.getInstance().getUsuariosDAO(getBaseContext()).getCantidadInvitados(nombreUsuarioID);
    }


    /**
     * @deprecado El menu debe existir. No se puede ingresar hasta que le mismo exista.
     *
     * llamado si el almuerzo no existia, le asigna valores por defecto.
     */
//    private void instanciarConNuevosValores(){
//        idPlatoElejido = Configuraciones.SIN_PLATO_ELEGIDO;
//        cantidadInvitados = Configuraciones.CANTIDAD_INVITADOS_POR_DEFECTO;
//    }


    /**
     * Obtiene los datos del almuerzo
     */
    private void obtenerMenu(){
        this.obtenerHorarioComida();
        this.obtenerIdPlatosDelMenu();
    }


    /**
     * Obtiene la fecha y se la setea a los atributos correspondientes.
     */
    private void obtenerFecha(){
        Calendar c = Calendar.getInstance();
        this.dia = c.get(Calendar.DAY_OF_MONTH);
        this.mes = c.get(Calendar.MONTH) + 1;
        this.anio = c.get(Calendar.YEAR);
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
     * @Pre: Necesita que ya se hayan obtenido todos los valores (atributos)
     * Instancia todos los componentes visuales de la activity, una vez.
     */
    private void instanciarComponentes(){
        this.actualizarTextviewFecha();
        this.actualizarTextviewHorario();
        this.actualizarTextViewCantidadInvitados();
        this.cambiarEstadoBotonNoComo();
        this.crearGridViewPlatos();
    }


    /**
     * Actualiza el TextView que muestra la hora a partir de los atributos de la actividad
     * horaComida y minutosComida.
     */
    private void actualizarTextviewHorario() {
        TextView textViewHoraComida = (TextView) findViewById(R.id.textView_elejirMenu_hora);
        String horaComidaEnTexto = TransformadorHorariosSingleton.getInstance()
                .getHorarioEnTexto(this.horaComida, this.minutosComida);
        textViewHoraComida.setText(horaComidaEnTexto);
    }


    /**
     * Setea la fecha correspondiente, en el TextView que muestra la fecha del menu.
     */
    private void actualizarTextviewFecha() {
        TextView textViewFecha = (TextView) findViewById(R.id.textView_elejirMenu_fecha);
        String fechaEnString = TransformadorFechasSingleton.getInstance().getFechaEnTexto(dia, mes, anio);
        textViewFecha.setText(fechaEnString);
    }


    /**
     * Crea e instancia el gridview.
     */
    public void crearGridViewPlatos(){
        Cursor platosDelMenu = ServiceLocator.getInstance().getMenuesDao(getBaseContext()).getPlatos(idPlatosDelMenu);
        GridView platosGridView = (GridView) findViewById(R.id.gridView_elejirMenu_platos);
        platosCursorAdapter = new PlatosSingleChoiceAdapter(getBaseContext(), platosDelMenu, 0, idPlatoElejido);
        platosGridView.setAdapter(platosCursorAdapter);
        platosGridView.setOnItemClickListener(onPlatoClick);
    }


    /**
     * @Edu Resuelto distinto que en MultipleChoice, En este caso se le pasa al adapter el id del item
     *      que debe ser resaltado. y se le pide que se vuelva a dibujar.
     *      El se encarga de ver quien debe resaltarse cuando se vuelve a dibijar.
     */
    private AdapterView.OnItemClickListener onPlatoClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

            int idNuevoPlatoElejido = (int) id;

            if(idNuevoPlatoElejido != idPlatoElejido){
                idPlatoElejido = idNuevoPlatoElejido;
                actualizarGridView();
                idPlatoElejido = idNuevoPlatoElejido;
                cambiarEstadoBotonNoComo();
                cambioRealizado();
            }
        }
    };


    /**
     * actualiza el GridView
     * usado cuando el usuario elige otro plato.
     */
    private void actualizarGridView(){
        platosCursorAdapter.cambiarPlatoElegido(idPlatoElejido);
        platosCursorAdapter.notifyDataSetChanged();
    }


    /**
     * OnClick
     * Se ejecuta al presionarse el boton hoyNoComo.
     * Registra que el usurio no desea comer.
     * @param view
     */
    public void votacionNoComo(View view){
        if(idPlatoElejido != Configuraciones.NO_COME){
            idPlatoElejido = Configuraciones.NO_COME;
            cambioRealizado();
            cambiarEstadoBotonNoComo();
            this.actualizarGridView();
        }
    }


    /**
     * Chequea el estado del boton hoyNoComo y lo resalta segun esa sea la opcion elegida o no.
     */
    private void cambiarEstadoBotonNoComo(){
        Button botonNoComo = (Button) findViewById(R.id.button_elejirMenu_hoyNoComo);
        if(idPlatoElejido == Configuraciones.NO_COME){
            botonNoComo.setBackgroundColor(getResources().getColor(R.color.gridViewItemCentro_background_checked));
        }else{
            botonNoComo.setBackgroundColor(getResources().getColor(R.color.gridViewItemCentro_background));
        }
    }


    /**
     * OnClick
     * Muestra el dialog que permite elegir la cantidad de invitados.
     * @param view
     */
    public void cambiarCantidadInvitados(View view){
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_CANTIDAD_INVITADOS, cantidadInvitados);
        CantidadInvitadosDialogFragment cantInvitadosDialog = new CantidadInvitadosDialogFragment();
        cantInvitadosDialog.setArguments(bundle);
        cantInvitadosDialog.show(this.getFragmentManager(),"cantInvitadpsDialog");
    }


    /**
     * Ejecutado cuando se confirma el Dialog CantidadInvitadosDialogFragment
     * @param cantidadInvitados la cantidad de invitados elegidos en el Dialog.
     */
    @Override
    public void onCantidadInvitadosConfirmarClick(int cantidadInvitados) {
        this.cantidadInvitados = cantidadInvitados;
        this.actualizarTextViewCantidadInvitados();
        cambioRealizado();
    }


    /**
     * Actualiza el textview con el nuevo numero de invitados
     */
    private void actualizarTextViewCantidadInvitados(){
        TextView textViewCantInvitados = (TextView)findViewById(R.id.textView_elejirMenu_cantidadInvitados);
        textViewCantInvitados.setText(String.valueOf(cantidadInvitados));
    }


    /**
     * Envia la votacion, si es que no se voto o se hizo algun cambio
     * Siempre y cuando el horario no pase de las 11hs (CAMBIAR POR HISTORIA DE USUARIO CAMBIAR)
     */
    public void enviarVotacion(){

        if (hayCambios) {
            boolean guardadoExitoso;
            //si conservava el premio, y voto, lo sigue conservando.
            boolean conservaPremio;

//            if(!horaDeVotacionPermitida()){
//                conservaPremio = false;
//            }else{
//                conservaPremio = ServiceLocator.getInstance().getUsuariosDAO(getBaseContext())
//                        .usuarioTienePremio(nombreUsuarioID);
//            }
            conservaPremio = this.analizarEstadoPremio();

            guardadoExitoso = ServiceLocator.getInstance().getUsuariosDAO(getBaseContext()).
                    enviarVoto(nombreUsuarioID, conservaPremio, idPlatoElejido, cantidadInvitados);

            String textoConservaPremio = getTextoConservaPremio(conservaPremio);

            if (guardadoExitoso) {
                if(horaDeVotacionPermitida()){
                    Toast.makeText(getBaseContext(), "Votacion enviada!\n" + textoConservaPremio, Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    showAdvertenciaTardeDialogYEnviar();
                }

            } else {
                Toast.makeText(getBaseContext(), "Upss, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
                finish();
            }

        } else {
            Toast.makeText(getBaseContext(), "No hay cambios que enviar", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Analiza si el usuario conserva el premio luego de esta votacion.
     * Si voto despues de las la hora de cierre de votacion, o si ya lo havia perdido anteriormente,
     * lo pierde.
     * @return
     */
    public boolean analizarEstadoPremio(){
        boolean conservaPremio;
        if(!horaDeVotacionPermitida()){
            conservaPremio = false;
        }else{
            conservaPremio = ServiceLocator.getInstance().getUsuariosDAO(getBaseContext())
                    .usuarioTienePremio(nombreUsuarioID);
        }
        return conservaPremio;
    }


    /**
     * Segun tenga premio o no, devuelve el texto a mostrar correspondiente.
     * @param conservaPremio
     * @return
     */
    private String getTextoConservaPremio (boolean conservaPremio){
        String textoConservaPremio;
        if(conservaPremio){
            textoConservaPremio = getResources().getString(R.string.texto_comserva_premio);
        }else{
            textoConservaPremio = getResources().getString(R.string.texto_no_conserva_premio);
        }
        return textoConservaPremio;
    }


    /**
     * //CUIDADO LA HORA DEBE COMPROBARLA DE OTRO LADO, NO DEL CELULAR. ARREGLAR
     * @return true si todavia es horario de votacion.
     */
    private boolean horaDeVotacionPermitida(){
        boolean votacionPermitida = false;

        Calendar c = Calendar.getInstance();
        int Hr24=c.get(Calendar.HOUR_OF_DAY);
        int Min=c.get(Calendar.MINUTE);

        if(Hr24 < Configuraciones.HORA_LIMITE_VOTACION){
            votacionPermitida = true;
        }else{
            if(Hr24 == Configuraciones.HORA_LIMITE_VOTACION && Min < Configuraciones.MINUTOS_LIMITE_VOTACION){
                votacionPermitida =true;
            }
        }
        return votacionPermitida;
    }


    /**
     * Muestra un mensaje de advertencia para los usuarios que votaron despues del horario permitido
     * y finaliza la actividad.
     */
    private void showAdvertenciaTardeDialogYEnviar(){
        AdvertenciaVotoTardeDialog votoTardeDialog = new AdvertenciaVotoTardeDialog();
        votoTardeDialog.show(getFragmentManager(),"advertencia_dialog");
    }


    /**
     * Llamar cuando se hace algun cambio.
     * Setea la variable hayCambio como verdadera y cambia el icono de enviar a activado (blanco).
     */
    private void cambioRealizado(){
        hayCambios = true;
        MenuItem itemEnviar = menu.findItem(R.id.action_elegirMenu_Enviar);
        itemEnviar.getIcon().clearColorFilter();
//        itemEnviar.setIcon(R.mipmap.hecho);
//        itemEnviar.setVisible(true);
    }


    /**
     * Setea cada icono en el estado que le corresponde (colores)
     */
    private void setearEstadoIconos(){
        if(!hayCambios){
            MenuItem itemEnviar = menu.findItem(R.id.action_elegirMenu_Enviar);
            itemEnviar.getIcon().setColorFilter(R.color.floating_action_button, PorterDuff.Mode.MULTIPLY);
//            itemEnviar.setIcon(R.mipmap.hecho_desactivado_osc);
//            itemEnviar.setVisible(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_elejir_menu, menu);
        this.menu = menu;
        setearEstadoIconos();

        return super.onCreateOptionsMenu(menu);
//        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (id == R.id.action_elegirMenu_Enviar){
            this.enviarVotacion();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

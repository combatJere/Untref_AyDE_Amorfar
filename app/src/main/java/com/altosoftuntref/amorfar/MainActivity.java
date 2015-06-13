package com.altosoftuntref.amorfar;

import android.app.Service;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import dialogs.SobreAltosoftDialog;
import inversiondecontrol.ServiceLocator;

/**
 * Actividad que permite elegir a que parte del sistema ingresar.
 * Segun el nivel de usuario que ingrese, tendra acceso a distintas funciones.
 */
public class MainActivity extends ActionBarActivity {

    public final static String EXTRA_NOMBRE_USUARIO_ID = "amorfar.mainActivity.NOMBRE_USUARIO_ID";

    private String nombreUsuarioID;
    private boolean esAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        nombreUsuarioID = intent.getStringExtra(LoginActivity.EXTRA_NOMBRE_USUARIO);
        TextView textViewNombreUsuario = (TextView) findViewById(R.id.textView_nombreUsuario);
        textViewNombreUsuario.setText(nombreUsuarioID);

        esAdmin = ServiceLocator.getInstance().getUsuariosDAO(getBaseContext()).esAdmin(nombreUsuarioID);
        this.ocultarBotonesSiNoEsAdmin();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void ocultarBotonesSiNoEsAdmin(){
        if(!esAdmin) {
            Button botonIrACrearMenu = (Button) findViewById(R.id.button_irACrearMenu);
            botonIrACrearMenu.setVisibility(View.GONE);
            Button botonIrAInforme = (Button) findViewById(R.id.button_mostrarInforme);
            botonIrAInforme.setVisibility(View.GONE);
        }
    }

    /**
     * OnClick
     * Inicia la actividad CrearMEnu.activity
     */
    public void irACrearMenu(View view){
        Intent intent = new Intent(this, CrearMenuActivity.class);
        startActivity(intent);
    }

    /**
     * OnClick
     * Inicia la actividad ElejirMenu.activity
     * @param view
     */
    public void irAElejirMenu(View view){
        Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH) + 1;
        int anio = c.get(Calendar.YEAR);
        if(ServiceLocator.getInstance().getMenuesDao(getBaseContext()).existeAlmuerzo(dia, mes, anio)) {
            Intent intent = new Intent(this, ElejirMenuActivity.class);
            intent.putExtra(EXTRA_NOMBRE_USUARIO_ID, nombreUsuarioID);
            startActivity(intent);
        }else{
            Toast.makeText(getBaseContext(), "Todavia no hay almuerzo!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * OnClick
     * @param view
     */
    public void irAInforme(View view){
        Intent intent = new Intent(this, InformeActivity.class);
        startActivity(intent);
    }

    /**
     * onClick
     * muestra el dialog sobre altosoft cuando alguien toca la marca de agua de la empreza
     */
    public void mostrarDialogAltosoft(View view){
        SobreAltosoftDialog dialogAltosoft = new SobreAltosoftDialog();
        dialogAltosoft.show(this.getFragmentManager(),"dialog_ltosoft");
    }
}

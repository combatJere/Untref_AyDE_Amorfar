package com.altosoftuntref.amorfar;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

import Persitencia.DAOs.DAOs.Implementacion.MenuesDAOImpl;
import inversiondecontrol.ServiceLocator;


public class InformeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe);
        this.crearInforme();
    }

    public void crearInforme(){
        String stringADevolver = "";

        Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH) + 1;
        int anio = c.get(Calendar.YEAR);

        int cantidadNoComen = ServiceLocator.getInstance().getUsuariosDAO(getBaseContext()).getCantidadNoComen(dia, mes, anio);
        int cantidadNoVotaron = ServiceLocator.getInstance().getUsuariosDAO(getBaseContext()).getCantidadNoVotaron(dia, mes ,anio);
        int cantidadInvitados = ServiceLocator.getInstance().getUsuariosDAO(getBaseContext()).getCantidadDeInvitadosTotales(dia, mes, anio);

        stringADevolver= "\n No comen:  "+cantidadNoComen + "\n No votaron:  "
                + cantidadNoVotaron + "\n Invitados: " +cantidadInvitados + "\n\n";

        Set<Integer> codigosPlatos;
        codigosPlatos= ServiceLocator.getInstance().getMenuesDao(getBaseContext()).getCodigosDePlatosDelMenuSet(dia, mes, anio);
        Iterator<Integer> it = codigosPlatos.iterator();

        int elementoActual;
        while(it.hasNext()){
            elementoActual = it.next();
            stringADevolver = stringADevolver + "   " + armarRenglon(elementoActual);
        }

        TextView textViewInforme = (TextView) findViewById(R.id.textView_informe_resumen);
        textViewInforme.setText(stringADevolver);
    }

    public String armarRenglon(int idPlato){
        String nombrePlato = ServiceLocator.getInstance().getMenuesDao(getBaseContext()).getNombrePlato(idPlato);
        int cantidadPlatos = ServiceLocator.getInstance().getMenuesDao(getBaseContext()).getCantidadDelPlato(idPlato);
        return nombrePlato + ":  " + cantidadPlatos + "\n";

    }

    /**
     * OnClick
     */
    public void irAListaDePremios(View view){
        Intent intent = new Intent(this, ListaDePremiosActivity.class);
        startActivity(intent);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_informe, menu);
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

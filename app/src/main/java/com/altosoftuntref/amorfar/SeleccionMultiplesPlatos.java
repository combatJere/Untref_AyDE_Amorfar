package com.altosoftuntref.amorfar;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import adapter.PlatosCursorAdapter;
import adapter.PlatosMultipleChoiceAdapter;
import inversiondecontrol.ServiceLocator;
import layouts.customs.GridViewItem;


public class SeleccionMultiplesPlatos extends Activity {

    private  int cantPlatosRestantes;
    private Set<Integer> platosElejidos = new HashSet<Integer>();
    private TextView textViewCantidadPlatosRestantes;
    private GridView gridViewPlatosMultiCheck;
    private PlatosMultipleChoiceAdapter platosCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_multiples_platos);
        cantPlatosRestantes = getIntent().getIntExtra(CrearMenuActivity.EXTRA_CANTIDAD_PLATOS, 0);

        this.instanciarGridViewSeleccionMultiple();
        this.instanciarTextViewPlatosRestantes();
    }

    private void instanciarTextViewPlatosRestantes(){
        textViewCantidadPlatosRestantes = (TextView) findViewById(R.id.textView_seleccionMultiplesPlatos_CANTplatosRestantes);
        textViewCantidadPlatosRestantes.setText(String.valueOf(cantPlatosRestantes));
    }

    private void instanciarGridViewSeleccionMultiple(){
        Cursor cursorAllPlatos = ServiceLocator.getInstance().getMenuesDao(getBaseContext()).getAllPlatosGuardadosCursor();
        gridViewPlatosMultiCheck = (GridView) findViewById(R.id.gridView_seleccionMultiplesPlatos_platos);
        platosCursorAdapter = new PlatosMultipleChoiceAdapter(getBaseContext(), cursorAllPlatos, 0, platosElejidos);
        gridViewPlatosMultiCheck.setAdapter(platosCursorAdapter);
        gridViewPlatosMultiCheck.setOnItemClickListener(onPlatoClick);
    }

    private AdapterView.OnItemClickListener onPlatoClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

            int idPlatoElejido = (int) id; //(int)gridViewPlatosMultiCheck.getAdapter().getItemId(position);
            GridViewItem itemElegido = (GridViewItem) view;
            FrameLayout centroBoton = (FrameLayout) itemElegido.findViewById(R.id.centro_boton);

            if(!platosElejidos.contains(idPlatoElejido)){

                if (cantPlatosRestantes > 0) {

                    itemElegido.setBackgroundColor(getResources().getColor(R.color.gridViewItem_background_checked));
                    centroBoton.setBackgroundColor(getResources().getColor(R.color.gridViewItemCentro_background_checked));
                    platosElejidos.add(idPlatoElejido);
                    cantPlatosRestantes--;
                    textViewCantidadPlatosRestantes.setText(String.valueOf(cantPlatosRestantes));

                }else{
                    Toast.makeText(getApplicationContext(), "Ya se eligiron todos los platos", Toast.LENGTH_LONG).show();
                }

            }else{
                itemElegido.setBackgroundColor(getResources().getColor(R.color.gridViewItem_background));
                centroBoton.setBackgroundColor(getResources().getColor(R.color.gridViewItemCentro_background));
                platosElejidos.remove(idPlatoElejido);
                cantPlatosRestantes++;
                textViewCantidadPlatosRestantes.setText(String.valueOf(cantPlatosRestantes));
            }
        }
    };



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

package com.altosoftuntref.amorfar;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Actividad que permite elegir a que parte del sistema ingresar.
 * Segun el nivel de usuario que ingrese, tendra acceso a distintas funciones.
 */
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String nombreUsuario = intent.getStringExtra(LoginActivity.EXTRA_NOMBRE_USUARIO);

        TextView textViewNombreUsuario = (TextView) findViewById(R.id.textView_nombreUsuario);
        textViewNombreUsuario.setText(nombreUsuario);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }

    /**
     * OnClick
     * Inicia la actividad CrearMEnu.activity
     */
    public void irACrearMenu(View view){
        Intent intent = new Intent(this, CrearMenuActivity.class);
        startActivity(intent);
    }
}

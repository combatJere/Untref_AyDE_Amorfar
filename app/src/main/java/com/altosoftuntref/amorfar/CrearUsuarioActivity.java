package com.altosoftuntref.amorfar;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Persitencia.BaseDeDatosContract;
import Persitencia.BaseDeDatosHelper;
import Persitencia.DAOs.DAOs.Implementacion.UsuariosDAOImpl;
import Persitencia.DAOs.UsuariosDAO;
import inversiondecontrol.ServiceLocator;


public class CrearUsuarioActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);
    }

    /**
     * OnClick
     * Valida los datos ingresados por el Usuario que pretende crear una cuenta, obtenidos de los EditText
     * Si el usuario no existe y los datos son validos, lo guarda en la BDD.
     * @param view
     */
    public void guardarNuevoUsuario(View view){
        UsuariosDAO usuariosDao = ServiceLocator.getInstance().getUsuariosDAO(getBaseContext());
        boolean usuarioGuardadoConExito = false;


        TextView mensaje = (TextView) findViewById(R.id.textView_crearUsuario_mensaje);

        EditText editTextUsuario = (EditText)findViewById(R.id.editText_crearUsuario_usuario);
        String nombreUsuarioIngresado = editTextUsuario.getText().toString();

        EditText editTextClave = (EditText)findViewById(R.id.editText_crearUsuario_clave);
        String claveIngresada = editTextClave.getText().toString();

        EditText editTextClaveRepetida = (EditText)findViewById(R.id.editText_crearUsuario_claveRepetida);
        String claveRepetidaIngresada = editTextClaveRepetida.getText().toString();

        if(nombreUsuarioIngresado.length()<5){
            mensaje.setText(R.string.nombre_usuario_no_valido);
        }else if(claveIngresada.length() < 4){
            mensaje.setText(R.string.clave_no_valida);
        }else if(!claveIngresada.equals(claveRepetidaIngresada)){
            mensaje.setText(R.string.claves_no_coinciden);
        }else{

            if(!usuariosDao.usuarioExiste(nombreUsuarioIngresado)){
                usuariosDao.guardarUsuario(nombreUsuarioIngresado, claveIngresada,0);
                Toast.makeText(getBaseContext(), R.string.usuario_creado_con_exito, Toast.LENGTH_LONG).show();
                finish();
            }else{
                mensaje.setText("El nombre de usuario \""+ nombreUsuarioIngresado +"\" ya existe");
                editTextClave.getText().clear();
                editTextClaveRepetida.getText().clear();
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_crear_usuario, menu);
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

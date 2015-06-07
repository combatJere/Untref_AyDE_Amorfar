package com.altosoftuntref.amorfar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import Persitencia.BaseDeDatosContract;
import Persitencia.BaseDeDatosHelper;
import Persitencia.DAOs.DAOs.Implementacion.UsuariosDAOImpl;
import Persitencia.DAOs.UsuariosDAO;
import inversiondecontrol.ServiceLocator;

public class LoginActivity extends ActionBarActivity {
    public final static String EXTRA_NOMBRE_USUARIO = "com.altosoftuntref.amorfar.NOMBRE_USUARIO";
//    private Map<String,String> usuariosYClaves = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        TextView mensaje = (TextView)findViewById(R.id.textView_login_mensaje);
        mensaje.setText("");
        super.onResume();
    }

    /**
     * Si los datos del usuario que pretende logearse son validos, y pertenecen a un usuario
     * registrado en la BDD, le permite el ingreso al sistema.
     * @param view
     */
    public void irAMainActivity(View view){
        boolean usuarioYClaveValidos = false;
        Intent intent = new Intent(this, MainActivity.class);

        TextView mensaje = (TextView)findViewById(R.id.textView_login_mensaje);

        EditText editTextUsuario = (EditText)findViewById(R.id.editText_login_usuario);
        String nombreUsuarioIngresado = editTextUsuario.getText().toString();

        EditText editTextClave = (EditText)findViewById(R.id.editText_login_clave);
        String claveIngresada = editTextClave.getText().toString();

        if(nombreUsuarioIngresado.length() < 5){
            mensaje.setText(R.string.nombre_usuario_no_valido);
        }else if(claveIngresada.length() < 4){
            mensaje.setText(R.string.clave_no_valida);
        }else{
            usuarioYClaveValidos = this.validarUsuario(nombreUsuarioIngresado, claveIngresada, mensaje);
        }

        this.ocultarTeclado();

        if(usuarioYClaveValidos){
            intent.putExtra(EXTRA_NOMBRE_USUARIO, nombreUsuarioIngresado);
            startActivity(intent);
        }

        editTextClave.getText().clear();
    }

    /**
     * @param nombreUsuarioIngresado
     * @param claveIngresada
     * @param mensaje: el texto a mostrar en caso de error
     * @return true, si el nombre de usuario existe y la clave ingresada coincide.
     */
    private boolean validarUsuario(String nombreUsuarioIngresado, String claveIngresada, TextView mensaje){
        boolean usuarioEsValido = false;
        UsuariosDAO  usuarioDao = ServiceLocator.getInstance().getUsuariosDAO(getBaseContext());
        String claveUsuarioGuardada;


        if(usuarioDao.usuarioExiste(nombreUsuarioIngresado)){
            claveUsuarioGuardada = usuarioDao.obtenerClaveUsuario(nombreUsuarioIngresado);
            if(claveIngresada.equals(claveUsuarioGuardada)){
                usuarioEsValido = true;
            }else{
                mensaje.setText(R.string.clave_no_correcta);
            }
        }else{
            mensaje.setText("El nombre de usuario \"" + nombreUsuarioIngresado + "\" no existe");
        }
        return usuarioEsValido;
    }

    private void ocultarTeclado(){
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * OnClick
     * Inicia la actividad CrearUsuario.activity
     * @param view
     */
    public void irACrearUsuario(View view){
        Intent intent = new Intent(this, CrearUsuarioActivity.class);
        startActivity(intent);
    }


    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_login, menu);
//        return true;
//    }

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

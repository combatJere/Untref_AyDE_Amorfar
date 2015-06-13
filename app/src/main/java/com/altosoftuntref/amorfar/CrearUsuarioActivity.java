package com.altosoftuntref.amorfar;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Configuraciones.Configuraciones;
import Persitencia.DAOs.UsuariosDAO;
import inversiondecontrol.ServiceLocator;


public class CrearUsuarioActivity extends ActionBarActivity {

    EditText textviewClaveAdminMaestra;
    boolean soyAdminChecked = false;
    int esAdmin = Configuraciones.NO_ES_ADMIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);
        this.instanciarEditTextClaveMaestra();
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

        if(soyAdminChecked){
            this.comprobarSiEsAdmin();
        }

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
                usuariosDao.guardarUsuario(nombreUsuarioIngresado, claveIngresada, esAdmin);
                Toast.makeText(getBaseContext(), R.string.usuario_creado_con_exito, Toast.LENGTH_LONG).show();
                finish();
            }else{
                mensaje.setText("El nombre de usuario \""+ nombreUsuarioIngresado +"\" ya existe");
                editTextClave.getText().clear();
                editTextClaveRepetida.getText().clear();
            }
        }
    }

    /**
     * Comprueba si un usuario que dice ser admin, lo es realmente.
     *  -En caso afirmativo, lo guarda como tipo de usuario admin.
     *  -En caso positivo lo retorna a la actividad anteriori mostrando un mensaje.
     */
    private void comprobarSiEsAdmin(){
        String claveMaestraIngresada = textviewClaveAdminMaestra.getText().toString();
        if(!claveMaestraIngresada.equals(Configuraciones.CLAVE_ADMIN_MAESTRA)){
            finish();
            Toast.makeText(getBaseContext(), R.string.usuario_no_autorizado, Toast.LENGTH_LONG).show();
        }else{
            esAdmin = Configuraciones.ES_ADMIN;
        }

    }

    private void instanciarEditTextClaveMaestra(){
        textviewClaveAdminMaestra = (EditText) findViewById(R.id.editText_crearUSuario_claveAdminMaestra);
        textviewClaveAdminMaestra.setVisibility(View.GONE);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox_crearUsuario_soyAdmin:
                if (checked){
                    soyAdminChecked = true;
                    textviewClaveAdminMaestra.setVisibility(View.VISIBLE);
                }else{
                    soyAdminChecked = false;
                    textviewClaveAdminMaestra.setVisibility(View.GONE);
                }
                break;
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

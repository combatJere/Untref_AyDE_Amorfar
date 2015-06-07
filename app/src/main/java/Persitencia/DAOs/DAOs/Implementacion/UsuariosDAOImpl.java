package Persitencia.DAOs.DAOs.Implementacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.altosoftuntref.amorfar.R;

import Persitencia.BaseDeDatosContract;
import Persitencia.BaseDeDatosHelper;
import Persitencia.DAOs.UsuariosDAO;

/**
 * Created by jeremias on 05/06/2015.
 */
public class UsuariosDAOImpl implements UsuariosDAO{

    private static UsuariosDAOImpl instance; //singleton
    private BaseDeDatosHelper miDbHelper;

    private UsuariosDAOImpl (Context context) {
        miDbHelper = BaseDeDatosHelper.getInstance(context);
    }

    public static UsuariosDAO getInstance(Context context) {
        if (instance == null) {
            instance = new UsuariosDAOImpl(context);
        }
        return instance;
    }



    @Override
    public boolean guardarUsuario(String nombreUsuario, String clave, int esAdministrador) {
        boolean usuarioCreadoConExito;
        SQLiteDatabase db = miDbHelper.getWritableDatabase();

        try{
//            SQLiteDatabase db = miDbHelper.getWritableDatabase();
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_USUARIO_ID, nombreUsuario);
            values.put(BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_CLAVE, clave);
            values.put(BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_PREMIO, 1);
            values.put(BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_ES_ADMINISTRDOR, esAdministrador);
            // Insert the new row, returning the primary key value of the new row
            db.insertOrThrow(BaseDeDatosContract.UsuariosYClave.TABLE_NAME, "null", values);

            usuarioCreadoConExito = true;
        }catch (SQLException e){
            usuarioCreadoConExito = false;
        }finally{
//            miDbHelper.close();
            db.close();
        }
        return usuarioCreadoConExito;
    }

    @Override
    public String obtenerClaveUsuario(String nombreUsuario){
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        String claveUsuarioObtenida;

        String[] columnsToReturn = {
                BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_USUARIO_ID,
                BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_CLAVE,
        };

        String whereClause= BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_USUARIO_ID + "=?";
        String[] whereValues = {nombreUsuario};

        try {
            Cursor cursor = db.query(
                    BaseDeDatosContract.UsuariosYClave.TABLE_NAME,  // The table to query
                    columnsToReturn,                               // The columns to return
                    whereClause,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            cursor.moveToFirst();
            claveUsuarioObtenida = cursor.getString(cursor.getColumnIndex(BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_CLAVE));
            cursor.close();

            //si el cursor no esta vacio es porque se encontro un usuario con ese nombre.
//            if (cursor.moveToFirst()) {
//                String claveObtenida = cursor.getString(cursor.getColumnIndex(BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_CLAVE));
//                if(claveObtenida.equals(claveIngresada)){
//                    usuarioEsValido = true;
//                }else{
//                    mensaje.setText(R.string.clave_no_correcta);
//                }
//            } else {
//                mensaje.setText("El nombre de usuario \"" + nombreUsuarioIngresado + "\" no existe");
//            }
        }finally {
            db.close();
        }
        return claveUsuarioObtenida;
    }

    @Override
    public boolean usuarioExiste(String nombreUsuario){
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        boolean existe = false;

        String[] columnsToReturn = {
                BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_USUARIO_ID,
        };

        String whereClause= BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_USUARIO_ID + "=?";
        String[] whereValues = {nombreUsuario};

        try {
            Cursor cursor = db.query(
                    BaseDeDatosContract.UsuariosYClave.TABLE_NAME,  // The table to query
                    columnsToReturn,                               // The columns to return
                    whereClause,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            //devuelve true si tiene elementos, osea, si encontro el nombre de usuario.
            if(cursor.moveToFirst()){
                existe = true;
            }
            cursor.close();

        }finally {
            db.close();
        }
        return existe;
    }
}

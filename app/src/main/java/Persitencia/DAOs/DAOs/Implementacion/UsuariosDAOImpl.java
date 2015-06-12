package Persitencia.DAOs.DAOs.Implementacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import Configuraciones.Configuraciones;
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


    /**
     * Guarda los datos obtenidos, junto a datos por defecto en ambas tablas de usuario
     * En caso de fallar el guardado en la segunda tabla, elimina los datos guardados en la primer tabla.
     * @param nombreUsuario
     * @param clave
     * @param esAdministrador
     * @return
     */
    @Override
    public boolean guardarUsuario(String nombreUsuario, String clave, int esAdministrador) {
        boolean usuarioCreadoConExito;
        SQLiteDatabase db = miDbHelper.getWritableDatabase();

        try{
            ContentValues values = new ContentValues();
            values.put(BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_USUARIO_ID, nombreUsuario);
            values.put(BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_CLAVE, clave);
            values.put(BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_ES_ADMINISTRDOR, esAdministrador);
            db.insertOrThrow(BaseDeDatosContract.UsuariosYClave.TABLE_NAME, "null", values);

            usuarioCreadoConExito = true;
        }catch (SQLException e) {
            db.close();
            usuarioCreadoConExito = false;
        }

        if(usuarioCreadoConExito) {
            try {
                ContentValues values = new ContentValues();
                values.put(BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_NOMBRE_USUARIO_ID, nombreUsuario);
                values.put(BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_TIENE_PREMIO, Configuraciones.TIENE_PREMIO);
                values.put(BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_COD_PLATO_ELEJIDO, Configuraciones.SIN_PLATO_ELEGIDO);
                values.put(BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_CANTIDAD_INVITADOS, Configuraciones.CANTIDAD_INVITADOS_POR_DEFECTO);
                db.insertOrThrow(BaseDeDatosContract.UsuariosYAvisos.TABLE_NAME, "null", values);

            } catch (SQLException e) {
                String whereClause= BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_NOMBRE_USUARIO_ID + "=?";
                String[] whereValues = {nombreUsuario};
                db.delete(
                        BaseDeDatosContract.UsuariosYClave.TABLE_NAME,
                        whereClause,
                        whereValues
                );
                usuarioCreadoConExito = false;
            } finally {
                db.close();
            }
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

    @Override
    public int getIdPlatoElegido(String nombreUsuarioID) {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        int codigoPlato;

        String[] columnsToReturn = {
                BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_COD_PLATO_ELEJIDO,
        };

        String whereClause= BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_NOMBRE_USUARIO_ID + "=?";
        String[] whereValues = {nombreUsuarioID};

        try {
            Cursor cursor = db.query(
                    BaseDeDatosContract.UsuariosYAvisos.TABLE_NAME,  // The table to query
                    columnsToReturn,                               // The columns to return
                    whereClause,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            cursor.moveToFirst();
            codigoPlato = cursor.getInt(cursor.getColumnIndex(BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_COD_PLATO_ELEJIDO));
            cursor.close();

        }finally {
            db.close();
        }
        return codigoPlato;
    }

    @Override
    public boolean platoYaElegido(String nombreUsuarioID) {
        boolean existe = false;
        int codigoGuardado = getIdPlatoElegido(nombreUsuarioID);
        if(codigoGuardado != Configuraciones.SIN_PLATO_ELEGIDO){
            existe = true;
        }
        return existe;
    }

    @Override
    public int getCantidadInvitados(String nombreUsuarioID) {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        int cantidadInvitados;

        String[] columnsToReturn = {
                BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_CANTIDAD_INVITADOS,
        };

        String whereClause= BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_NOMBRE_USUARIO_ID + "=?";
        String[] whereValues = {nombreUsuarioID};

        try {
            Cursor cursor = db.query(
                    BaseDeDatosContract.UsuariosYAvisos.TABLE_NAME,  // The table to query
                    columnsToReturn,                               // The columns to return
                    whereClause,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            cursor.moveToFirst();
            cantidadInvitados = cursor.getInt(cursor.getColumnIndex(BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_CANTIDAD_INVITADOS));
            cursor.close();

        }finally {
            db.close();
        }
        return cantidadInvitados;
    }

    @Override
    public boolean usuarioTienePremio(String nombreUsuarioID) {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        int tienePremioInt;
        boolean tienePremio = false;

        String[] columnsToReturn = {
                BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_TIENE_PREMIO,
        };

        String whereClause= BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_NOMBRE_USUARIO_ID + "=?";
        String[] whereValues = {nombreUsuarioID};

        try {
            Cursor cursor = db.query(
                    BaseDeDatosContract.UsuariosYAvisos.TABLE_NAME,  // The table to query
                    columnsToReturn,                               // The columns to return
                    whereClause,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            cursor.moveToFirst();
            tienePremioInt = cursor.getInt(cursor.getColumnIndex(BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_TIENE_PREMIO));
            if(tienePremioInt == Configuraciones.TIENE_PREMIO){
                tienePremio = true;
            }
            cursor.close();

        }finally {
            db.close();
        }
        return tienePremio;
    }



    @Override
    public Cursor getUsuariosConPremioCursor() {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        Cursor cursor;

        String[] columnsToReturn = {
                BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_NOMBRE_USUARIO_ID,
        };

        String whereClause= BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_TIENE_PREMIO + "=?";

        String[] whereValues = {String.valueOf(Configuraciones.TIENE_PREMIO)};

        String sortOrder =
                BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_NOMBRE_USUARIO_ID + " ASC";


        cursor = db.query(
                BaseDeDatosContract.UsuariosYAvisos.TABLE_NAME,  // The table to query
                columnsToReturn,                                 // The columns to return
                whereClause,                                     // The columns for the WHERE clause
                whereValues,                                     // The values for the WHERE clause
                null,                                            // don't group the rows
                null,                                            // don't filter by row groups
                sortOrder                                             // The sort order
        );

        return cursor;
    }






    @Override
    public boolean enviarVoto(String nombreUsuarioID, boolean tienePremio, int codPlatoElegido, int cantInvitados) {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        boolean guardadoExitoso = true;

        int tienePremioInt;
        if (tienePremio){
            tienePremioInt = 1;
        }else{
            tienePremioInt = 0;
        }

        try{
            ContentValues values = new ContentValues();
            values.put(BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_TIENE_PREMIO, tienePremioInt);
            values.put(BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_COD_PLATO_ELEJIDO, codPlatoElegido);
            values.put(BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_CANTIDAD_INVITADOS, cantInvitados);

            String whereClause= BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_NOMBRE_USUARIO_ID + "=?";
            String[] whereValues = {nombreUsuarioID};

            db.update(
                    BaseDeDatosContract.UsuariosYAvisos.TABLE_NAME,
                    values,
                    whereClause,
                    whereValues
            );

        }catch(SQLException e){
            guardadoExitoso = false;
        }finally{
            db.close();
        }
        return guardadoExitoso;
    }

    @Override
    public boolean reiniciarVotacion() {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        boolean reiniciadoExitoso = true;

        try{
            ContentValues values = new ContentValues();
            values.put(BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_COD_PLATO_ELEJIDO, Configuraciones.SIN_PLATO_ELEGIDO);
            values.put(BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_CANTIDAD_INVITADOS, Configuraciones.CANTIDAD_INVITADOS_POR_DEFECTO);

            db.update(
                    BaseDeDatosContract.UsuariosYAvisos.TABLE_NAME,
                    values,
                    null,
                    null
            );

        }catch(SQLException e){
            reiniciadoExitoso = false;
        }finally{
            db.close();
        }
        return reiniciadoExitoso;
    }

    @Override
    public boolean reiniciarPremios() {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        boolean reiniciadoExitoso = true;

        try{
            ContentValues values = new ContentValues();
            values.put(BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_TIENE_PREMIO, Configuraciones.TIENE_PREMIO);

            db.update(
                    BaseDeDatosContract.UsuariosYAvisos.TABLE_NAME,
                    values,
                    null,
                    null
            );

        }catch(SQLException e){
            e.printStackTrace();
            reiniciadoExitoso = false;
        }finally{
            db.close();
        }
        return reiniciadoExitoso;
    }

    @Override
    public boolean actualizarPremios(){
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        boolean actualizadoExitoso = true;

        String[] columnsToReturn = {
                BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_NOMBRE_USUARIO_ID,
                BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_COD_PLATO_ELEJIDO,
        };

        String whereClause= BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_COD_PLATO_ELEJIDO + "=?";

        String[] whereValues = {String.valueOf(Configuraciones.SIN_PLATO_ELEGIDO)};


        try {
            Cursor cursor = db.query(
                    BaseDeDatosContract.UsuariosYAvisos.TABLE_NAME,  // The table to query
                    columnsToReturn,                                 // The columns to return
                    whereClause,                                     // The columns for the WHERE clause
                    whereValues,                                     // The values for the WHERE clause
                    null,                                            // don't group the rows
                    null,                                            // don't filter by row groups
                    null                                             // The sort order
            );

            String nombreUsuarioID;
            while(cursor.moveToNext()){
                nombreUsuarioID = cursor.getString(cursor.getColumnIndex(BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_NOMBRE_USUARIO_ID));

                ContentValues values = new ContentValues();
                values.put(BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_TIENE_PREMIO, Configuraciones.PERDIO_PREMIO);

                whereClause= BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_NOMBRE_USUARIO_ID + "=?";
                whereValues[0] = nombreUsuarioID;

                db.update(
                        BaseDeDatosContract.UsuariosYAvisos.TABLE_NAME,
                        values,
                        whereClause,
                        whereValues
                );
            }

            cursor.close();

        }catch (SQLException e){
            actualizadoExitoso = false;
        }finally {
            db.close();
        }
        return actualizadoExitoso;
    }

    @Override
    public int getCantidadNoComen(int dia, int mes, int anio) {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        int cantidadNoComen;

        String[] columnsToReturn = {
                BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_COD_PLATO_ELEJIDO,
        };

        String whereClause= BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_COD_PLATO_ELEJIDO + "=?";

        String[] whereValues = {String.valueOf(Configuraciones.NO_COME)};

        try {
            Cursor cursor = db.query(
                    BaseDeDatosContract.UsuariosYAvisos.TABLE_NAME,  // The table to query
                    columnsToReturn,                               // The columns to return
                    whereClause,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            cantidadNoComen = cursor.getCount();
            cursor.close();

        }finally {
            db.close();
        }
        return cantidadNoComen;
    }

    @Override
    public int getCantidadNoVotaron(int dia, int mes, int anio) {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        int cantidadNoVotaron;

        String[] columnsToReturn = {
                BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_COD_PLATO_ELEJIDO,
        };

        String whereClause= BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_COD_PLATO_ELEJIDO + "=?";

        String[] whereValues = {String.valueOf(Configuraciones.SIN_PLATO_ELEGIDO)};

        try {
            Cursor cursor = db.query(
                    BaseDeDatosContract.UsuariosYAvisos.TABLE_NAME,  // The table to query
                    columnsToReturn,                               // The columns to return
                    whereClause,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            cantidadNoVotaron = cursor.getCount();
            cursor.close();

        }finally {
            db.close();
        }
        return cantidadNoVotaron;
    }

    @Override
    public int getCantidadDeInvitadosTotales(int dia, int mes, int anio) {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        int cantidadInviadosTotales = 0;

        try {
            Cursor cursor = db.rawQuery("SELECT SUM(" + BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_CANTIDAD_INVITADOS
                            + ") FROM " + BaseDeDatosContract.UsuariosYAvisos.TABLE_NAME, null);

            if(cursor.moveToFirst()){
                cantidadInviadosTotales= cursor.getInt(0);
            }
            cursor.close();
        }finally {
            db.close();
        }
        return cantidadInviadosTotales;
    }

    @Override
    public boolean esAdmin(String nombreUsuarioID) {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        boolean esAdmin = false;

        String[] columnsToReturn = {
                BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_ES_ADMINISTRDOR,
        };

        String whereClause= BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_USUARIO_ID + "=?";

        String[] whereValues = {nombreUsuarioID};

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
            int esAdminInt = cursor.getInt(cursor.getColumnIndex(BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_ES_ADMINISTRDOR));
            if(esAdminInt == Configuraciones.ES_ADMIN ){
                esAdmin = true;
            }
            cursor.close();

        }finally {
            db.close();
        }
        return esAdmin;
    }
}

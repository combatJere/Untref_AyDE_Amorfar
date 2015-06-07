package Persitencia.DAOs.DAOs.Implementacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import Persitencia.BaseDeDatosContract;
import Persitencia.BaseDeDatosHelper;
import Persitencia.DAOs.MenuesDAO;

/**
 * Created by jeremias on 05/06/2015.
 */
public class MenuesDAOImpl implements MenuesDAO {

    private static MenuesDAOImpl instance;
    private BaseDeDatosHelper miDbHelper;

    private MenuesDAOImpl(Context context){
        miDbHelper = BaseDeDatosHelper.getInstance(context);
    }

    public static MenuesDAOImpl getInstance(Context context){
        if(instance == null){
            instance = new MenuesDAOImpl(context);
        }
        return instance;
    }


    public boolean existeAlmuerzo(int dia, int mes, int anio){
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        boolean existeAlmuerzo = false;

        String[] columnsToReturn = {
                BaseDeDatosContract.Almuerzo.COLUMN_NAME_DIA,
        };

        String whereClause= BaseDeDatosContract.Almuerzo.COLUMN_NAME_DIA + "=? " + "AND " + BaseDeDatosContract.Almuerzo.COLUMN_NAME_MES + "=? "
                + "AND " + BaseDeDatosContract.Almuerzo.COLUMN_NAME_ANIO + "=?";

        String[] whereValues = {String.valueOf(dia), String.valueOf(mes), String.valueOf(anio)};

        try {
            Cursor cursor = db.query(
                    BaseDeDatosContract.Almuerzo.TABLE_NAME,  // The table to query
                    columnsToReturn,                               // The columns to return
                    whereClause,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            //devuelve true si tiene elementos.
            if(cursor.moveToFirst()){
                existeAlmuerzo = true;
            }
            cursor.close();

        }finally {
            db.close();
        }

        return existeAlmuerzo;
    }

    public boolean guardarPlato(String nombreplato){
        boolean platoGuardadoConExito;
        SQLiteDatabase db = miDbHelper.getWritableDatabase();

        try{
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE, nombreplato);
            // Insert the new row, returning the primary key value of the new row
            db.insertOrThrow(BaseDeDatosContract.Platos.TABLE_NAME, "null", values);
            platoGuardadoConExito = true;

        }catch (SQLException e){
            platoGuardadoConExito = false;
        }finally{
            db.close();
        }
        return platoGuardadoConExito;
    }

    @Override
    public boolean existePlato(String nombrePlato) {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        boolean existeNombrePlato = false;

        String[] columnsToReturn = {
                BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE,
        };

        String whereClause= BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE + "=?";

        String[] whereValues = {String.valueOf(nombrePlato)};

        try {
            Cursor cursor = db.query(
                    BaseDeDatosContract.Platos.TABLE_NAME,  // The table to query
                    columnsToReturn,                               // The columns to return
                    whereClause,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            //devuelve true si tiene elementos.
            if(cursor.moveToFirst()){
                existeNombrePlato = true;
            }
            cursor.close();

        }finally {
            db.close();
        }

        return existeNombrePlato;
    }


    public Cursor getAllPlatosGuardadosCursor(){
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        Cursor cursor;

        String[] columnsToReturn = {
                BaseDeDatosContract.Platos.COLUMN_NAME_CODIGO_PLATO,
                BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE,
        };
        String sortOrder =
                BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE + " ASC";

        try {
            cursor = db.query(
                    BaseDeDatosContract.Platos.TABLE_NAME,  // The table to query
                    columnsToReturn,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

//            cursor.close(); //No cerrar porque lo tengo que pasar

        }finally {
//            db.close();
        }
        return cursor;
    }

    public Cursor getAllNombrePlatosCursor(){
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        Cursor cursor;

        String[] columnsToReturn = {
                BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE,
        };
        String sortOrder =
                BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE + " ASC";

        try {
            cursor = db.query(
                    BaseDeDatosContract.Platos.TABLE_NAME,  // The table to query
                    columnsToReturn,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

//            cursor.close(); //No cerrar porque lo tengo que pasar

        }finally {
//            db.close();
        }
        return cursor;
    }

//    public void cerrarDB(){
//
//    }
}

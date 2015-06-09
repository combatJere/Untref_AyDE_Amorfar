package Persitencia.DAOs.DAOs.Implementacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.prefs.Preferences;

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

//        String[] columnsToReturn = {
//                BaseDeDatosContract.Almuerzo.COLUMN_NAME_DIA, //CAMBIAR
//        };

        String whereClause= BaseDeDatosContract.Almuerzo.COLUMN_NAME_DIA + "=? " + "AND " + BaseDeDatosContract.Almuerzo.COLUMN_NAME_MES + "=? "
                + "AND " + BaseDeDatosContract.Almuerzo.COLUMN_NAME_ANIO + "=?";

        String[] whereValues = {String.valueOf(dia), String.valueOf(mes), String.valueOf(anio)};

        try {
            Cursor cursor = db.query(
                    BaseDeDatosContract.Almuerzo.TABLE_NAME,  // The table to query
                    null,                               // The columns to return
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

    @Override
    public Cursor getPlatosDelMenu(int dia, int mes, int anio) {
//      SQLiteDatabase db = miDbHelper.getReadableDatabase(); // si lo dejo aca, se cierra con el db.close de .getCodigosDePlatosDelMenu(dia, mes, anio);
        int[] codigosPlatosDelMenu = this.getCodigosDePlatosDelMenu(dia, mes, anio);
        int cantidadPlatos = codigosPlatosDelMenu.length; // number of IN arguments

        SQLiteDatabase db = miDbHelper.getReadableDatabase();

        String[] whereValues = new String[cantidadPlatos];

        StringBuilder inList = new StringBuilder(cantidadPlatos*2);
        for(int i=0;i<cantidadPlatos;i++){
            whereValues[i] = String.valueOf(codigosPlatosDelMenu[i]);
            if(i > 0) inList.append(","); inList.append("?"); }
//        cursor = contentResolver.query(CONTENT_URI, PROJECTION, "field IN ("+inList.toString()+")", whereValues, null);

        String[] columnsToReturn = {
                BaseDeDatosContract.Platos.COLUMN_NAME_CODIGO_PLATO,
                BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE,
        };

        String whereClause= "field IN ("+inList.toString()+")";


        Cursor cursor = db.query(
                BaseDeDatosContract.Platos.TABLE_NAME,  // The table to query
                columnsToReturn,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereValues,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        return cursor;
    }

    @Override
    public int[] getCodigosDePlatosDelMenu(int dia, int mes, int anio) {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        Cursor cursorIdPlatosDelMenu;
        int [] clavesPlatos;

        String[] columnsToReturn = {
                BaseDeDatosContract.MenuConPlatos.COLUM_NAME_CODIGO_PLATO,
        };

        String whereClause= BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_DIA + "=? " + "AND " + BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_MES + "=? "
                + "AND " + BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_ANIO + "=?";

        String[] whereValues = {String.valueOf(dia), String.valueOf(mes), String.valueOf(anio)};


        try{
            cursorIdPlatosDelMenu = db.query(
                    BaseDeDatosContract.MenuConPlatos.TABLE_NAME,  // The table to query
                    columnsToReturn,                               // The columns to return
                    whereClause,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            int cantidadPlatos =  cursorIdPlatosDelMenu.getCount();
            clavesPlatos = new int[cantidadPlatos];


//            for(int i = 0; cursorIdPlatosDelMenu.moveToNext(); i++){
//                clavesPlatos[i] = cursorIdPlatosDelMenu.getInt(cursorIdPlatosDelMenu.
//                        getColumnIndex(BaseDeDatosContract.MenuConPlatos.COLUM_NAME_CODIGO_PLATO));
//            }
            int contador = 0;
            while(cursorIdPlatosDelMenu.moveToNext()){
                clavesPlatos[contador] = cursorIdPlatosDelMenu.getInt(cursorIdPlatosDelMenu.
                        getColumnIndex(BaseDeDatosContract.MenuConPlatos.COLUM_NAME_CODIGO_PLATO));
                contador++;
            }
            cursorIdPlatosDelMenu.close();

        }finally {
            db.close();
        }

        return clavesPlatos;
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

        String[] whereValues = {nombrePlato};

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

//        try {
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

//        }finally {
//            db.close();
//        }
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

//        try {
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

//        }finally {
//            db.close();
//        }
        return cursor;
    }

    @Override
    public String getNombrePlato(int idPlatoElejido) {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        String nombrePlato = null;

        String[] columnsToReturn = {
                BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE,
        };

        String whereClause= BaseDeDatosContract.Platos.COLUMN_NAME_CODIGO_PLATO + "=?";

        String[] whereValues = {String.valueOf(idPlatoElejido)};

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
                nombrePlato = cursor.getString(cursor.getColumnIndex(BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE));
            }
            cursor.close();

        }finally {
            db.close();
        }

        return nombrePlato;
    }

//    public void cerrarDB(){
//
//    }
}

package Persitencia.DAOs.DAOs.Implementacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import Configuraciones.Configuraciones;
import Persitencia.BaseDeDatosContract;
import Persitencia.BaseDeDatosHelper;
import Persitencia.DAOs.MenuesDAO;
import Utilidades.TransformadorIntSetArray;

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


    @Override
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

        String[] columnsToReturn = {
                BaseDeDatosContract.Platos.COLUMN_NAME_CODIGO_PLATO,
                BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE,
        };

        String whereClause= BaseDeDatosContract.Platos.COLUMN_NAME_CODIGO_PLATO + " IN ("+inList.toString()+")";

        String sortOrder =
                BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE + " ASC";

        Cursor cursor = db.query(
                BaseDeDatosContract.Platos.TABLE_NAME,  // The table to query
                columnsToReturn,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereValues,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
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


    @Override
    public Set<Integer> getCodigosDePlatosDelMenuSet(int dia, int mes, int anio) {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        Cursor cursorIdPlatosDelMenu;
        Set<Integer> clavesPlatosADevolver = new HashSet<Integer>();

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

            int codigoEnMovimiento;
            while(cursorIdPlatosDelMenu.moveToNext()){

                codigoEnMovimiento = cursorIdPlatosDelMenu.getInt(cursorIdPlatosDelMenu.
                        getColumnIndex(BaseDeDatosContract.MenuConPlatos.COLUM_NAME_CODIGO_PLATO));
                clavesPlatosADevolver.add(codigoEnMovimiento);
            }

            cursorIdPlatosDelMenu.close();

        }finally {
            db.close();
        }
        return clavesPlatosADevolver;
    }

    @Override
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

    @Override
    public Cursor getPlatosGuardadosExcepto(Set<Integer> idPlatosEscluidos) {
        int[] codigosPlatosExcluidos = TransformadorIntSetArray.getInstance().setIntAArrayInt(idPlatosEscluidos);
        int cantidadPlatos = codigosPlatosExcluidos.length; // number of IN arguments

        SQLiteDatabase db = miDbHelper.getReadableDatabase();

        String[] whereValues = new String[cantidadPlatos];

        StringBuilder inList = new StringBuilder(cantidadPlatos*2);
        for(int i=0;i<cantidadPlatos;i++){
            whereValues[i] = String.valueOf(codigosPlatosExcluidos[i]);
            if(i > 0) inList.append(","); inList.append("?"); }

        String[] columnsToReturn = {
                BaseDeDatosContract.Platos.COLUMN_NAME_CODIGO_PLATO,
                BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE,
        };

        String whereClause= BaseDeDatosContract.Platos.COLUMN_NAME_CODIGO_PLATO + " NOT IN ("+inList.toString()+")";

        String sortOrder =
                BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE + " ASC";

        Cursor cursor = db.query(
                BaseDeDatosContract.Platos.TABLE_NAME,  // The table to query
                columnsToReturn,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereValues,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

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


    @Override
    public Cursor getPlatos(Set<Integer> idPlatosDelMenu) {

        int[] codigosPlatos = TransformadorIntSetArray.getInstance().setIntAArrayInt(idPlatosDelMenu);
        int cantidadPlatos = codigosPlatos.length; // number of IN arguments

        SQLiteDatabase db = miDbHelper.getReadableDatabase();

        String[] whereValues = new String[cantidadPlatos];

        StringBuilder inList = new StringBuilder(cantidadPlatos*2);
        for(int i=0;i<cantidadPlatos;i++){
            whereValues[i] = String.valueOf(codigosPlatos[i]);
            if(i > 0) inList.append(","); inList.append("?"); }

        String[] columnsToReturn = {
                BaseDeDatosContract.Platos.COLUMN_NAME_CODIGO_PLATO,
                BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE,
        };

        String whereClause= BaseDeDatosContract.Platos.COLUMN_NAME_CODIGO_PLATO + " IN ("+inList.toString()+")";

        String sortOrder =
                BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE + " ASC";

        Cursor cursor = db.query(
                BaseDeDatosContract.Platos.TABLE_NAME,  // The table to query
                columnsToReturn,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereValues,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return cursor;
    }


    @Override
    public int[] getHorarioAlmuerzo(int dia, int mes, int anio) {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        int[] horarioADevolver = null;

        String[] columnsToReturn = {
                BaseDeDatosContract.Almuerzo.COLUM_NAME_HORA,
                BaseDeDatosContract.Almuerzo.COLUM_NAME_MINUTOS,
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

            if(cursor.moveToFirst()){
                horarioADevolver = new int[2];
                horarioADevolver[0] = cursor.getInt(cursor.getColumnIndex(BaseDeDatosContract.Almuerzo.COLUM_NAME_HORA));
                horarioADevolver[1] = cursor.getInt(cursor.getColumnIndex(BaseDeDatosContract.Almuerzo.COLUM_NAME_MINUTOS));
            }
            cursor.close();

        }finally {
            db.close();
        }
        return horarioADevolver;
    }


    @Override
    public int getCantidadPlatos(int dia, int mes, int anio) {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        int cantidadDePlatos;

        String[] columnsToReturn = {
                BaseDeDatosContract.MenuConPlatos.COLUM_NAME_CODIGO_PLATO,
        };

        String whereClause= BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_DIA + "=? " + "AND " + BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_MES + "=? "
                + "AND " + BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_ANIO + "=?";

        String[] whereValues = {String.valueOf(dia), String.valueOf(mes), String.valueOf(anio)};

        try {
            Cursor cursor = db.query(
                    BaseDeDatosContract.MenuConPlatos.TABLE_NAME,  // The table to query
                    columnsToReturn,                               // The columns to return
                    whereClause,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            cantidadDePlatos = cursor.getCount();
            cursor.close();

        }finally {
            db.close();
        }
        return cantidadDePlatos;
    }


    @Override
    public boolean guardarAlmuerzo(int dia, int mes, int anio, int hora, int minutos, Set<Integer> idPlatos) {
        boolean guardadoExitoso = true;

        SQLiteDatabase db = miDbHelper.getWritableDatabase();

        try{
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(BaseDeDatosContract.Almuerzo.COLUMN_NAME_DIA, dia);
            values.put(BaseDeDatosContract.Almuerzo.COLUMN_NAME_MES, mes);
            values.put(BaseDeDatosContract.Almuerzo.COLUMN_NAME_ANIO, anio);
            values.put(BaseDeDatosContract.Almuerzo.COLUM_NAME_HORA, hora);
            values.put(BaseDeDatosContract.Almuerzo.COLUM_NAME_MINUTOS, minutos);

            // Insert the new row, returning the primary key value of the new row
            db.insertOrThrow(BaseDeDatosContract.Almuerzo.TABLE_NAME, "null", values);
//            guardadoExitoso = true;

        }catch (SQLException e) {
            guardadoExitoso = false;
        }

        try {
            if(guardadoExitoso) {
                Iterator<Integer> iterator = idPlatos.iterator();
                while (iterator.hasNext()) {
                    ContentValues values = new ContentValues();
                    values.put(BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_DIA, dia);
                    values.put(BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_MES, mes);
                    values.put(BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_ANIO, anio);
                    values.put(BaseDeDatosContract.MenuConPlatos.COLUM_NAME_CODIGO_PLATO, iterator.next());
                    db.insertOrThrow(BaseDeDatosContract.MenuConPlatos.TABLE_NAME, "null", values);
                }
            }
        }catch(SQLException e){
            //BORRAR LO GUARDADO EN LA TABLA ALMUERZOS!!!
            //BORRAR LO GUARDADO EN LA TABLA ALMUERZOS!!!
            guardadoExitoso = false;
        }finally{
            db.close();
        }

        return guardadoExitoso;
    }


    @Override
    public void eliminarAlmuerzo(int dia, int mes, int anio) {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        boolean eliminadoExitoso = false;

        String whereClause= BaseDeDatosContract.Almuerzo.COLUMN_NAME_DIA + "=? " + "AND " + BaseDeDatosContract.Almuerzo.COLUMN_NAME_MES + "=? "
                + "AND " + BaseDeDatosContract.Almuerzo.COLUMN_NAME_ANIO + "=?";

        String[] whereValues = {String.valueOf(dia), String.valueOf(mes), String.valueOf(anio)};

        try {
            db.delete(
                    BaseDeDatosContract.Almuerzo.TABLE_NAME,
                    whereClause,
                    whereValues
            );

            whereClause= BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_DIA + "=? " + "AND " + BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_MES + "=? "
                    + "AND " + BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_ANIO + "=?";

            db.delete(
                    BaseDeDatosContract.MenuConPlatos.TABLE_NAME,
                    whereClause,
                    whereValues
            );

        }finally {
            db.close();
        }
    }

    @Override
    public int getCantidadDelPlato(int idPlato) {
        SQLiteDatabase db = miDbHelper.getReadableDatabase();
        int cantidadDePedidos;

        String[] columnsToReturn = {
                BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_COD_PLATO_ELEJIDO,
        };

        String whereClause= BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_COD_PLATO_ELEJIDO + "=?";

        String[] whereValues = {String.valueOf(idPlato)};

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

            cantidadDePedidos = cursor.getCount();
            cursor.close();

        }finally {
            db.close();
        }
    return cantidadDePedidos;
    }



//    public void cerrarDB(){
//
//    }
}

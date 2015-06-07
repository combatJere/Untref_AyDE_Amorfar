package Persitencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manejador de la base de datos SQLite.
 * Created by jeremias on 01/06/2015.
 */
public class BaseDeDatosHelper extends SQLiteOpenHelper {

    private static BaseDeDatosHelper instance;

    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "amorfar.db";


    private BaseDeDatosHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static BaseDeDatosHelper getInstance(Context context){
        if(instance == null) {
            instance = new BaseDeDatosHelper(context);
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BaseDeDatosComandos.SQL_CREATE_USUARIOSYCLAVES);
        db.execSQL(BaseDeDatosComandos.SQL_CREATE_ALMUERZOS);
        db.execSQL(BaseDeDatosComandos.SQL_CREATE_MENU_CON_PLATOS);
        db.execSQL(BaseDeDatosComandos.SQL_CREATE_PLATOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BaseDeDatosComandos.SQL_DELETE_USUARIOSYCLAVES);
        db.execSQL(BaseDeDatosComandos.SQL_DELETE_ALMUERZOS);
        db.execSQL(BaseDeDatosComandos.SQL_DELETE_MENU_CON_PLATOS);
        db.execSQL(BaseDeDatosComandos.SQL_DELETE_PLATOS);
        onCreate(db);
    }
}

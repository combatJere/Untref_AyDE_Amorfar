package Persitencia;

/**
 * Define los comandos para el manejo de la base de datos.
 * Created by jeremias on 01/06/2015.
 */
public class BaseDeDatosComandos {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_USUARIOSYCLAVES = "CREATE TABLE " + BaseDeDatosContract.UsuariosYClave.TABLE_NAME + " (" +
            BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_USUARIO_ID + " TEXT PRIMARY KEY NOT NULL," +
            BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_CLAVE + " TEXT NOT NULL, " +
            BaseDeDatosContract.UsuariosYClave.COLUMN_NAME_ES_ADMINISTRDOR + " INTEGER NOT NULL"
            + " )";
    ;

    public static final String SQL_CREATE_USUARIOSYAVISOS = "CREATE TABLE " + BaseDeDatosContract.UsuariosYAvisos.TABLE_NAME + " (" +
            BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_NOMBRE_USUARIO_ID + " TEXT PRIMARY KEY NOT NULL," +
            BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_TIENE_PREMIO + " INTEGER NOT NULL, " +
            BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_COD_PLATO_ELEJIDO + " INTEGER NOT NULL, " +
            BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_CANTIDAD_INVITADOS + " INTEGER NOT NULL"
            + " )";

    public static final String SQL_CREATE_ALMUERZOS = "CREATE TABLE " + BaseDeDatosContract.Almuerzo.TABLE_NAME + " (" +
            BaseDeDatosContract.Almuerzo.COLUMN_NAME_DIA + " INTEGER NOT NULL, " +
            BaseDeDatosContract.Almuerzo.COLUMN_NAME_MES + " INTEGER NOT NULL, " +
            BaseDeDatosContract.Almuerzo.COLUMN_NAME_ANIO + " INTEGER NOT NULL, " +
            BaseDeDatosContract.Almuerzo.COLUM_NAME_HORA + " INTEGER NOT NULL, " +
            BaseDeDatosContract.Almuerzo.COLUM_NAME_MINUTOS + " INTEGER NOT NULL, " +
            "PRIMARY KEY (" + BaseDeDatosContract.Almuerzo.COLUMN_NAME_DIA + ", " + BaseDeDatosContract.Almuerzo.COLUMN_NAME_MES + ", " + BaseDeDatosContract.Almuerzo.COLUMN_NAME_ANIO + "))";

    public static final String SQL_CREATE_MENU_CON_PLATOS = "CREATE TABLE " + BaseDeDatosContract.MenuConPlatos.TABLE_NAME + " (" +
            BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_DIA + " INTEGER NOT NULL," +
            BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_MES + " INTEGER NOT NULL," +
            BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_ANIO + " INTEGER NOT NULL," +
            BaseDeDatosContract.MenuConPlatos.COLUM_NAME_CODIGO_PLATO + " INTEGER NOT NULL, " +
            "PRIMARY KEY (" + BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_DIA + ", " + BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_MES + ", " + BaseDeDatosContract.MenuConPlatos.COLUMN_NAME_ANIO
                + ", " + BaseDeDatosContract.MenuConPlatos.COLUM_NAME_CODIGO_PLATO + "))";

    public static final String SQL_CREATE_PLATOS = "CREATE TABLE " + BaseDeDatosContract.Platos.TABLE_NAME + " (" +
            BaseDeDatosContract.Platos.COLUMN_NAME_CODIGO_PLATO + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE + " TEXT NOT NULL" +
            " )";

    public static final String SQL_DELETE_USUARIOSYCLAVES =
            "DROP TABLE IF EXISTS " + BaseDeDatosContract.UsuariosYClave.TABLE_NAME;

    public static final String SQL_DELETE_USUARIOSYAVISOS =
            "DROP TABLE IF EXISTS " + BaseDeDatosContract.UsuariosYAvisos.TABLE_NAME;

    public static final String SQL_DELETE_ALMUERZOS =
            "DROP TABLE IF EXISTS " + BaseDeDatosContract.Almuerzo.TABLE_NAME;

    public static final String SQL_DELETE_MENU_CON_PLATOS =
            "DROP TABLE IF EXISTS " + BaseDeDatosContract.MenuConPlatos.TABLE_NAME;

    public static final String SQL_DELETE_PLATOS =
            "DROP TABLE IF EXISTS " + BaseDeDatosContract.Platos.TABLE_NAME;

}

package Persitencia;

import android.provider.BaseColumns;

/**
 * Define como seran la tablas.
 * Created by jeremias on 01/06/2015.
 */
public class BaseDeDatosContract {
    public BaseDeDatosContract(){}

    /* Inner class that defines the table contents */
    public static abstract class UsuariosYClave implements BaseColumns {
        public static final String TABLE_NAME = "usuariosYclave";
        public static final String COLUMN_NAME_USUARIO_ID = "usuario";
        public static final String COLUMN_NAME_CLAVE = "clave";
        public static final String COLUMN_NAME_ES_ADMINISTRDOR = "es_administrador";
    }

    public static abstract class UsuariosYAvisos implements BaseColumns{
        public static final String TABLE_NAME = "usuariosYavisos";
        public static final String COLUMN_NAME_NOMBRE_USUARIO_ID = "_id";
        public static final String COLUMN_NAME_TIENE_PREMIO = "tienePremio";
        public static final String COLUMN_NAME_COD_PLATO_ELEJIDO = "codigoPlatoElejido";
        public static final String COLUMN_NAME_CANTIDAD_INVITADOS = "cantidadInvitados";
    }

    public static abstract class Almuerzo implements BaseColumns {
        public static final String TABLE_NAME = "almuerzos";
        public static final String COLUMN_NAME_DIA = "dia";
        public static final String COLUMN_NAME_MES = "mes";
        public static final String COLUMN_NAME_ANIO = "anio";
        public static final String COLUM_NAME_HORA = "hora";
        public static final String COLUM_NAME_MINUTOS = "minutos";
    }

    public static abstract class MenuConPlatos implements BaseColumns {
        public static final String TABLE_NAME = "menu_con_platos";
        public static final String COLUMN_NAME_DIA = "dia";
        public static final String COLUMN_NAME_MES = "mes";
        public static final String COLUMN_NAME_ANIO = "anio";
        public static final String COLUM_NAME_CODIGO_PLATO = "codigo_platos";
    }

    public static abstract class Platos implements BaseColumns {
        public static final String TABLE_NAME = "platos";
        public static final String COLUMN_NAME_CODIGO_PLATO = "_id";
        public static final String COLUM_NAME_NOMBRE = "nombre";
    }

}

package inversiondecontrol;

import android.content.Context;

import Persitencia.DAOs.DAOs.Implementacion.MenuesDAOImpl;
import Persitencia.DAOs.DAOs.Implementacion.UsuariosDAOImpl;
import Persitencia.DAOs.MenuesDAO;
import Persitencia.DAOs.UsuariosDAO;

/**
 * Me permite cambiar facilmente algunos parametro de como trabaja la aplicacion.
 * Created by jeremias on 06/06/2015.
 */
public class ServiceLocator {

    private static ServiceLocator instance;
    private static MenuesDAO menuesDAO;
    private static UsuariosDAO usuariosDAO;


    private ServiceLocator(){
        //vacio
    }

    public static ServiceLocator getInstance(){
        if(instance == null){
            instance = new ServiceLocator();
        }
        return instance;
    }


    public MenuesDAO getMenuesDao(Context context){
        if(menuesDAO == null){
            menuesDAO = MenuesDAOImpl.getInstance(context); //Si cambia la conexion, Esta mal que el MenuesDAOImpl tambien sea Singleton?
        }
        return menuesDAO;
    }


    public UsuariosDAO getUsuariosDAO(Context context){
        if(usuariosDAO == null){
            usuariosDAO = UsuariosDAOImpl.getInstance(context); //Si cambia la conexion. Esta mal que el UsuariosDAOImpl tambien sea Singleton?
        }
        return usuariosDAO;
    }

}

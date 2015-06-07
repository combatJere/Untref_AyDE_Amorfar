package Persitencia.DAOs;

import Persitencia.DAOs.DAOs.Implementacion.UsuariosDAOImpl;

/**
 * Created by jeremias on 05/06/2015.
 */
public interface UsuariosDAO {

    /**
     * Recive los datos de un nuevo usuario, previamente validados, y lo guarda en la BDD.
     * CUIDADO: los datos deben ser previamente validados.
     * @param nombreUsuario
     * @param clave
     * @param esAdministrador
     * @return true si fue guardado exitosamente.
     */
    public boolean guardarUsuario(String nombreUsuario, String clave, int esAdministrador);

    /**
     * CUIDADO: El nombreUsuario ingresado debe existir.
     * @param nombreUsuario
     * @return la clave correspondiente al usuario.
     */
    public String obtenerClaveUsuario(String nombreUsuario);


    /**
     * @param nombreUsuario
     * @return true si ya existe un usuario con ese nombre en la BDD.
     */
    public boolean usuarioExiste(String nombreUsuario);

}

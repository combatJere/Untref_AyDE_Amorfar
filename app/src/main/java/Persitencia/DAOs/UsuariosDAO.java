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

    /**
     *
     * @param nombreUsuarioID
     * @return el id del plato elegido por el usuario.
     */
    public int getIdPlatoElegido(String nombreUsuarioID);

    /**
     * @param nombreUsuarioID
     * @return true, si el usuario ya tiene voto registrado para ese almuerzo.
     */
    public boolean platoYaElegido(String nombreUsuarioID);

    /**
     * @param nombreUsuarioID
     * @return la cantidad de invitados.
     */
    public int getCantidadInvitados(String nombreUsuarioID);

    /**
     * @param nombreUsuarioID
     * @return true si el usuario conserva el premio
     */
    public boolean usuarioTienePremio(String nombreUsuarioID);

    /**
     * guarda una votacion (eleccion de plato) en la BDD
     * @param nombreUsuarioID
     * @param tienePremio
     * @param idPlatoelejido
     * @param cantInvitados
     * @return
     */
    public boolean enviarVoto(String nombreUsuarioID, boolean tienePremio, int idPlatoelejido, int cantInvitados);

    /**
     * reinicia las votaciones, platoElejido y cantidad de Invitados
     * (ideado para cuando empieza un nuevo dia)
     * @return
     */
    public boolean reiniciarVotacion();

    /**
     * Asigna a todos los usuarios el premio en verdadero
     * (ideado para cuando empieza un nuevo ciclo)
     * @return
     */
    public boolean reinicairPremios();

    /**
     * usado para calcular si conserva el premio, a partir de la ultima votacion realizada.
     * @return
     */
    public boolean actualizarPremios();
}

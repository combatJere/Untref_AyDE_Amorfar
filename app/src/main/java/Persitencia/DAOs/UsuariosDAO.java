package Persitencia.DAOs;

import android.database.Cursor;

import Persitencia.DAOs.DAOs.Implementacion.UsuariosDAOImpl;

/**
 * Created by jeremias on 05/06/2015.
 */
public interface UsuariosDAO {


    /**
     * Guarda los datos obtenidos, junto a datos por defecto en ambas tablas de usuario
     * En caso de fallar el guardado en la segunda tabla, elimina los datos guardados en la primer tabla.
     * CUIDADO: los datos deben estar previamente validados.
     * @param nombreUsuario
     * @param clave
     * @param esAdministrador
     * @return
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
     * @return Cursor con todos los nombres de los usuarios que tienen premios hasta el momento.
     */
    public Cursor getUsuariosConPremioCursor();


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
    public boolean reiniciarPremios();


    /**
     * usado para calcular si conserva el premio, a partir de la ultima votacion realizada.
     * @return
     */
    public boolean actualizarPremios();


    /**
     *
     * @param dia
     * @param mes
     * @param anio
     * @return
     */
    public int getCantidadNoComen(int dia, int mes, int anio);


    /**
     *
     * @param dia
     * @param mes
     * @param anio
     * @return
     */
    public int getCantidadNoVotaron(int dia, int mes, int anio);


    /**
     *
     * @param dia
     * @param mes
     * @param anio
     * @return
     */
    public int getCantidadDeInvitadosTotales(int dia, int mes, int anio);


    /**
     *
     * @param nombreUsuarioID
     * @return true si el usuario es admin.
     */
    public boolean esAdmin(String nombreUsuarioID);

}

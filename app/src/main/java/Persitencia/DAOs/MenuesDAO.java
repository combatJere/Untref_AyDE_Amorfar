package Persitencia.DAOs;

import android.database.Cursor;

import java.util.Set;

/**
 * Created by jeremias on 05/06/2015.
 */
public interface MenuesDAO {

    /**
     * Recive los valores correspondientes a la fecha de un menu. (id de un menu)
     * @param dia
     * @param mes
     * @param anio
     * @return true si el almuerzo correspondiente a ese dia ya fue creado.
     */
    public boolean existeAlmuerzo(int dia, int mes, int anio);

    /**
     * Recive los valores correspondientes a la fecha de un menu. (id de un menu)
     * @param dia
     * @param mes
     * @param anio
     * @return un cursor conteniendo los platos correspondientes a ese almuerzo.
     */
    public Cursor getPlatosDelMenu(int dia, int mes, int anio);

    /**
     * Recive los valores correspondientes a la fecha de un menu (id de un menu)
     * @param dia
     * @param mes
     * @param anio
     * @return un array con lo codigos de los platos del menu correspondiente.
     */
    public int[] getCodigosDePlatosDelMenu(int dia, int mes, int anio);


    /**
     * Recive los valores correspondientes a la fecha de un menu (id de un menu)
     * @param dia
     * @param mes
     * @param anio
     * @return un Set<Integer> con lo codigos de los platos del menu correspondiente.
     */
    public Set<Integer> getCodigosDePlatosDelMenuSet(int dia, int mes, int anio);

    /**
     * Recive el nombre de un plato y lo guarda en la BDD.
     * CUIDADO: No comprueba que un plato con ese nombre ya exista.
     * @param nombreplato
     * @return true si fue guardado en la BDD exitosamente.
     */
    public boolean guardarPlato(String nombreplato);

    /**
     * @param nombrePlato
     * @return true si ya existe un plato con ese nombre en la BDD.
     */
    public boolean existePlato(String nombrePlato);

    /**
     * CUIDADO: Recordar cerrar el cursor al finalizar su uso.
     * @return un Cursor con todos los platos (id, nombrePlato) guardados.
     */
    public Cursor getAllPlatosGuardadosCursor();

    /**
     * CUIDADO: Recordar cerrar el cursor al finalizar su uso.
     * @param idPlatosEscluidos conjunto de id de los platos que no se desean obtener
     * @return un cusor con todos los platos, excepto los no deseados.
     */
    public Cursor getPlatosGuardadosExcepto(Set<Integer> idPlatosEscluidos);

    /**
     * CUIDADO: Recordar cerrer el cursor al finalizar su uso.
     * @return un Cursor con el nombre de todos los platos guardados.
     */
    public Cursor getAllNombrePlatosCursor();

    /**
     * CUIDADO: El plato debe existir en la BDD.
     * @param idPlatoElejido recibe el id correspondiente a un plato.
     * @return el nobre del plato.
     */
    public String getNombrePlato(int idPlatoElejido);

    /**
     * Devuelve un Cursor con los platos correspondientes a los id solicitados;
     * @param idPlatosDelMenu los id de los platos que se quieren obtener
     * @return un Cursor con los platos correspondientes.
     */
    public Cursor getPlatos(Set<Integer> idPlatosDelMenu);

    /**
     * Recive los valores correspondientes a la fecha de un menu. (id de un menu)
     * @param dia
     * @param mes
     * @param anio
     * @return un Array de Int de dos posiciones de la forma [HORA; MINUTOS]
     */
    public int[] getHorarioAlmuerzo(int dia, int mes, int anio);

    /**
     * Recive los valores correspondientes a la fecha de un menu. (id de un menu)
     * @param dia
     * @param mes
     * @param anio
     * @return Un int representando la cantidad de platos del almuerzo correspondiente.
     */
    public int getCantidadPlatos(int dia, int mes, int anio);

    /**
     * Recivetodos los datos correspondientes a un nuevo almuerzo y lo guarda en la BDD.
     * @param dia
     * @param mes
     * @param anio
     * @param hora
     * @param minutos
     * @param idPlatos
     * @return
     */
    public boolean guardarAlmuerzo(int dia, int mes, int anio, int hora, int minutos, Set<Integer> idPlatos);

    /**
     * Elimina un Almuerzo completo de la BDD.
     * @param dia
     * @param mes
     * @param anio
     * @return
     */
    public void eliminarAlmuerzo(int dia, int mes, int anio);

    /**
     * @param idPlato id del plato que se quiere saber la cantidad.
     * @return cantidad de veces que fue elegido para el almuerzo actual.
     */
    public int getCantidadDelPlato(int idPlato);

}

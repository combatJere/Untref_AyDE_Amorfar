package Persitencia.DAOs;

import android.database.Cursor;

/**
 * Created by jeremias on 05/06/2015.
 */
public interface MenuesDAO {

    /**
     * Recive los parametros correspondientes a la PK de la tabla Almuerzos.
     * @param dia
     * @param mes
     * @param anio
     * @return true si el almuerzo correspondiente a ese dia ya fue creado.
     */
    public boolean existeAlmuerzo(int dia, int mes, int anio);

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
     * CUIDADO: Recordar cerrer el cursor al finalizar su uso.
     * @return un Cursor con el nombre de todos los platos guardados.
     */
    public Cursor getAllNombrePlatosCursor();
//    public void cerrarDB();

}

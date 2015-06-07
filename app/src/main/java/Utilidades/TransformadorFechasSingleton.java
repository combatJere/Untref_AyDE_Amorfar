package Utilidades;

/**
 * Created by jeremias on 03/06/2015.
 */
public class TransformadorFechasSingleton {

    private static TransformadorFechasSingleton miTransformadorFechas;

    private TransformadorFechasSingleton(){
        //Constructor vacio.
    }

    public static TransformadorFechasSingleton getInstance(){
        if (miTransformadorFechas == null){
            miTransformadorFechas = new TransformadorFechasSingleton();
        }
        return miTransformadorFechas;
    }

    /**
     * Recive en forma de Int los elementos que conforman una fecha.
     * Devuelve la fecha en String, del tipo "DD/MM/AAAA"
     * @param dia
     * @param mes
     * @param anio
     * @return
     */
    public String getFechaEnTexto(int dia, int mes, int anio){
        return dia + "/" + mes +"/" + anio;
    }
}

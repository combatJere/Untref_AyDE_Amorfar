package Utilidades;

/**
 * Created by jeremias on 03/06/2015.
 */
public class TransformadorHorariosSingleton {
    private static TransformadorHorariosSingleton miTransformadorHorarios;

    private TransformadorHorariosSingleton(){
        //Constructor vacio.
    }

    public static TransformadorHorariosSingleton getInstance(){
        if (miTransformadorHorarios == null){
            miTransformadorHorarios = new TransformadorHorariosSingleton();
        }
        return miTransformadorHorarios;
    }

    /**
     * Recive en forma de Int lo elementos que conforman una hora.
     * Devuelve la hora en String, del tipo "HH:MM"
     * @param hora
     * @param minutos
     * @return
     */
    public String getHorarioEnTexto(int hora, int minutos){
         return this.getHoraEnTexto(hora) + ":" + this.getMinutosEnTexto(minutos);
    }

    /**
     * Recive un Int representando una hora.
     * Se asegura de que siempre tenga dos digitos. "HH"
     * @param hora
     * @return
     */
    public String getHoraEnTexto(int hora){
        String horaEnTexto;
        if(hora < 10){
            horaEnTexto = "0" + hora;
        }else{
            horaEnTexto = "" + hora;
        }
        return horaEnTexto;
    }

    /**
     * Recive un Int representando los minutos.
     * Se asegura de que siempre tenga dos digitos. "MM"
     * @param minutos
     * @return
     */
    public String getMinutosEnTexto(int minutos){
        String minutosEnTexto;
        if(minutos<10){
            minutosEnTexto = "0" + minutos;
        }else{
            minutosEnTexto = "" +minutos;
        }
        return minutosEnTexto;
    }
}

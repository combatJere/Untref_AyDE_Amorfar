package Utilidades;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by jeremias on 09/06/2015.
 */
public class TransformadorIntSetArray {

    private static TransformadorIntSetArray miTransformadorIntSetArray;

    private TransformadorIntSetArray() {
        //Vacio
    }

    public static TransformadorIntSetArray getInstance() {
        if (miTransformadorIntSetArray == null) {
            miTransformadorIntSetArray = new TransformadorIntSetArray();
        }
        return miTransformadorIntSetArray;
    }

    /**
     * Transforma un conjunto de Integers en un Array de Integers
     * @param conjuntoATransformar
     * @return El Array de Integers.
     */
    public int[] setIntAArrayInt(Set<Integer> conjuntoATransformar) {
        int tamanio= conjuntoATransformar.size();
        int [] conjuntoTransformado = new int[tamanio];

        Iterator<Integer> it = conjuntoATransformar.iterator();
        int cont = 0;
        while(it.hasNext()){
            conjuntoTransformado[cont] = it.next();
            cont++;
        }

        return conjuntoTransformado;
    }

    public Set<Integer> arrayIntASetInt(int[] arrayATransformar){
        Set<Integer> conjuntoADevolver = new HashSet<Integer>();
        int tamanioArray = arrayATransformar.length;

        for(int i = 0; i < tamanioArray; i++){
            int elementoEnMovimiento = arrayATransformar[i];
            conjuntoADevolver.add(elementoEnMovimiento);
        }

        return conjuntoADevolver;
    }
}
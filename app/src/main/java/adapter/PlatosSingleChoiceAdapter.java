package adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.altosoftuntref.amorfar.R;

import java.util.Set;

import Persitencia.BaseDeDatosContract;
import layouts.customs.GridViewItem;

/**
 * Created by jeremias on 11/06/2015.
 */
public class PlatosSingleChoiceAdapter extends CursorAdapter{

    int platoElejido;


    /**
     * Conoce cuales son los platos que deben resaltarse.
     * @param context
     * @param c
     * @param flags
     * @param platoElegido El conjunto de platos que deben ser resaltados.
     */
    public PlatosSingleChoiceAdapter(Context context, Cursor c, int flags, int platoElegido) {
        super(context, c, flags);
        this.platoElejido = platoElegido;
    }


    /**
     * @param nuevoPlatoElegido el id del plato a resaltarse.
     */
    public void cambiarPlatoElegido(int nuevoPlatoElegido){
        this.platoElejido = nuevoPlatoElegido;
    }


    /**
     * @Edu Solo inflar la vista (View) y devolverla.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.plato_gridview_element, parent, false);
        return v;
    }


    /**
     * @Edu Instanciar los elementos de la vista (View)
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String nombrePlato;
        TextView nombrePlatoTextView = (TextView) view.findViewById(R.id.textView_gridView_nombrePlato);
        int idPlato = cursor.getInt(cursor.getColumnIndex(BaseDeDatosContract.Platos.COLUMN_NAME_CODIGO_PLATO));
        nombrePlato = cursor.getString(cursor.getColumnIndex(BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE));
        nombrePlatoTextView.setText(nombrePlato);

        GridViewItem itemElegido = (GridViewItem) view;
        FrameLayout centroBoton = (FrameLayout) itemElegido.findViewById(R.id.centro_boton);

        //logica para resaltar los platos correspondientes, los elejidos.
        if(platoElejido == idPlato){
            itemElegido.setBackgroundColor(view.getResources().getColor(R.color.gridViewItem_background_checked));
            centroBoton.setBackgroundColor(view.getResources().getColor(R.color.gridViewItemCentro_background_checked));
        }else{
            itemElegido.setBackgroundColor(view.getResources().getColor(R.color.gridViewItem_background));
            centroBoton.setBackgroundColor(view.getResources().getColor(R.color.gridViewItemCentro_background));
        }
    }

}

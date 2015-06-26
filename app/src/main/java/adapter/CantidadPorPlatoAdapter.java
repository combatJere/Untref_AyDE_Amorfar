package adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.altosoftuntref.amorfar.R;

import Persitencia.BaseDeDatosContract;
import inversiondecontrol.ServiceLocator;

/**
 * Created by jeremias on 20/06/2015.
 */
public class CantidadPorPlatoAdapter extends CursorAdapter {

    public CantidadPorPlatoAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    /**
     * @Edu Solo inflar la vista (View) y devolverla.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.plato_cantidad_listview_element, parent, false);
        return v;
    }


    /**
     * @Edu Instanciar los elementos de la vista (View)
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int codigoPlato = cursor.getInt(cursor.getColumnIndex(BaseDeDatosContract.Platos.COLUMN_NAME_CODIGO_PLATO));
        String nombrePlato = cursor.getString(cursor.getColumnIndex(BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE));
        int cantidadQueComenPlato = ServiceLocator.getInstance().getMenuesDao(context).getCantidadDelPlato(codigoPlato);

        TextView textViewNombrePlato = (TextView) view.findViewById(R.id.textView_platoCantidadListItem_nombrePlato);
        TextView textViewCantidadDelPlato = (TextView) view.findViewById(R.id.textView_platoCantidadListItem_cantidadDelPlato);

//        String nombrePlatoMayusc = nombrePlato.substring(0,1).toUpperCase() + nombrePlato.substring(1);
        String nombrePlatoMayusc = nombrePlato.toUpperCase();

        textViewNombrePlato.setText(nombrePlatoMayusc);
        textViewCantidadDelPlato.setText(String.valueOf(cantidadQueComenPlato));
    }
}

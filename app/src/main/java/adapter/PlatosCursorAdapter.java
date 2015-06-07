package adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.altosoftuntref.amorfar.R;

import Persitencia.BaseDeDatosContract;

/**
 * Created by jeremias on 05/06/2015.
 */
public class PlatosCursorAdapter extends CursorAdapter {

    public PlatosCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.plato_gridview_element, parent, false);
//        cursor.getString(cursor.getColumnIndex(BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE));
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String nombrePlato;
        TextView nombrePlatoTextView = (TextView) view.findViewById(R.id.textView_gridView_nombrePlato);
        nombrePlato = cursor.getString(cursor.getColumnIndex(BaseDeDatosContract.Platos.COLUM_NAME_NOMBRE));

        nombrePlatoTextView.setText(nombrePlato);
        //asignarle el nombre del plato traido por el cursor
    }
}

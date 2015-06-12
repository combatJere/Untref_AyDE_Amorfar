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
 * Created by jeremias on 12/06/2015.
 */
public class ComensalesConPremioAdapter extends CursorAdapter {

    public ComensalesConPremioAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.persona_premio_gridview_element, parent, false);
        return v;
    }

    /**
     * @Edu Instanciar los elementos de la vista (View)
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String nombreUsuario;
        TextView nombreUsuarioTextView = (TextView) view.findViewById(R.id.textView_personaPremioGridView_nombreUsuarioID);
        nombreUsuario = cursor.getString(cursor.getColumnIndex(BaseDeDatosContract.UsuariosYAvisos.COLUMN_NAME_NOMBRE_USUARIO_ID));
        nombreUsuarioTextView.setText(nombreUsuario);
    }

}

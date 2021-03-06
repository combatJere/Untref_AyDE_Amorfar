package dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import com.altosoftuntref.amorfar.R;


/**
 * Created by jeremias on 12/06/2015.
 */
public class SobreAltosoftDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.sobre_altosoft_dialog, null))
                .setNegativeButton(R.string.cerrar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Nada
                    }
                });
        return builder.create();
    }

}

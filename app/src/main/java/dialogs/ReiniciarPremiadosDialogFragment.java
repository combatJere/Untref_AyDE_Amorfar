package dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.altosoftuntref.amorfar.R;

/**
 * Created by jeremias on 12/06/2015.
 */
public class ReiniciarPremiadosDialogFragment extends DialogFragment {

    private ReiniciarPremiadosDialogListener reiniciarPremiadosListener;

    public interface ReiniciarPremiadosDialogListener {
        public void onCantidadPlatosConfirmarClick();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.desea_reiniciar_premiados)
                .setPositiveButton(R.string.reiniciar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        reiniciarPremiadosListener.onCantidadPlatosConfirmarClick();
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the TimePickerFragmentListener interface. If not, it throws an exception
        try {
            reiniciarPremiadosListener = (ReiniciarPremiadosDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TimePickerFragmentListenerJer");
        }
    }
}
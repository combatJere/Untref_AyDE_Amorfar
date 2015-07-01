package dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import com.altosoftuntref.amorfar.R;


/**
 * Created by jeremias on 30/06/2015.
 */
public class ConInvitadosDialog extends DialogFragment {

    private ConInvitadosDialogListener cantidadPlatosListener;

    public interface ConInvitadosDialogListener {
        public void onConInvitadosConfirmarClick();
        public void onConInvitadosCancelarClick();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.advertencia_con_invitados_dialog, null))
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cantidadPlatosListener.onConInvitadosCancelarClick();
                    }
                })
                .setPositiveButton(R.string.confirmar_minusc, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        cantidadPlatosListener.onConInvitadosConfirmarClick();
                    }
                });
        return builder.create();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the TimePickerFragmentListener interface. If not, it throws an exception
        try {
            cantidadPlatosListener = (ConInvitadosDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ConInvitadosDialogListener");
        }
    }
}
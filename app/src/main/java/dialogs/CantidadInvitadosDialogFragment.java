package dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.altosoftuntref.amorfar.ElejirMenuActivity;
import com.altosoftuntref.amorfar.R;

import Configuraciones.Configuraciones;

/**
 * Dialog que permite al usuario elegir la cantidad de invitados que se
 * con los que asistiran. por medio de un NumberPicker.
 * Created by jeremias on 04/06/2015.
 */
public class CantidadInvitadosDialogFragment extends DialogFragment {

    NumberPicker numberPicker;
    private CantidadInvitadosDialogListener cantidadInvitadosListener;

    public interface CantidadInvitadosDialogListener {
        public void onCantidadInvitadosConfirmarClick(int cantidadInvitados);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        int cantidadInvitadosRecividos = getArguments().getInt(ElejirMenuActivity.EXTRA_CANTIDAD_INVITADOS);

        View numberPickerLayout = inflater.inflate(R.layout.dialog_cantidad_invitados_seleccion, null);
        numberPicker = (NumberPicker) numberPickerLayout.findViewById(R.id.numberPicker_cantidadInvitados);
        numberPicker.setMaxValue(Configuraciones.CANTIDAD_INVITADOS_MAX);
        numberPicker.setMinValue(Configuraciones.CANTIDAD_INVITADOS_MIN);
        numberPicker.setValue(cantidadInvitadosRecividos);

        builder.setView(numberPickerLayout) //Le tengo que pasar el mismo layout que ya cree.
                .setMessage(R.string.cantidad_de_invitados)
                .setPositiveButton(R.string.confirmar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cantidadInvitadosListener.onCantidadInvitadosConfirmarClick(numberPicker.getValue());
                        //VER SI SE EJECUTA DOS VECES COMO EN EL TIMEPICKERFRAGMENT DIALOG
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
            cantidadInvitadosListener = (CantidadInvitadosDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TimePickerFragmentListenerJer");
        }
    }
}
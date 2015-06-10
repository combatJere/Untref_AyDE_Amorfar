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
import android.widget.Toast;

import com.altosoftuntref.amorfar.R;

/**
 * Dialog que permite al usuario (administrador) elegir la cantidad de platos que se
 * ofrecerean en ese menu. por medio de un NumberPicker.
 * Created by jeremias on 04/06/2015.
 */
public class CantidadPlatosDialogFragment extends DialogFragment {

    NumberPicker numberPicker;
    private CantidadPlatosDialogListener cantidadPlatosListener;

    public interface CantidadPlatosDialogListener {
        public void onCantidadPlatosConfirmarClick(int cantidadPlatos);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View numberPickerLayout = inflater.inflate(R.layout.dialog_cantidad_comidas_seleccion, null);
        numberPicker = (NumberPicker)numberPickerLayout.findViewById(R.id.numberPicker_cantidadComidas);
        numberPicker.setMaxValue(5);
        numberPicker.setMinValue(1);
        numberPicker.setValue(2);

        builder.setView(numberPickerLayout) //Le tengo que pasar el mismo layout que ya cree.
                .setMessage(R.string.cantidad_de_platos)
                .setPositiveButton(R.string.confirmar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cantidadPlatosListener.onCantidadPlatosConfirmarClick(numberPicker.getValue());
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
            cantidadPlatosListener = (CantidadPlatosDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TimePickerFragmentListenerJer");
        }
    }

}

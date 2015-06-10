package dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.altosoftuntref.amorfar.R;

/**
 * Dialog que permite al usuario (administrador) crear un nuevo plato, y que quede guardado
 * en la BDD. Solo requiere que se ingrese el nombre del plato por un EditText
 * Created by jeremias on 06/06/2015.
 */
public class NombrePlatoDialogFragment extends DialogFragment {

    EditText editText;
    private NuevoPlatoDialogListener nuevoPlatoAAgregarListener;

    public interface NuevoPlatoDialogListener {
        public void onConfirmarDialogClick(String nombrePlato);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View nuevoPlatoLayout = inflater.inflate(R.layout.dialog_nuevo_plato, null);
        editText = (EditText)nuevoPlatoLayout.findViewById(R.id.editText_dialogNuevoPlato_nombrePlato);

        builder.setView(nuevoPlatoLayout) //Le tengo que pasar el mismo layout que ya cree.
                .setMessage(R.string.nuevo_plato)
                .setPositiveButton(R.string.crear, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String nombreIngresado = editText.getText().toString();
                        nuevoPlatoAAgregarListener.onConfirmarDialogClick(nombreIngresado);
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
            nuevoPlatoAAgregarListener = (NuevoPlatoDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TimePickerFragmentListenerJer");
        }
    }


}

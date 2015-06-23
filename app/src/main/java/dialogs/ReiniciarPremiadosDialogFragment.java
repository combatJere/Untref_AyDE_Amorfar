package dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.altosoftuntref.amorfar.R;

/**
 * Created by jeremias on 12/06/2015.
 */
public class ReiniciarPremiadosDialogFragment extends DialogFragment {

    private ReiniciarPremiadosDialogListener reiniciarPremiadosListener;

    public interface ReiniciarPremiadosDialogListener {
        public void confirmarReiniciarPremiadosClick();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.desea_reiniciar_premiados)
                .setPositiveButton(R.string.reiniciar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        reiniciarPremiadosListener.confirmarReiniciarPremiadosClick();
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        // No lo uso en este caso, no hace nada si cancela.
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


    /**
     * Se asegura de que el Fragment que muestre este Dialog, implemente la interfaz correspondiente
     * para obtener el resultado del mismo.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            reiniciarPremiadosListener = (ReiniciarPremiadosDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("El fragmento que pretende reiniciar los premiados debe implementar ReiniciarPremiadosDialogListene");
        }
    }

    /**
     * @Edu Esto solo sirve para cuando el dialog lo ejecuta la actividad, si lo ejecuta el fragment
     * debo hacerlo en el onCreate()  Ver arriba ^ el onCreate()
     */
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        // This makes sure that the container activity has implemented
//        // the TimePickerFragmentListener interface. If not, it throws an exception
//        try {
//            reiniciarPremiadosListener = (ReiniciarPremiadosDialogListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement TimePickerFragmentListenerJer");
//        }
//    }


}
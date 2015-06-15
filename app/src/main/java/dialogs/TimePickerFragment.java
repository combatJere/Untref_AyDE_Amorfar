package dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.altosoftuntref.amorfar.CrearMenuActivity;

/**
 * TimePickerDialog que permite al usuario (administrador) cambiar la hora del almuerzo.
 * Created by jeremias on 03/06/2015.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private int horaRecivida;
    private int minutosRecividos;
    int callcount = 0; //por bug de android que llama onTimeSet aunque se aprete el boton atras o el se cierre el dialog.


    private TimePickerFragmentListenerJer timePickerListener;

    public interface TimePickerFragmentListenerJer {
        public void onHorarioSet(int hora, int minutos);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle horaYminutosRecividos = this.getArguments();

        horaRecivida = horaYminutosRecividos.getInt(CrearMenuActivity.EXTRA_HORA_TIMEPICKER);
        minutosRecividos = horaYminutosRecividos.getInt(CrearMenuActivity.EXTRA_MINUTOS_TIMEPICKER);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, horaRecivida, minutosRecividos,
                DateFormat.is24HourFormat(getActivity()));
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        if(callcount == 1) {
            timePickerListener.onHorarioSet(hourOfDay, minute);
        }
        callcount ++;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the TimePickerFragmentListener interface. If not, it throws an exception
        try {
            timePickerListener = (TimePickerFragmentListenerJer) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TimePickerFragmentListenerJer");
        }
    }

}
package barra_informe.Fragmentos;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.altosoftuntref.amorfar.R;

import java.util.Calendar;

import adapter.CantidadPorPlatoAdapter;
import inversiondecontrol.ServiceLocator;


/**
 * IMPORTANTE
 * Fue creado automanticamente con muchos metodos que por ahora no usamos, pero que luego podremos usar.
 * El unico metodo que usamos de los creados es el  onActivityCreated()
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InformeTabResumen.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InformeTabResumen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformeTabResumen extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private CantidadPorPlatoAdapter cantidadPorPlatoAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InformeTabResumen.
     */
    // TODO: Rename and change types and number of parameters
    public static InformeTabResumen newInstance(String param1, String param2) {
        InformeTabResumen fragment = new InformeTabResumen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public InformeTabResumen() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_informe_tab_resumen, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        this.instanciarComponentes();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    /**
     * Instanca todos los componenetes
     */
    private void instanciarComponentes(){
        Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH) + 1;
        int anio = c.get(Calendar.YEAR);

        this.instanciarListViewPlatosConCantidad(dia, mes, anio);
        this.instanciarTextViews(dia, mes, anio);
    }


    /**
     * Instancia el listview que mostrara el nombre de cada plato junto a la cantidad pedida.
     * @param dia
     * @param mes
     * @param anio
     * Recibe la fecha para saber cuales son los platos del menu correspondientes
     */
    private void instanciarListViewPlatosConCantidad(int dia, int mes, int anio){
        Cursor cursor = ServiceLocator.getInstance().getMenuesDao(getActivity().getBaseContext()).getPlatosDelMenu(dia, mes, anio);
        cantidadPorPlatoAdapter = new CantidadPorPlatoAdapter(getActivity(), cursor, 0);
        ListView listViewCantPorPlato = (ListView) getView().findViewById(R.id.listView_inforem_resmuen_cantidadPorPlato);
        listViewCantPorPlato.setAdapter(cantidadPorPlatoAdapter);
    }


    /**
     * Instancia todos los textview que mostrara el fragment.
     * @param dia
     * @param mes
     * @param anio
     * Recibe  la fecha para saber cual es el menu del que debe obtener y mostrar los valores.
     */
    private void instanciarTextViews(int dia, int mes, int anio){
        int cantidadComensalesTotales = ServiceLocator.getInstance().getUsuariosDAO(getActivity().getBaseContext()).
                getCantidadComensalesEnSistema();
        int cantNoVotaron = ServiceLocator.getInstance().getUsuariosDAO(getActivity().getBaseContext())
                .getCantidadNoVotaron(dia, mes, anio);
        int cantInvitados = ServiceLocator.getInstance().getUsuariosDAO(getActivity().getBaseContext())
                .getCantidadDeInvitadosTotales(dia, mes, anio);
        int cantidadNoComen = ServiceLocator.getInstance().getUsuariosDAO(getActivity().getBaseContext())
                .getCantidadNoComen(dia, mes, anio);

        int cantidadSiComen = cantidadComensalesTotales - cantidadNoComen - cantNoVotaron;

        int cantidadTotalAprox = cantidadSiComen + cantInvitados + cantNoVotaron;


        TextView textViewCantNoVotaron = (TextView) getView().findViewById(R.id.textView_Informe_resumen_CANTnoVotaron);
        textViewCantNoVotaron.setText(String.valueOf(cantNoVotaron));

        TextView textViewCantInvitados = (TextView) getView().findViewById(R.id.textView_Informe_resumen_CANTinvitados);
        textViewCantInvitados.setText(String.valueOf(cantInvitados));

        TextView textViewCantTotalAprox = (TextView) getView().findViewById(R.id.textView_informe_resumen_CANTtotalAprox);
        textViewCantTotalAprox.setText(String.valueOf(cantidadTotalAprox));
    }


}

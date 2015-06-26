package barra_informe.Fragmentos;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.altosoftuntref.amorfar.R;

import adapter.ComensalesConPremioAdapter;
import dialogs.ReiniciarPremiadosDialogFragment;
import inversiondecontrol.ServiceLocator;


/**
 * IMPORTANTE
 * Fue creado automanticamente con muchos metodos que por ahora no usamos, pero que luego podremos usar.
 * El unico metodo que usamos de los creados es el  onActivityCreated()
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InformeTabsListaPremiados.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InformeTabsListaPremiados#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformeTabsListaPremiados extends Fragment implements ReiniciarPremiadosDialogFragment.ReiniciarPremiadosDialogListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ComensalesConPremioAdapter premiadosAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InformeTabsListaPremiados.
     */
    // TODO: Rename and change types and number of parameters
    public static InformeTabsListaPremiados newInstance(String param1, String param2) {
        InformeTabsListaPremiados fragment = new InformeTabsListaPremiados();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public InformeTabsListaPremiados() {
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
        return inflater.inflate(R.layout.fragment_informe_tabs_lista_premiados, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        this.instanciarListViewPremiados();
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

    //MI CODIGO COMIENZA AQUI

    /**
     * Muestra el ListView de personas que conservan el permio.
     */
    private void instanciarListViewPremiados(){
        Cursor cursor = ServiceLocator.getInstance().getUsuariosDAO(getActivity().getBaseContext()).getUsuariosConPremioCursor();
        instanciarTextViewCantPremiados(cursor.getCount());
        ListView listViewPremiados = (ListView) getView().findViewById(R.id.listView_listaDePremios_usuariosConPremios2);
        premiadosAdapter = new ComensalesConPremioAdapter(getActivity(), cursor, 0);
        listViewPremiados.setAdapter(premiadosAdapter);
        instanciarBotonReiniciarPremiados();
    }


    private void instanciarTextViewCantPremiados(int cantidadPremiados){
        TextView textViewCantPremiados = (TextView) getView().findViewById(R.id.textView_listaDePremios_cantidadConPremio);
        textViewCantPremiados.setText(String.valueOf(cantidadPremiados));
    }


    /**
     * Actualiza el ListView, usado cuando hay cambios (se reinician los premiados)
     */
    private void actualizarListViewPremiados(){
        Cursor cursor = ServiceLocator.getInstance().getUsuariosDAO(getActivity().getBaseContext()).getUsuariosConPremioCursor();
        premiadosAdapter.changeCursor(cursor);
        premiadosAdapter.notifyDataSetChanged();
        instanciarTextViewCantPremiados(cursor.getCount());
    }


    /**
     * Seteo el onClick para el boton reiniciarPremiados.
     */
    private void instanciarBotonReiniciarPremiados(){
        FloatingActionButton floatingBotonReiniciarPremiados = (FloatingActionButton) getView().findViewById(R.id.floatingActionButton_listaPremiados);
        floatingBotonReiniciarPremiados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogReiniciarPremiados();
            }
        });
    }


    /**
     * Muestra el dialog para confirmar el reinicio de los Premiados.
     */
    private void showDialogReiniciarPremiados(){
        ReiniciarPremiadosDialogFragment dialogReiniciar = new ReiniciarPremiadosDialogFragment();
        dialogReiniciar.setTargetFragment(this, 0);
        dialogReiniciar.show(getFragmentManager(), "ReiniciarPremiadosDialog");
    }


    /**
     * si se confirma el ReiniciarPremiadosDialog
     */
    @Override
    public void confirmarReiniciarPremiadosClick() {
        ServiceLocator.getInstance().getUsuariosDAO(getActivity().getBaseContext()).reiniciarPremios();
        actualizarListViewPremiados();
    }


    public void reiniciarPijas(View view){

    }

}

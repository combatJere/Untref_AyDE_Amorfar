package com.altosoftuntref.amorfar;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import adapter.ComensalesConPremioAdapter;
import dialogs.ReiniciarPremiadosDialogFragment;
import inversiondecontrol.ServiceLocator;


public class ListaDePremiosActivity extends Activity implements ReiniciarPremiadosDialogFragment.ReiniciarPremiadosDialogListener{

    ComensalesConPremioAdapter premiadosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_premios);
        this.instanciarListViewPremiados();
    }

    private void instanciarListViewPremiados(){
        Cursor cursor = ServiceLocator.getInstance().getUsuariosDAO(getBaseContext()).getUsuariosConPremioCursor();
        ListView listViewPremiados = (ListView) findViewById(R.id.listView_listaDePremios_usuariosConPremios);
        premiadosAdapter = new ComensalesConPremioAdapter(this, cursor, 0);
        listViewPremiados.setAdapter(premiadosAdapter);
        instanciarTextViewCantPremiados(cursor.getCount());
    }

    private void instanciarTextViewCantPremiados(int cantidadPremiados){
        TextView textViewCantPremiados = (TextView) findViewById(R.id.textView_listaDePremios_cantidadConPremio);
        textViewCantPremiados.setText(String.valueOf(cantidadPremiados));
    }

    private void actualizarListViewPremiados(){
        Cursor cursor = ServiceLocator.getInstance().getUsuariosDAO(getBaseContext()).getUsuariosConPremioCursor();
        premiadosAdapter.changeCursor(cursor);
        premiadosAdapter.notifyDataSetChanged();
        instanciarTextViewCantPremiados(cursor.getCount());
    }

    /**
     * Onclick
     * Pone a todos los usuarios en estado de tener premio.
     */
    public void showTimepickerReiniciarPremiados(View view){
        ReiniciarPremiadosDialogFragment dialogReiniciar = new ReiniciarPremiadosDialogFragment();
        dialogReiniciar.show(this.getFragmentManager(), "ReiniciarPremiosDialog");
    }

    /**
     * si se confirma el ReiniciarPremiadosDialog
     */
    @Override
    public void onCantidadPlatosConfirmarClick() {
        ServiceLocator.getInstance().getUsuariosDAO(getBaseContext()).reiniciarPremios();
        actualizarListViewPremiados();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_lista_de_premios, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}

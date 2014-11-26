package com.example.busdevelop.sendlocation;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Locale;


public class ObtRutasActivity extends ActionBarActivity {

    ManejadorRutas mManejador;
    ListView mList;
    static ListViewAdapter smAdapter;
    EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obt_rutas);

        // Llocalizar el listview en activity_obt_rutas.xml
        mList = (ListView) findViewById(R.id.listviewRutas);

        new HttpAsyncTask(this).execute();

        // Localizar el search_text
        searchText = (EditText) findViewById(R.id.search_text);

        // capturar el texto en search_text
        searchText.addTextChangedListener(new TextWatcher() {

            /**
             * Metodo que se da cuenta que el campo de texto
             * esta recibiendo caracteres y llama al metodo filtrar
             * del ListViewAdapter
             * @param arg0
             */
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                smAdapter.filter(text);
            }

            /*
                Estos metodos hay que sobreescribirlos pero no se ocupan
             */
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Metodo que llama a la instancia del ManejadordeRutas
     *
     * @return String con  el array Json
     */
    public  boolean llenarRutas(){
        mManejador = ManejadorRutas.getInstancia("b0936d7e239775e770ce002307f0acda");
        if(mManejador.getListaRutas() != null){
            return true;
        }else
            return false;
    }



    private class HttpAsyncTask extends AsyncTask<String, Void, Boolean> {
        Activity mActivity;
        private HttpAsyncTask(Activity activity){
            this.mActivity = activity;
        }


        @Override
        protected Boolean doInBackground(String... token) {

            return llenarRutas();
        }

        /**
         * metodo que se ejecuta después de obtener la respuesta
         * al request get
         * @param succes
         */
        @Override
        protected void onPostExecute(final Boolean succes) {
            if( succes){
                Toast.makeText(getBaseContext(), "Rutas Obtenidas!", Toast.LENGTH_LONG).show();
                // Pasar las rutas al  ListViewAdapter
                smAdapter = new ListViewAdapter(mActivity, mManejador.getListaRutas());

                // enlazar el adaptador con el listView
                mList.setAdapter(smAdapter);
            }else{
                Toast.makeText(getBaseContext(), "Error al obtener rutas!",
                        Toast.LENGTH_LONG).show();
            }

        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_main, container, false);
            return rootView;
        }
    }

}
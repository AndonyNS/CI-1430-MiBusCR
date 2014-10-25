package com.example.busdevelop.buses;

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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Locale;


public class ObtRutasActivity extends ActionBarActivity {

    ManejadorRutas mManejador;
    ListView mList;
    static ListViewAdapter mAdapter;
    EditText searchText;
    Usuario mUsuario;
    private final String mPrefs_Name = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obt_rutas);

        // Obtener datos del usuario
        // obtener del shared preferences el email
        // y el password
        mUsuario = new Usuario();
        SharedPreferences sharedPref = getSharedPreferences(mPrefs_Name, 0);
        mUsuario.setEmail(sharedPref.getString("UserEmail", ""));
        mUsuario.setEncrypted_password(sharedPref.getString("UserPass", ""));

        // Llocalizar el listview en activity_obt_rutas.xml
        mList = (ListView) findViewById(R.id.listviewRutas);
        //  Obtener el token
        new HttpAsyncTaskToken(this).execute();

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
                    mAdapter.filter(text);
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


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.obt_rutas, menu);
        return true;
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
     * @param token token del usuario
     * @return String con  el array Json
     */
    public  boolean llenarRutas(String token){
        mManejador = ManejadorRutas.getInstancia(token);
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

            return llenarRutas(token[0]);
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
                   mAdapter = new ListViewAdapter(mActivity, mManejador.getListaRutas());

                   // enlazar el adaptador con el listView
                   mList.setAdapter(mAdapter);
               }else{
                   Toast.makeText(getBaseContext(), "Error al obtener rutas!",
                           Toast.LENGTH_LONG).show();
               }

        }
    }

    /**
     * Obtener el token para poder consultar rutas
     */
    private class HttpAsyncTaskToken extends AsyncTask<Void, Void, String> {
        Activity mActivity;
        private HttpAsyncTaskToken(Activity activity){
            this.mActivity = activity;
        }


        @Override
        protected String doInBackground(Void...params) {

            return mUsuario.obtenerToken(mUsuario.getEmail(), mUsuario.getEncrypted_password());
        }

        /**
         * metodo que se ejecuta después de obtener la respuesta
         * al request post del token
         * @param resultado
         */
        @Override
        protected void onPostExecute(String resultado) {
            mUsuario.guardarTokenId(resultado);

            // una vez obtenido el token se pide las rutas
            new HttpAsyncTask(mActivity).execute(mUsuario.getToken());

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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public static class AdFragment extends Fragment {

        private AdView mAdView;

        public AdFragment() {
        }

        @Override
        public void onActivityCreated(Bundle bundle) {
            super.onActivityCreated(bundle);
            mAdView = (AdView) getView().findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_ad, container, false);
        }

        // Called when leaving the activity
        @Override
        public void onPause() {
            if (mAdView != null) {
                mAdView.pause();
            }
            super.onPause();
        }

        // Called when returning to the activity
        @Override
        public void onResume() {
            super.onResume();
            if (mAdView != null) {
                mAdView.resume();
            }
        }

        // Called before the activity is destroyed
        @Override
        public void onDestroy() {
            if (mAdView != null) {
                mAdView.destroy();
            }
            super.onDestroy();
        }


    }

}
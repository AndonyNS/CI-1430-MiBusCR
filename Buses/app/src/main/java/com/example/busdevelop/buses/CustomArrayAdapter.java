package com.example.busdevelop.buses;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
 
/**
 * Adapter personalizado que permite tanto el checkbox como
 * el texto, y el onCheckedChanged para el checkbox
 * @author danielme.com
 * @modified by Andony
 * 
 */
public class CustomArrayAdapter extends ArrayAdapter<Row>
{
    private LayoutInflater layoutInflater;
    private Activity mActivity;
    private List<Ruta> mRutas;
    private Usuario mUsuario;
    private String mUrl = "https://murmuring-anchorage-1614.herokuapp.com/favoritas/";
 
    public CustomArrayAdapter(Context context, List<Row> objects,Activity activity,List<Ruta> array, Usuario usuario)
    {
        super(context, 0, objects);
        layoutInflater = LayoutInflater.from(context);
        this.mActivity = activity;
        this.mRutas = array;
        this.mUsuario = usuario;
    }
 
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // holder pattern
        Holder holder = null;
        if (convertView == null)
        {
            holder = new Holder();
 
            convertView = layoutInflater.inflate(R.layout.list_item, null);
            holder.setTextViewTitle((TextView) convertView.findViewById(R.id.textViewTitle));
            holder.setCheckBox((CheckBox) convertView.findViewById(R.id.checkBox));
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }
 
        final Row row = getItem(position);
        holder.getTextViewTitle().setText(row.getTitle());
        holder.getCheckBox().setTag(row.getTitle());
        holder.getCheckBox().setChecked(row.isChecked());
        
        holder.getCheckBox().setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked)
            {
                //asegura que se modifica la Row originalmente asociado a este checkbox
                //para evitar que al reciclar la vista se reinicie el row que antes se mostraba en esta
                //fila. Es imprescindible tagear el Row antes de establecer el valor del checkbox
                if (row.getTitle().equals(view.getTag().toString()))
                {
                    row.setChecked(isChecked);

                    Ruta seleccionada = mRutas.get(position);
                    String url = mUrl + seleccionada.getId();
                    Log.d(url,"esta es la url");
                    if(!isChecked){

                        //Aqui debe quitar la ruta de favoritos
                    	Toast.makeText(getContext(), "Ya no :( ", Toast.LENGTH_SHORT).show();


                        new HttpAsyncTaskDelete(mActivity).execute(url);
                    }else{
                        //Aqui debe agregar la ruta a favoritos
                    	Toast.makeText(getContext(), "Si :) ", Toast.LENGTH_SHORT).show();

                        new HttpAsyncTaskAgregar(mActivity).execute(url);
                    }
                }
                //AQUI DEBE ESTAR EL HANDLER DE QUE EL TIPO DE CAMBIO FUE INCLUIDO
            }
        });
         
         
        return convertView;
    }

    public String deleteRuta(String url){

        String resultado = ApiManager.httpDelete(url, mUsuario.getToken());

        return resultado;
    }

    public String postRuta(String url){
        Token token = new Token(mUsuario.getEmail(),mUsuario.getEncrypted_password());
        String resultado = ApiManager.httpPost(url, mUsuario.getToken(),token);

        return resultado;
    }

    private class HttpAsyncTaskDelete extends AsyncTask<String, Void, String> {
        Activity mActividad;
        private HttpAsyncTaskDelete(Activity activity){
            this.mActividad = activity;
        }

        @Override
        protected String doInBackground(String... urls) {
            try{

                String resultado = deleteRuta(urls[0]);
                Log.d(resultado,"");

            }catch(Exception e){
                e.printStackTrace();
            }
            return "Ruta borrada de Favoritas!";
        }

        /**
         * metodo que se ejecuta después de obtener la respuesta
         * al request get
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(mActividad.getBaseContext(), result, Toast.LENGTH_SHORT).show();

        }


    }

    private class HttpAsyncTaskAgregar extends AsyncTask<String, Void, String> {
        Activity mActividad;
        private HttpAsyncTaskAgregar(Activity activity){
            this.mActividad = activity;
        }

        @Override
        protected String doInBackground(String... urls) {
            try{

                String resultado = postRuta(urls[0]);
                Log.e(resultado,"");

            }catch(Exception e){
                e.printStackTrace();
            }
            return "Ruta Agregada a Favoritas!";
        }

        /**
         * metodo que se ejecuta después de obtener la respuesta
         * al request get
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(mActividad.getBaseContext(), result, Toast.LENGTH_SHORT).show();

        }


    }
 
}
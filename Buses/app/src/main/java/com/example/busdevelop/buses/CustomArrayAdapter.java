package com.example.busdevelop.buses;

import android.content.Context;
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
 
    public CustomArrayAdapter(Context context, List<Row> objects)
    {
        super(context, 0, objects);
        layoutInflater = LayoutInflater.from(context);
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
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
                    if(!isChecked){

                        //Aqui debe quitar la ruta de favoritos
                    	Toast.makeText(getContext(), "Ya no :( ", Toast.LENGTH_SHORT).show();
                    }else{
                        //Aqui debe agregar la ruta a favoritos
                    	Toast.makeText(getContext(), "Si :) ", Toast.LENGTH_SHORT).show();
                    }
                }
                //AQUI DEBE ESTAR EL HANDLER DE QUE EL TIPO DE CAMBIO FUE INCLUIDO
            }
        });
         
         
        return convertView;
    }
 
}
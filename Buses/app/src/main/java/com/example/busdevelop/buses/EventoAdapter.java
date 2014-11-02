package com.example.busdevelop.buses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jossue on 29/10/14.
 */
public class EventoAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    List<Evento> mEventosArray ;



    public EventoAdapter(Context context, List<Evento> eventosArray) {
        mContext = context;
        mEventosArray = eventosArray;
        inflater = LayoutInflater.from(mContext);

    }

    // Los campos que se muestran en cada fila de la lista
    public class ViewHolder {
        TextView tituloEvento;
        TextView fechaEvento;
        ImageView imagenEvento;
    }

    @Override
    public int getCount() {
        return mEventosArray.size();
    }

    @Override
    public Evento getItem(int position) {
        return mEventosArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.evento_list_item, null);
            // Localizar los view en evento_list_item.xml
            holder.tituloEvento = (TextView) view.findViewById(R.id.tituloEvento);
            holder.fechaEvento = (TextView) view.findViewById(R.id.fechaEvento);
            holder.imagenEvento = (ImageView) view.findViewById(R.id.imagenEvento);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Poner los resultados en los view
        /*String nombreEvento = mEventosArray.get(position).getNombre();
        if (nombreEvento.length() > 5){
            holder.tituloEvento.setTextSize(8);
        }*/

        holder.tituloEvento.setText(mEventosArray.get(position).getNombre());
        holder.fechaEvento.setText(mEventosArray.get(position).getFecha());

        // Dependiendo del tipo de evento la imagen cambiara
        switch(Integer.parseInt(mEventosArray.get(position).getTipo())){
            case 1: // Artistico
                if(position  == 0){
                    holder.imagenEvento.setImageResource(R.drawable.arte2);
                } else{
                    holder.imagenEvento.setImageResource(R.drawable.arte1);
                }

                break;
            case 2: // Deportivo
                holder.imagenEvento.setImageResource(R.drawable.deportes2);
                break;
            case 3: // Fiesta
                if(position % 2 == 0){
                    holder.imagenEvento.setImageResource(R.drawable.fiesta2);
                }else{
                    holder.imagenEvento.setImageResource(R.drawable.fiesta3);
                }

                break;
            case 4: // Concierto
                if(position % 2 != 0) {
                    holder.imagenEvento.setImageResource(R.drawable.concierto2);
                }else{
                    holder.imagenEvento.setImageResource(R.drawable.concierto4);
                }
                break;
            case 5: // Exposicion
                holder.imagenEvento.setImageResource(R.drawable.arte2);
                break;
        }

        // agregar listener para un click de fila
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


            }
        });

        return view;
    }



}


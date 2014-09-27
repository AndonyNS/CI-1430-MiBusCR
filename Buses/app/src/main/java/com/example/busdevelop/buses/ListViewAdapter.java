package com.example.busdevelop.buses;


import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    ArrayList<Ruta> mRutasArray = null;
    ArrayList<Ruta> mLista;


    public ListViewAdapter(Context context, ArrayList<Ruta> rutasArray) {
        mContext = context;
        this.mRutasArray = rutasArray;
        inflater = LayoutInflater.from(mContext);
        this.mLista = new ArrayList<Ruta>();
        mLista.addAll(mRutasArray);
    }

    public class ViewHolder {
        TextView id;
        TextView nombre;
        TextView frecuencia;
        //TextView precio;
        //TextView horario;
    }

    @Override
    public int getCount() {
        return mRutasArray.size();
    }

    @Override
    public Ruta getItem(int position) {
        return mRutasArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.nombre = (TextView) view.findViewById(R.id.nombreR);
            holder.frecuencia = (TextView) view.findViewById(R.id.frecuenciaR);
            //holder.precio = (TextView) view.findViewById(R.id.precioR);
            //holder. horario= (TextView) view.findViewById(R.id.horarioR);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.nombre.setText(mRutasArray.get(position).getNombre());
        holder.frecuencia.setText(mRutasArray.get(position).getFrecuencia());
        //holder.precio.setText(mRutasArray.get(position).getPrecio());
        //holder.horario.setText(mRutasArray.get(position).getHorario());

        // Listen for ListView Item Click
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(mContext, SingleItemView.class);
                // Pass all data rank
                intent.putExtra("nombre",(mRutasArray.get(position).getNombre()));
                // Pass all data country
                intent.putExtra("frecuencia",(mRutasArray.get(position).getFrecuencia()));
                // Pass all data population
                intent.putExtra("precio",(mRutasArray.get(position).getPrecio()));
                intent.putExtra("horario",(mRutasArray.get(position).getHorario()));
                // Start SingleItemView Class
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mRutasArray.clear();
        if (charText.length() == 0) {
            mRutasArray.addAll(mLista);
        }
        else
        {
            for (Ruta ruta : mLista)
            {
                if (ruta.getNombre().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    mRutasArray.add(ruta);
                }
            }
        }
        notifyDataSetChanged();
    }

}

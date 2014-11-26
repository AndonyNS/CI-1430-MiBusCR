package com.example.busdevelop.sendlocation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

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
        TextView nombre;
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
            // Localizar el textview en  listview_item.xml
            holder.nombre = (TextView) view.findViewById(R.id.nombreR);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Poner los resultados en el textView
        holder.nombre.setText(mRutasArray.get(position).getNombre());

        // agregar listener para un click de fila
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Mandar los datos de la ruta a la actividad SleItemView
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("id",(mRutasArray.get(position).getId()));

                mContext.startActivity(intent);
            }
        });

        return view;
    }

    /**
     * Metodo para filtrar, recibe  caracteres y verifica
     * que el nombre de la ruta los contenga
     * si lo que se escribio no esta en el nombre de ninguna ruta
     * la lista queda en blanco
     * @param charText
     */
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

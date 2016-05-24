package passgenerator.myapplication;

import android.content.Context;
import android.graphics.drawable.RippleDrawable;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alberto.alvarez on 10/05/16.
 */
public class AdaptadorLista extends RecyclerView.Adapter<AdaptadorLista.MyViewHolder>{
    /*****************************ADAPTADOR PARA A LISTA DE SERVIZOS*********************************/
    private List<Items> contrasinais;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView texto;
        public ImageView imaxe;

        public MyViewHolder(View view) {
            super(view);
            texto = (TextView) view.findViewById(R.id.nombre_fila_lista);
            imaxe = (ImageView) view.findViewById(R.id.idImaxeEsquerda);
        }
    }


    public AdaptadorLista(List<Items> contrasinais) {
        this.contrasinais = contrasinais;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.xml_lista, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Items item = contrasinais.get(position);
        holder.texto.setText(item.getRuta());
        holder.imaxe.setImageResource(item.getImaxe());

    }

    @Override
    public int getItemCount() {
        return contrasinais.size();
    }

}

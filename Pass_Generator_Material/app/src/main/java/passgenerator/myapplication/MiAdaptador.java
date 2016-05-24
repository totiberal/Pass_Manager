package passgenerator.myapplication;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
public class MiAdaptador extends RecyclerView.Adapter<MiAdaptador.MyViewHolder>{

    /********************ADAPTADOR PARA O EXPLORADOR DE FICHEIRO**********************/
    private List<Items> ficheiros;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView texto;
        public ImageView imaxe;

        public MyViewHolder(View view) {
            super(view);
            texto = (TextView) view.findViewById(R.id.idTipo);
            imaxe = (ImageView) view.findViewById(R.id.idTipoFicheiro);
        }
    }


    public MiAdaptador(List<Items> contrasinais) {
        this.ficheiros = contrasinais;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.xml_lista_directorios, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Items item = ficheiros.get(position);
        holder.texto.setText(item.getRuta());
        holder.imaxe.setImageResource(item.getImaxe());

    }

    @Override
    public int getItemCount() {
        return ficheiros.size();
    }


}

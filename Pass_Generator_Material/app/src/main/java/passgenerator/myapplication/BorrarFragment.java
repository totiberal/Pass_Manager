package passgenerator.myapplication;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BorrarFragment extends DialogFragment {

    private AppCompatButton btnAceptar, btnCancelar;
    private BaseDatos bd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.activity_borrar_fragment, container, false);
        getDialog().setTitle(getTag());         // O Tag se envía dende a activiy có método show.

        btnAceptar=(AppCompatButton) rootView.findViewById(R.id.idbtnAceptar);
        btnCancelar=(AppCompatButton) rootView.findViewById(R.id.idbtnCancelar);
        bd = new BaseDatos(getActivity());
        bd.sqlLiteDB = bd.getWritableDatabase();

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bd.borrar(getArguments().getString("borrar"));
                if(getActivity().getClass().equals(MostrarDatos.class)) {
                    ((MostrarDatos) BorrarFragment.this.getActivity()).comprobarSalida(0);
                }
                dismiss();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        // Do something else
        return rootView;
    }

    /**Metodos da base de datos*/

    //Metodo que inicia as chamadas a base de datos
    @Override
    public void onStart(){
        super.onStart();

        if (bd==null) {   // Abrimos a base de datos para escritura
            bd = new BaseDatos(getActivity());
            bd.sqlLiteDB = bd.getWritableDatabase();

        }
    }

    //Metodo que para as chamadas a base de datos
    @Override
    public void onStop(){
        super.onStop();

        if (bd!=null){    // Pechamos a base de datos.
            bd.close();
            bd=null;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ContrasinaisFragment.adaptador.notifyDataSetChanged();
    }

    /**FIN Metodos da base de datos*/

}

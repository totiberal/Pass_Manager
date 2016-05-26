package passgenerator.myapplication;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class Fragment_Change_Data extends DialogFragment {

    private AppCompatButton btnAceptar, btnCancelar;
    private EditText etUser, etPass;
    private BaseDatos bd;
    private String dato;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.activity_fragment__change__data, container, false);
        getDialog().setTitle(getTag());         // O Tag se envía dende a activiy có método show.
        bd = new BaseDatos(getActivity());
        bd.sqlLiteDB = bd.getWritableDatabase();
        btnAceptar=(AppCompatButton) rootView.findViewById(R.id.aceptar);
        btnCancelar=(AppCompatButton) rootView.findViewById(R.id.cancelar);
        etUser=(EditText) rootView.findViewById(R.id.idChangeUser);
        etPass=(EditText) rootView.findViewById(R.id.idChangePass);
        dato=getArguments().getString("modificar");

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bd.modificar(dato ,etPass.getText().toString(), etUser.getText().toString());
                if(getActivity().getClass().equals(MostrarDatos.class)) {
                    ((MostrarDatos) Fragment_Change_Data.this.getActivity()).volverCambios(etUser.getText().toString(), etPass.getText().toString());
                }ContrasinaisFragment.adaptador.notifyDataSetChanged();
                ContrasinaisFragment c = new ContrasinaisFragment();
                c.prepararAdaptador();
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

}

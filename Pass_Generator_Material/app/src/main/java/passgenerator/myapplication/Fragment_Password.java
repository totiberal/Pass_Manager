package passgenerator.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class Fragment_Password extends DialogFragment {

    private AppCompatButton btnAceptar, btnCancelar;
    private EditText etPassVella, etPassNova;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.activity_fragment__password, container, false);
        getDialog().setTitle(getTag());         // O Tag se envía dende a activiy có método show.

        btnAceptar=(AppCompatButton) rootView.findViewById(R.id.idAceptar);
        btnCancelar=(AppCompatButton) rootView.findViewById(R.id.idCancelar);
        etPassNova=(EditText) rootView.findViewById(R.id.idPassNova);
        etPassVella=(EditText) rootView.findViewById(R.id.idPassVella);
        etPassNova.setText("");
        etPassVella.setText("");
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Listado)Fragment_Password.this.getActivity()).cambiarContrasinal(etPassVella.getText().toString(), etPassNova.getText().toString());
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
}

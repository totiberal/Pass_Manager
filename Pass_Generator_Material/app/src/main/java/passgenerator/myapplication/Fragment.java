package passgenerator.myapplication;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class Fragment extends DialogFragment {

    public String valorTexto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.activity_borrar_fragment, container, false);
        getDialog().setTitle(getTag());         // O Tag se envía dende a activiy có método show.

        Button btn = (Button) rootView.findViewById(R.id.buttonPecharDialogo);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EditText edit = (EditText)rootView.findViewById(R.id.editTexto);
                valorTexto = edit.getText().toString();
                if(valorTexto.equals("") || valorTexto==null) ((Main)Fragment.this.getActivity()).pantallaInicial("abc123.");
                else ((Main)Fragment.this.getActivity()).pantallaInicial(valorTexto);
                dismiss();
            }
        });

        // Do something else
        return rootView;
    }
}

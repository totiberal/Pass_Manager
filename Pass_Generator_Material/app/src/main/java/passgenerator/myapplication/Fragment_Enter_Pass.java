package passgenerator.myapplication;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class Fragment_Enter_Pass extends DialogFragment {

    private EditText etPassword;
    private AppCompatButton btnAceptar, btnCancelar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.activity_fragment__enter__pass, container, false);
        getDialog().setTitle(getTag());         // O Tag se envía dende a activiy có método show.

        etPassword=(EditText) rootView.findViewById(R.id.enterPass);
        btnAceptar=(AppCompatButton) rootView.findViewById(R.id.idAceptarPass);
        btnCancelar=(AppCompatButton) rootView.findViewById(R.id.idCancelarPass);
        etPassword.setText("");
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Main)Fragment_Enter_Pass.this.getActivity()).comprobarContrasinal(etPassword.getText().toString());
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

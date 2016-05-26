package passgenerator.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Gardar_Datos extends AppCompatActivity {

    private Intent intent;
    private String pass, color;
    private TextView tvContrasinal;
    private BaseDatos bd;
    private Button btnGardarDef, btnAtras;
    private EditText etServizo, etUsuario;
    private Toolbar mToolbar;
    private boolean vibrar;
    private Vibrator vib;
    private LinearLayout ly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gardar__datos);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setLogo(R.drawable.ic_launcher);

        intent=getIntent();
        pass=intent.getExtras().getString("pass");
        vibrar=intent.getExtras().getBoolean("vibrar", false);
        color=intent.getExtras().getString("color","Azul");
        ly=(LinearLayout) findViewById(R.id.idLinear);
        tvContrasinal=(TextView) findViewById(R.id.idPassDef);
        tvContrasinal.setText(pass);
        tvContrasinal.setTextSize(25);
        etServizo=(EditText) findViewById(R.id.idNomeServ);
        etUsuario=(EditText) findViewById(R.id.idNomeUsuario);
        btnGardarDef=(Button) findViewById(R.id.idGardarDef);
        btnAtras=(Button) findViewById(R.id.idAtras);
        cambiarTema();
        btnGardarDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etServizo.getText().toString().equals("") || etServizo.getText()==null){
                    Snackbar.make(ly, R.string.falta_servicio, Snackbar.LENGTH_LONG).show();
                    /*if(vibrar){
                        vib.vibrate(500);
                    }else{}*/
                }else if(etUsuario.getText().toString().equals("") || etUsuario.getText()==null){
                    Snackbar.make(ly, R.string.falta_usuario, Snackbar.LENGTH_LONG).show();
                   /* if(vibrar){
                        vib.vibrate(500);
                    }else{}*/
                }else if(bd.consulta(etServizo.getText().toString())){
                    Snackbar.make(ly, R.string.toast_existe, Snackbar.LENGTH_LONG).show();
                    /*if(vibrar){
                        vib.vibrate(500);
                    }else{}*/
                }
                else {
                    String encriptadoPass = Utilidades.Encriptar(tvContrasinal.getText().toString());
                    String encriptadoUser=Utilidades.Encriptar(etUsuario.getText().toString());
                    bd.engadir(new Pares(etServizo.getText().toString(), encriptadoUser, encriptadoPass));
                    Snackbar.make(ly, R.string.toast_correcto, Snackbar.LENGTH_LONG).show();
                }

            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void cambiarTema(){
        switch(color){
            case "Rojo":
                color="Rojo";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.rojo));
                btnGardarDef.setBackground(getResources().getDrawable(R.drawable.estilo_boton_rojo));
                btnAtras.setBackground(getResources().getDrawable(R.drawable.estilo_boton_rojo));
                break;
            case "Verde":
                color="Verde";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.verde));
                btnGardarDef.setBackground(getResources().getDrawable(R.drawable.estilo_boton_verde));
                btnAtras.setBackground(getResources().getDrawable(R.drawable.estilo_boton_verde));
                break;
            case "Amarillo":
                color="Amarillo";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.amarelo));
                btnGardarDef.setBackground(getResources().getDrawable(R.drawable.estilo_boton_amarelo));
                btnAtras.setBackground(getResources().getDrawable(R.drawable.estilo_boton_amarelo));
                break;
            case "Naranja":
                color="Naranja";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.naranja));
                btnGardarDef.setBackground(getResources().getDrawable(R.drawable.estilo_boton_naranja));
                btnAtras.setBackground(getResources().getDrawable(R.drawable.estilo_boton_naranja));
                break;
            default:
                color="Azul";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.color_boton));
                btnGardarDef.setBackground(getResources().getDrawable(R.drawable.estilo_botones));
                btnAtras.setBackground(getResources().getDrawable(R.drawable.estilo_botones));
                break;
        }
    }

    /**Metodos de acceso a base de datos*/
    @Override
    public void onStart(){
        super.onStart();

        if (bd==null) {   // Abrimos a base de datos para escritura
            bd = new BaseDatos(this);
            bd.sqlLiteDB = bd.getWritableDatabase();
        }
    }

    @Override
    public void onStop(){
        super.onStop();

        if (bd!=null){    // Pechamos a base de datos.
            bd.close();
            bd=null;
        }
    }
    /**FIN Metodos acceso a base de datos*/

    /**MENUS*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**FIN Menus*/


}

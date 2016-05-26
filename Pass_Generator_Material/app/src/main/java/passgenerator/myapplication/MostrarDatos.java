package passgenerator.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MostrarDatos extends AppCompatActivity {

    TextView tvServicio, tvConstrasinal, tvUsuario;
    Intent intent;
    Button btnAtras, btnBorrar, btnModificar;
    BaseDatos bd;
    AlertDialog.Builder venta;
    String dato;
    private Toolbar mToolbar;
    String color;
    private BorrarFragment borrarFragment;
    private Fragment_Change_Data fragmentChangeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_datos);

        //Chamo o action bar
        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setLogo(R.drawable.ic_launcher);
        borrarFragment=new BorrarFragment();
        fragmentChangeData=new Fragment_Change_Data();
        //Activo as chamadas a base de datos
        bd = new BaseDatos(this);
        bd.sqlLiteDB = bd.getWritableDatabase();

        //Declaro as variables
        tvServicio=(TextView) findViewById(R.id.idTextoServicio);
        tvConstrasinal=(TextView) findViewById(R.id.idTextoContrasinal);
        tvUsuario=(TextView) findViewById(R.id.idTextoUsuario);
        btnAtras=(Button) findViewById(R.id.idBtnAtras);
        btnBorrar=(Button) findViewById(R.id.idBorrar);
        btnModificar=(Button) findViewById(R.id.idEditar);

        intent=getIntent();
        color=intent.getExtras().getString("color", "Azul");
        color=Listado.color;
        seleccionTema();

        //Collo os datos pasados dende a outra activity e os meto nos TextView
        try{
            String datos[] =intent.getExtras().getStringArray("datos");

            dato=datos[0];
            tvServicio.setText(datos[0]);
            tvUsuario.setText(Utilidades.Desencriptar(datos[1]));
            tvConstrasinal.setText(Utilidades.Desencriptar(datos[2]));
            tvUsuario.setTextSize(15);
            tvServicio.setTextSize(15);
            tvConstrasinal.setTextSize(15);
        }catch (Exception ex){
            Log.i("FALLO", "Error desencriptando en mosrar datos");
        }


        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirFragmentBorrar();
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarDatosFrgament();
            }
        });

    }

    public void modificarDatosFrgament(){
        Bundle b=new Bundle();
        b.putString("modificar", dato);
        fragmentChangeData.setArguments(b);
        FragmentManager fm = getSupportFragmentManager();
        fragmentChangeData.show(fm, getResources().getString(R.string.cambiar_datos));
    }

    public void abrirFragmentBorrar(){
        Bundle b=new Bundle();
        b.putString("borrar", tvServicio.getText().toString());
        borrarFragment.setArguments(b);
        FragmentManager fm = getSupportFragmentManager();
        borrarFragment.show(fm, getResources().getString(R.string.atencion));
    }

    //Metodo para cambiar a cor do Toolbar
    public void seleccionTema() {
        switch (color) {
            case "Rojo":
                color = "Rojo";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.rojo));
                btnBorrar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_rojo));
                btnAtras.setBackground(getResources().getDrawable(R.drawable.estilo_boton_rojo));
                btnModificar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_rojo));
                break;
            case "Verde":
                color = "Verde";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.verde));
                btnBorrar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_verde));
                btnAtras.setBackground(getResources().getDrawable(R.drawable.estilo_boton_verde));
                btnModificar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_verde));
                break;
            case "Amarillo":
                color = "Amarillo";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.amarelo));
                btnBorrar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_amarelo));
                btnAtras.setBackground(getResources().getDrawable(R.drawable.estilo_boton_amarelo));
                btnModificar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_amarelo));

                break;
            case "Naranja":
                color = "Naranja";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.naranja));
                btnBorrar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_naranja));
                btnAtras.setBackground(getResources().getDrawable(R.drawable.estilo_boton_naranja));
                btnModificar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_naranja));

                break;
            default:
                color = "Azul";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.color_boton));
                btnBorrar.setBackground(getResources().getDrawable(R.drawable.estilo_botones));
                btnAtras.setBackground(getResources().getDrawable(R.drawable.estilo_botones));
                btnModificar.setBackground(getResources().getDrawable(R.drawable.estilo_botones));
                break;
        }
    }

    //Metodo que inicia as chamadas a base de datos
    @Override
    public void onStart(){
        super.onStart();

        if (bd==null) {   // Abrimos a base de datos para escritura
            bd = new BaseDatos(this);
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

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todas, menu);
        return true;
    }

    //Accions do menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        //Esto fai que a foto do menu sexa clicable
        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Metodo que cando se volve do fragment de borrar comproba se ten que sair porque se borrou ou non
    public void comprobarSalida(int num){
        if(num==0) finish();
    }

    //Metodo que pon ben os datos o volver de cambialos
    public void volverCambios(String user, String pass){
        tvUsuario.setText(user);
        tvConstrasinal.setText(pass);
    }

}

package passgenerator.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import org.w3c.dom.Text;

public class MostrarDatos extends AppCompatActivity {

    TextView tvServicio, tvConstrasinal, tvUsuario;
    Intent intent;
    Button btnAtras, btnBorrar, btnModificar;
    BaseDatos bd;
    AlertDialog.Builder venta;
    String dato;
    private Toolbar mToolbar;

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

        //Collo os datos pasados dende a outra activity e os meto nos TextView
        try{
            String datos[] =intent.getExtras().getStringArray("datos");
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
                showDialog(1);
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(2);
            }
        });

    }

    //Metodo que amosa os dialogos correspondentes
    protected Dialog onCreateDialog(int id) {
        if(id==1) {
            venta = new AlertDialog.Builder(this);
            venta.setIcon(android.R.drawable.ic_dialog_info);
            venta.setTitle(R.string.alerta);
            venta.setMessage(R.string.mensaxe);
            venta.setCancelable(false);
            venta.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int boton) {
                                    /* Sentencias se o usuario preme Si */
                    bd.borrar(tvServicio.getText().toString());
                    System.exit(0);
                }
            });
            venta.setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int boton) {
                                    /* Sentencias se o usuario preme Non */
                }
            });

            return venta.create();
        }

        if(id==2){
            // Primeiro preparamos o interior da ventá de diálogo inflando o seu
            // fichero XML
            String infService = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(infService);
            // Inflamos o compoñente composto definido no XML
            View inflador = li.inflate(R.layout.entrada_texto, null);
            // Buscamos os compoñentes dentro do Diálogo
            final EditText etNome = (EditText) inflador.findViewById(R.id.et_nome);

            venta = new AlertDialog.Builder(this);
            venta.setTitle(R.string.elixe);
            // Asignamos o contido dentro do diálogo (o que inflamos antes)
            venta.setView(inflador);
            // Non se pode incluír unha mensaxe dentro deste tipo de diálogo!!!
            venta.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int boton) {
                    dato=etNome.getText().toString();
                    cambios(dato);
                }
            });
            venta.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int boton) {

                }
            });
            return venta.create();
        }
        return null;
    }

    //Metodo para confirmar os cambios
    public void cambios(String dato){
        bd.modificar(tvServicio.getText().toString(), dato);
        tvConstrasinal.setText(dato);
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

}

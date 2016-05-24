package passgenerator.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;

public class Main extends AppCompatActivity {

    private AlertDialog.Builder venta;
    private EditText tvEntrada, tvSalida, etContrasinal;
    private Button btnXerar, btnGardar, btnXestionar;
    private RadioButton rbNumero, rbLetra, rbTodo;
    private int lonxitude,numero;
    private Random r;
    private SharedPreferences preferencias, pref;
    private Fragment dialogoFragmento;
    private String contrasinal, color="Azul";
    private Intent intent;
    private BaseDatos bd;
    static int cont;
    private Toolbar mToolbar;
    private boolean vibrar=true;
    private Vibrator vib;
    private LinearLayout rel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cont=0;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setLogo(R.drawable.ic_launcher);
        rel=(LinearLayout) findViewById(R.id.probando);

        //Activo as chamadas a base de datos
        bd = new BaseDatos(this);
        bd.sqlLiteDB = bd.getWritableDatabase();

        inicializar();
        meterPreferencias();
        dialogoFragmento = new Fragment();

        boolean primeiraVez=preferencias.getBoolean("primeira",true);
        if(primeiraVez==true){
            FragmentManager fm = getSupportFragmentManager();
            dialogoFragmento.show(fm, getResources().getString(R.string.atencion));
        }

        SharedPreferences.Editor editor = preferencias.edit();
        editor.putBoolean("primeira", false);
        editor.commit();
        editor.apply();

        r=new Random();

        btnXerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xerarContrasinal();
            }
        });

        btnGardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gardarContrasinal();
            }
        });

        btnXestionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

    }

    public void inicializar(){
        vib=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        tvEntrada=(EditText) findViewById(R.id.idEntrada);
        tvSalida=(EditText) findViewById(R.id.idResultado);
        btnXerar=(Button) findViewById(R.id.idXerar);
        btnGardar=(Button) findViewById(R.id.idGardar);
        btnXestionar=(Button) findViewById(R.id.idXestionar);
        rbLetra=(RadioButton) findViewById(R.id.idCaracteres);
        rbNumero=(RadioButton) findViewById(R.id.idNumero);
        rbTodo=(RadioButton) findViewById(R.id.idComplex);
    }

    //Metodo que permite gardar contrasinais
    public void gardarContrasinal(){
        if(tvSalida.getText().toString().equals("") || tvSalida.getText()==null){
            if(vibrar){
                vib.vibrate(500);
            }
            Toast.makeText(getApplicationContext(), R.string.toast_gardar,Toast.LENGTH_SHORT).show();
        }else {
            Intent intent=new Intent(getBaseContext(),Gardar_Datos.class);
            intent.putExtra("pass",tvSalida.getText().toString());
            intent.putExtra("vibrar", vibrar);
            intent.putExtra("color", color);
            startActivity(intent);
        }
    }

    //Metodo que xera contrasinais
    public void xerarContrasinal(){
        if(tvEntrada.getText().toString().equals("") || tvEntrada.getText()==null){
            if(vibrar){
                vib.vibrate(500);
            }


            //Toast.makeText(getApplicationContext(), R.string.toast,Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar
                    .make(rel, R.string.toast, Snackbar.LENGTH_LONG);

            snackbar.show();
        }else {
            if(Integer.parseInt(tvEntrada.getText().toString())>25){
                Toast.makeText(getApplicationContext(),R.string.max_length, Toast.LENGTH_SHORT).show();
            }else{
                tvSalida.setVisibility(View.VISIBLE);
                if (rbNumero.isChecked()) conNumero();
                if (rbLetra.isChecked()) conLetra();
                if (rbTodo.isChecked()) conTodo();
            }
        }
    }

    //Metodo da pantalla inicial
    public void pantallaInicial(String pass){
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putBoolean("primeira", false);
        editor.putString("contrasinal", pass);
        editor.commit();
        editor.apply();
        meterPreferencias();
    }

    //Metodo que xera conntrasinais con numero
    public void conNumero(){
        lonxitude=Integer.parseInt(tvEntrada.getText().toString());
        tvSalida.setText("");
        for(int x=0;x<lonxitude;x++) {
            numero=r.nextInt(10)+48;
            char c=(char)numero;
            tvSalida.append(Character.toString(c));
        }
    }

    //Metodo que xera contrasinais con letra
    public void conLetra(){
        lonxitude=Integer.parseInt(tvEntrada.getText().toString());
        tvSalida.setText("");
        for(int x=0;x<lonxitude;x++) {
            numero=r.nextInt(2);
            if(numero==1) numero=r.nextInt(26)+65;
            if(numero==0) numero=r.nextInt(26)+97;
            char c=(char)numero;
            tvSalida.append(Character.toString(c));
        }
    }

    //Metodo que xera una contrasinal con numeros, letras e algun caracter especial
    public void conTodo(){
        lonxitude=Integer.parseInt(tvEntrada.getText().toString());
        tvSalida.setText("");
        for(int x=0;x<lonxitude;x++) {
            numero=r.nextInt(94)+33;
            char c=(char)numero;
            tvSalida.append(Character.toString(c));
        }
    }

    //Metodo que mostra os dialogos
    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case 1:
                // Primeiro preparamos o interior da ventá de diálogo inflando o seu
                // fichero XML
                String infService = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(infService);
                // Inflamos o compoñente composto definido no XML
                View inflador = li.inflate(R.layout.entrada_contrasinal, null);
                // Buscamos os compoñentes dentro do Diálogo


                venta = new AlertDialog.Builder(this);
                venta.setTitle(R.string.need_pass);
                // Asignamos o contido dentro do diálogo (o que inflamos antes)
                venta.setView(inflador);

                etContrasinal=(EditText) inflador.findViewById(R.id.keepTrying);
                etContrasinal.setText("");
                // Non se pode incluír unha mensaxe dentro deste tipo de diálogo!!!
                venta.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int boton) {
                        comprobarContrasinal();
                        etContrasinal.setText("");
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

    public void comprobarContrasinal(){
        if(contrasinal.equals(etContrasinal.getText().toString())){
            etContrasinal.setText("");
            startActivity(new Intent(getApplicationContext(), Listado.class).putExtra("color", color));
        }
        else{
            cont++;
            if(vibrar){
                vib.vibrate(500);
                Toast.makeText(getApplicationContext(),"Estou vibrando",Toast.LENGTH_SHORT).show();
            }

            if(cont==3){
                bd.eliminarTodo();
                cont=0;
                Toast.makeText(getApplicationContext(),"Todo borrado",Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(getApplicationContext(),R.string.incorrecto,Toast.LENGTH_SHORT).show();
        }
    }

    public void meterPreferencias(){
        preferencias=getSharedPreferences("preferenciasGlobais", MODE_PRIVATE);
        contrasinal=preferencias.getString("contrasinal", "abc123.");
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        color=pref.getString("toolbarColour","Azul");
        if (pref.getBoolean("vibrar", true)) {
            vibrar = true;
        }else{
            vibrar=false;
        }
        switch(color){
            case "Rojo":
                color="Rojo";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.rojo));
                break;
            case "Verde":
                color="Verde";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.verde));
                break;
            case "Amarillo":
                color="Amarillo";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.amarelo));
                break;
            case "Naranja":
                color="Naranja";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.naranja));
                break;
            default:
                color="Azul";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.color_boton));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
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

        if (id == R.id.idShare) {
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Mira la app que he encontrado...");
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.compartir)));
            return true;
        }

        if(id == R.id.idSettings){
            startActivity(new Intent(getBaseContext(),PantallaPreferencias.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        meterPreferencias();
        cont=0;
        tvEntrada.setText("");
    }

    /**Metodos da base de datos*/

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

    /**FIN Metodos da base de datos*/

}

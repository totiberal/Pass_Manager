package passgenerator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import java.util.Random;

public class Main extends AppCompatActivity {

    private EditText tvEntrada, tvSalida;
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
    private Fragment_Enter_Pass fragmentPass;

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
        fragmentPass=new Fragment_Enter_Pass();

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
                FragmentManager fm = getSupportFragmentManager();
                fragmentPass.show(fm, getResources().getString(R.string.need_pass));
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
            Snackbar.make(rel, R.string.toast_gardar, Snackbar.LENGTH_LONG).show();
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
            Snackbar.make(rel, R.string.toast, Snackbar.LENGTH_LONG).show();
        }else {
            if(Integer.parseInt(tvEntrada.getText().toString())>25){
                Snackbar.make(rel, R.string.max_length,Snackbar.LENGTH_LONG).show();
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

    public void comprobarContrasinal(String password){
        if(contrasinal.equals(password)){
            startActivity(new Intent(getApplicationContext(), Listado.class).putExtra("color", color));
        }
        else{
            cont++;
            if(vibrar){
                vib.vibrate(500);
            }

            if(cont==3){
                bd.eliminarTodo();
                cont=0;
                Snackbar.make(rel, R.string.limite_fallos, Snackbar.LENGTH_LONG).show();
            }
            else Snackbar.make(rel, R.string.incorrecto, Snackbar.LENGTH_LONG).show();
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
                btnXerar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_rojo));
                btnXestionar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_rojo));
                btnGardar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_rojo));
                break;
            case "Verde":
                color="Verde";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.verde));
                btnXerar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_verde));
                btnXestionar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_verde));
                btnGardar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_verde));
                break;
            case "Amarillo":
                color="Amarillo";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.amarelo));
                btnXerar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_amarelo));
                btnXestionar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_amarelo));
                btnGardar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_amarelo));
                break;
            case "Naranja":
                color="Naranja";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.naranja));
                btnXerar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_naranja));
                btnXestionar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_naranja));
                btnGardar.setBackground(getResources().getDrawable(R.drawable.estilo_boton_naranja));
                break;
            default:
                color="Azul";
                mToolbar.setBackgroundColor(getResources().getColor(R.color.color_boton));
                btnXerar.setBackground(getResources().getDrawable(R.drawable.estilo_botones));
                btnXestionar.setBackground(getResources().getDrawable(R.drawable.estilo_botones));
                btnGardar.setBackground(getResources().getDrawable(R.drawable.estilo_botones));
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
    protected void onRestart() {
        super.onRestart();
        tvSalida.setText("");
    }

    @Override
    public void onResume(){
        super.onResume();
        meterPreferencias();
        cont=0;
        tvEntrada.setText("");
        tvSalida.setText("");
        if(tvSalida!=null) tvSalida.setVisibility(View.GONE);
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

//http://androideity.com/2016/02/15/material-design-recycler-view-parte-2/
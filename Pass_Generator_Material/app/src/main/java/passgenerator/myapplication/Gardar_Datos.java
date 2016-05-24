package passgenerator.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        tvContrasinal=(TextView) findViewById(R.id.idPassDef);
        tvContrasinal.setText(pass);
        tvContrasinal.setTextSize(25);
        etServizo=(EditText) findViewById(R.id.idNomeServ);
        etUsuario=(EditText) findViewById(R.id.idNomeUsuario);
        btnGardarDef=(Button) findViewById(R.id.idGardarDef);
        btnAtras=(Button) findViewById(R.id.idAtras);

        btnGardarDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etServizo.getText().toString().equals("") || etServizo.getText()==null){
                    Toast.makeText(Gardar_Datos.this, R.string.falta_servicio, Toast.LENGTH_SHORT).show();
                    if(vibrar){
                        vib.vibrate(500);
                    }
                }else if(etUsuario.getText().toString().equals("") || etUsuario.getText()==null){
                    Toast.makeText(Gardar_Datos.this, R.string.falta_usuario, Toast.LENGTH_SHORT).show();
                    if(vibrar){
                        vib.vibrate(500);
                    }
                }else if(bd.consulta(etServizo.getText().toString())){
                    Toast.makeText(Gardar_Datos.this, R.string.toast_existe, Toast.LENGTH_SHORT).show();
                    if(vibrar){
                        vib.vibrate(500);
                    }
                }
                else {
                    String encriptadoPass = Utilidades.Encriptar(tvContrasinal.getText().toString());
                    String encriptadoUser=Utilidades.Encriptar(etUsuario.getText().toString());
                    bd.engadir(new Pares(etServizo.getText().toString(), encriptadoUser, encriptadoPass));
                    Toast.makeText(Gardar_Datos.this, R.string.toast_correcto, Toast.LENGTH_SHORT).show();
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

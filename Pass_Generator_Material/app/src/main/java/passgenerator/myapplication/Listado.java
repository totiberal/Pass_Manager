package passgenerator.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Listado extends AppCompatActivity {

    private AlertDialog.Builder venta;
    private SharedPreferences preferencias;
    private EditText etContrasinal;
    private Toolbar mToolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String color;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        mToolbar = (Toolbar) findViewById(R.id.appbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setLogo(R.drawable.ic_launcher);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        intent=getIntent();
        color=intent.getExtras().getString("color", "Azul");

        seleccionTema();

        preferencias=getSharedPreferences("preferenciasGlobais", MODE_PRIVATE);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ContrasinaisFragment(), "Passwords");
        adapter.addFragment(new FicheirosFragment(), "Backup");
        viewPager.setAdapter(adapter);
    }

    public void seleccionTema(){
        switch(color){
            case "Rojo":
                color="Rojo";
                tabLayout.setBackgroundColor(getResources().getColor(R.color.rojo));
                break;
            case "Verde":
                color="Verde";
                tabLayout.setBackgroundColor(getResources().getColor(R.color.verde));
                break;
            case "Amarillo":
                color="Amarillo";
                tabLayout.setBackgroundColor(getResources().getColor(R.color.amarelo));
                break;
            case "Naranja":
                color="Naranja";
                tabLayout.setBackgroundColor(getResources().getColor(R.color.naranja));
                break;
            default:
                color="Azul";
                tabLayout.setBackgroundColor(getResources().getColor(R.color.color_boton));
                break;
        }
    }

    protected Dialog onCreateDialog(int id) {

        if(id==3){
            String infService = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(infService);
            // Inflamos o compoñente composto definido no XML
            View inflador = li.inflate(R.layout.entrada_contrasinal, null);
            // Buscamos os compoñentes dentro do Diálogo


            venta = new AlertDialog.Builder(this);
            venta.setTitle(R.string.password);
            venta.setMessage(R.string.nova_pass);
            // Asignamos o contido dentro do diálogo (o que inflamos antes)
            venta.setView(inflador);
            TextView texto=(TextView) inflador.findViewById(R.id.tvEntrada_Contrasinal);
            etContrasinal=(EditText) inflador.findViewById(R.id.keepTrying);
            etContrasinal.setText("");
            texto.setText("");
            // Non se pode incluír unha mensaxe dentro deste tipo de diálogo!!!
            venta.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int boton) {
                    cambiarContrasinal(etContrasinal.getText().toString());
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

    //Metodo para cambiar o contrasinal de superusuario
   public void cambiarContrasinal(String novaPass){
        etContrasinal .setText("");
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("contrasinal", novaPass);
        editor.commit();
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listado, menu);
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

        if(id==R.id.idCambiarPass){
            showDialog(3);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        seleccionTema();
    }
}

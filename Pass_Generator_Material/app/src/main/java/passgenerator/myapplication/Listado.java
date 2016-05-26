package passgenerator.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Listado extends AppCompatActivity {

    private AlertDialog.Builder venta;
    private SharedPreferences preferencias;
    private EditText etContrasinal;
    private Toolbar mToolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static String color;
    private Intent intent;
    private CoordinatorLayout cor;
    private Fragment_Password dialogoFragmento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        mToolbar = (Toolbar) findViewById(R.id.appbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setLogo(R.drawable.ic_launcher);
        cor=(CoordinatorLayout) findViewById(R.id.idCoordinator);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        dialogoFragmento=new Fragment_Password();
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        intent=getIntent();
        color=intent.getExtras().getString("color", "Azul");

        seleccionTema();

        preferencias=getSharedPreferences("preferenciasGlobais", MODE_PRIVATE);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ContrasinaisFragment c=new ContrasinaisFragment();
        Bundle b=new Bundle();
        b.putString("color",color);
        c.setArguments(b);
        adapter.addFragment(c, "Passwords");
        adapter.addFragment(new FicheirosFragment(), "Backup");
        viewPager.setAdapter(adapter);
    }

    public void seleccionTema(){
        switch(color){
            case "Rojo":
                color="Rojo";
                tabLayout.setBackgroundColor(getResources().getColor(R.color.rojo));
                mToolbar.setBackgroundColor(getResources().getColor(R.color.rojo));
                break;
            case "Verde":
                color="Verde";
                tabLayout.setBackgroundColor(getResources().getColor(R.color.verde));
                mToolbar.setBackgroundColor(getResources().getColor(R.color.verde));
                break;
            case "Amarillo":
                color="Amarillo";
                tabLayout.setBackgroundColor(getResources().getColor(R.color.amarelo));
                mToolbar.setBackgroundColor(getResources().getColor(R.color.amarelo));
                break;
            case "Naranja":
                color="Naranja";
                tabLayout.setBackgroundColor(getResources().getColor(R.color.naranja));
                mToolbar.setBackgroundColor(getResources().getColor(R.color.naranja));
                break;
            default:
                color="Azul";
                tabLayout.setBackgroundColor(getResources().getColor(R.color.color_boton));
                mToolbar.setBackgroundColor(getResources().getColor(R.color.color_boton));
                break;
        }
    }

    //Metodo para cambiar o contrasinal de superusuario
   public void cambiarContrasinal(String passVella, String novaPass){
       String contrasinal=preferencias.getString("contrasinal", "fallo aqui");
       if (contrasinal.equals(passVella)) {
           SharedPreferences.Editor editor=preferencias.edit();
           editor.putString("contrasinal", novaPass);
           editor.commit();
           editor.apply();
            Snackbar.make(cor, R.string.cambiada, Snackbar.LENGTH_LONG).show();
       }else{
           Snackbar.make(cor, R.string.noncambiada, Snackbar.LENGTH_LONG).show();
       }
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
            FragmentManager fm = getSupportFragmentManager();
            dialogoFragmento.show(fm, getResources().getString(R.string.pass_nova));
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

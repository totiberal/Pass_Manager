package passgenerator.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by alberto.alvarez on 20/05/16.
 */
public class FicheirosFragment extends Fragment {

    RecyclerView lvFiles;
    MiAdaptador adaptadorFiles=null;
    AppCompatButton btnGardar;
    ImageButton btnAtras;
    File file;
    File[] fileList;
    String rutaCompleta;
    RelativeLayout rel;

    public FicheirosFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.listado_ficheiros,container,false);

        rel=(RelativeLayout) v.findViewById(R.id.rel);
        lvFiles=(RecyclerView) v.findViewById(R.id.idListaFiles);
        btnGardar=(AppCompatButton) v.findViewById(R.id.idBotonGardar);
        btnAtras=(ImageButton) v.findViewById(R.id.idBotonAtras);
        file= Environment.getRootDirectory().getParentFile();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        lvFiles.setLayoutManager(mLayoutManager);
        lvFiles.setItemAnimator(new DefaultItemAnimator());
        //lvFiles.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        prepararListaFiles();

        lvFiles.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), lvFiles, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                comprobar(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));



        btnGardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarBackup();
            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directorioAnterior();
            }
        });

        return v;
    }

    //Metodo para realizar copias de seguridade
    public void realizarBackup(){
        try{
            Backup backup=new Backup();
            backup.copiaSeguridade(file.getAbsolutePath().toString());
            adaptadorFiles.notifyDataSetChanged();
            prepararListaFiles();
        }catch(NullPointerException ex){
            ex.printStackTrace();
            Snackbar.make(rel, R.string.imposible_gardar,Snackbar.LENGTH_LONG).show();
        }

    }

    //Preparo o adaptados do explorador de ficheiros
    public void prepararListaFiles(){
        lvFiles.setAdapter(null);
        fileList=file.listFiles();
        String listaDef[]=new String[fileList.length];
        ArrayList<Items> arrayL=new ArrayList<>();
        ArrayList<File> listaArquivos=new ArrayList<>();
        ArrayList<File> listaCarpetas=new ArrayList<>();
        ArrayList<File> total=new ArrayList<>();
        for(int x=0;x<fileList.length;x++){
            if(fileList[x].isDirectory()) listaCarpetas.add(fileList[x]);
            if(fileList[x].isFile()) listaArquivos.add(fileList[x]);
        }
        for(int x=0;x<listaCarpetas.size();x++){
            total.add(listaCarpetas.get(x));
        }
        for(int x=0;x<listaArquivos.size();x++){
            total.add(listaArquivos.get(x));
        }
        fileList=null;
        fileList=new File[total.size()];
        for(int x=0;x<total.size();x++){
            fileList[x]=total.get(x);
        }
        for(int x=0;x<fileList.length;x++){
            listaDef[x]=fileList[x].toString();
            String dividido=listaDef[x].substring(listaDef[x].lastIndexOf("/"));
            listaDef[x]=dividido;
            if(fileList[x].isDirectory()) {
                arrayL.add(new Items(R.drawable.folder, dividido));
            }
            else if(fileList[x].isFile()){
                arrayL.add(new Items(R.drawable.file, dividido));
            }

        }
        adaptadorFiles=new MiAdaptador(arrayL);
        lvFiles.setAdapter(adaptadorFiles);
    }

    //Metodo que comproba se o ficheiro se trata dun cartafol ou dun arquivo
    public void comprobar(int posicion){
        try{
            if(fileList[posicion].isFile()) Toast.makeText(getContext(),"arquivo",Toast.LENGTH_SHORT).show();
            else if(fileList[posicion].isDirectory()){
                rutaCompleta=fileList[posicion].toString();
                //Toast.makeText(getContext(),""+rutaCompleta,Toast.LENGTH_SHORT).show();
                file=new File(rutaCompleta);
                prepararListaFiles();
            }
        }catch(NullPointerException ex){
            //Toast.makeText(getBaseContext(),"carpeta oculta, no puedes acceder",Toast.LENGTH_SHORT).show();
            Log.e("CONTROLADO", "Directorio oculto");
        }

    }

    //Metodo que nos permite volver o dierctorio anterior
    public void directorioAnterior(){
        try{
            rutaCompleta=file.getParentFile().getAbsolutePath();
            file=new File(rutaCompleta);
            prepararListaFiles();
        }catch(NullPointerException ex){
            //Toast.makeText(getContext(),"fin", Toast.LENGTH_SHORT).show();
            Log.e("CONTROLADO","Non se pode ir mais cara atras");
        }
    }

    //Implementacion dos eventos click do RecyclerView
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private FicheirosFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FicheirosFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}

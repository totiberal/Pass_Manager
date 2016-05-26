package passgenerator.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by alberto.alvarez on 20/05/16.
 */
public class ContrasinaisFragment extends Fragment {

    RecyclerView lv;
    MiAdaptador adaptadorFiles=null;
    BaseDatos bd;
    ArrayList<Pares> arrayL;
    String dato;
    static int num;
    AlertDialog.Builder venta;
    ArrayList<String> servizos;
    ArrayList<Items> arrayFilas;
    File pdfFile;
    String  ruta;
    public static AdaptadorLista adaptador;
    FloatingActionButton btnFlotante;
    private String color;
    private BorrarFragment borrarFragment;
    private Fragment_Change_Data fragmentChangeData;

    public ContrasinaisFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararAdaptador();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.listado_contrasinais,container,false);
        btnFlotante=(FloatingActionButton) v.findViewById(R.id.fab);
        color=getArguments().getString("color", "Azul");

        lv=(RecyclerView) v.findViewById(R.id.idLista);

        bd = new BaseDatos(getActivity());
        bd.sqlLiteDB = bd.getWritableDatabase();
        borrarFragment=new BorrarFragment();
        fragmentChangeData=new Fragment_Change_Data();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        lv.setLayoutManager(mLayoutManager);
        lv.setItemAnimator(new DefaultItemAnimator());
        // lv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        prepararAdaptador();
        operacionsPDF();
        registerForContextMenu(lv);

        btnFlotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPdf();
            }
        });

        lv.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), lv, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                num = position;
                String[] mandar = {arrayL.get(position).getServizo(), arrayL.get(position).getUsuario(), arrayL.get(position).getContrasinal()};
                Intent intent = new Intent(getContext(), MostrarDatos.class);
                intent.putExtra("datos", mandar);
                intent.putExtra("color", color);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                num = position;
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.inflate(R.menu.menu_contextual);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            // Ítems premidos sobre o TextView
                            // Lanza un Toast coa opción do menú contextual que se seleccinou
                            case R.id.idMenuBorrar:
                                abrirFragmentBorrar();
                                adaptador.notifyDataSetChanged();
                                prepararAdaptador();
                                return true;

                            case R.id.idMenuEditar:
                                modificarDatosFrgament();
                                adaptador.notifyDataSetChanged();
                                prepararAdaptador();
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        }));

        return v;
    }



    //Cando se inicia a aplicacion lanzase o fio paa xerar pdf
    public void operacionsPDF(){
        Thread fio = new Thread(){
            public void run(){
                xerarPDF();
            }       // FIN DO RUN
        };
        fio.start();
        try{
            fio.join();
        }catch(Exception ex){
            Log.e("FIOS","Erro xerando o PDF");
        }
    }

    //Metodo que comproba que haxa unha aplicacion capaz de abrir pdf
    public void mostrarPdf(){
        File file = new File(ruta);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //Se captura esta excepcion debido a que o usuario non ten ningun tipo de lector de pdf instalado
        }
    }

    //Metodo que xera un pdf cada vez que se inicia a activity
    public void xerarPDF(){
        ArrayList<Pares> arrayPdf=bd.obter();
        try {
            pdfFile=new File(Environment.getExternalStorageDirectory()+"/PDF/");
            if(pdfFile.exists()) pdfFile.delete();
            ruta=pdfFile.getAbsolutePath()+"informePdf.pdf";
            if(!pdfFile.exists()) pdfFile.mkdirs();
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(ruta));
            XerarPDF xerar=new XerarPDF();
            document.open();
            xerar.addTitlePage(document);
            xerar.crearTabla(document, arrayPdf);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Metodo que prepara automaticamente os datos do listview cos servizos
    public void prepararAdaptador(){
        try {
            arrayL = bd.obter();
            servizos = new ArrayList<>();
            for (Pares p : arrayL) {
                servizos.add(p.getServizo());
            }
            arrayFilas = new ArrayList<>();
            for (int x = 0; x < arrayL.size(); x++) {
                if (servizos.get(x).equalsIgnoreCase("Facebook"))
                    arrayFilas.add(new Items(R.drawable.facebook, servizos.get((x))));
                else if (servizos.get(x).equalsIgnoreCase("Twitter"))
                    arrayFilas.add(new Items(R.drawable.twitter, servizos.get((x))));
                else if (servizos.get(x).equalsIgnoreCase("Gmail"))
                    arrayFilas.add(new Items(R.drawable.gmail, servizos.get((x))));
                else if (servizos.get(x).equalsIgnoreCase("Tibia"))
                    arrayFilas.add(new Items(R.drawable.tibia, servizos.get((x))));
                else arrayFilas.add(new Items(R.drawable.notfound, servizos.get(x)));
            }
            adaptador = new AdaptadorLista(arrayFilas);
            lv.setAdapter(adaptador);
        }catch(Exception ex){}
    }

    /**Metodos de acceso a base de datos**/

    @Override
    public void onStart(){
        super.onStart();

        if (bd==null) {   // Abrimos a base de datos para lectura
            bd = new BaseDatos(getContext());
            bd.sqlLiteDB = bd.getWritableDatabase();
        }
        adaptador.notifyDataSetChanged();
        prepararAdaptador();
    }

    @Override
    public void onStop(){
        super.onStop();

        if (bd!=null){    // Pechamos a base de datos.
            bd.close();
            bd=null;
        }
    }
    /**Fin metodos acceso a base de datos **/

    //Metodo que recarga o adaptador cando se volve a activity
    @Override
    public void onResume(){
        super.onResume();
        adaptador.notifyDataSetChanged();
    }

    //Implementacion dos eventos click do RecyclerView
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ContrasinaisFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ContrasinaisFragment.ClickListener clickListener) {
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

    public void modificarDatosFrgament(){
        Bundle b=new Bundle();
        b.putString("modificar", arrayL.get(num).getServizo());
        fragmentChangeData.setArguments(b);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fragmentChangeData.show(fm, getResources().getString(R.string.cambiar_datos));
    }

    public void abrirFragmentBorrar(){
        Bundle b=new Bundle();
        b.putString("borrar", arrayL.get(num).getServizo());
        borrarFragment.setArguments(b);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        borrarFragment.show(fm, getResources().getString(R.string.atencion));
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        prepararAdaptador();
    }
}

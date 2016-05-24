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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ListView;
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
    AdaptadorLista adaptador;
    FloatingActionButton btnFlotante;

    public ContrasinaisFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.listado_contrasinais,container,false);
        btnFlotante=(FloatingActionButton) v.findViewById(R.id.fab);
        lv=(RecyclerView) v.findViewById(R.id.idLista);

        bd = new BaseDatos(getActivity());
        bd.sqlLiteDB = bd.getWritableDatabase();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        lv.setLayoutManager(mLayoutManager);
        lv.setItemAnimator(new DefaultItemAnimator());
        lv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
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
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                num = position;
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
    public void borrar(){
        Pares p = arrayL.get(num);
        bd.borrar(p.getServizo());
        adaptador.notifyDataSetChanged();
        prepararAdaptador();
    }

    public void cambios(String dato){
        Pares p=arrayL.get(num);
        bd.modificar(p.getServizo(), dato);
        adaptador.notifyDataSetChanged();
        prepararAdaptador();
    }

    @Override
    public void onStart(){
        super.onStart();

        if (bd==null) {   // Abrimos a base de datos para lectura
            bd = new BaseDatos(getContext());
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
    /**Fin metodos acceso a base de datos **/


    /** Ventas de dialogo para editar e borrar**/
    protected Dialog onCreateDialog(int id) {
        if(id==1) {
            venta = new AlertDialog.Builder(getActivity());
            venta.setIcon(android.R.drawable.ic_dialog_info);
            venta.setTitle(R.string.alerta);
            venta.setMessage(R.string.mensaxe);
            venta.setCancelable(false);
            venta.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int boton) {
                    borrar();
                }
            });
            venta.setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int boton) {

                }
            });

            return venta.create();
        }

        if(id==2){
            // Primeiro preparamos o interior da ventá de diálogo inflando o seu
            // fichero XML
            String infService = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
            // Inflamos o compoñente composto definido no XML
            View inflador = li.inflate(R.layout.entrada_texto, null);
            // Buscamos os compoñentes dentro do Diálogo
            final EditText etNome = (EditText) inflador.findViewById(R.id.et_nome);
            etNome.setText("");
            venta = new AlertDialog.Builder(getActivity());
            venta.setTitle(R.string.elixe);
            venta.setMessage(R.string.pass_service);
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

    /**  FIN Ventas de dialogo para editar e borrar**/

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_contextual, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            ArrayAdapter<String> adaptador = (ArrayAdapter<String>) lv.getAdapter();

            switch (item.getItemId()) {

                // Ítems premidos sobre o TextView
                // Lanza un Toast coa opción do menú contextual que se seleccinou
                case R.id.idMenuBorrar:
                    getActivity().showDialog(2);
                    prepararAdaptador();
                    adaptador.notifyDataSetChanged();
                    return true;

                case R.id.idMenuEditar:
                    getActivity().showDialog(2);
                    return true;

                default:
                    return super.onContextItemSelected(item);
            }
        }
        return super.onContextItemSelected(item);
    }*/

    //Metodo que recarga o adaptador cando se volve a activity
    @Override
    public void onResume(){
        super.onResume();
        prepararAdaptador();
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


}

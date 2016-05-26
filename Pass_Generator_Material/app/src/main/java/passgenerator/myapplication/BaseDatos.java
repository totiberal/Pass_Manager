package passgenerator.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by alberto on 24/03/2016.
 */
public class BaseDatos extends SQLiteOpenHelper {

    public SQLiteDatabase sqlLiteDB;
    public final static String NOME_BD="basedatos";
    public final static int VERSION_BD=1;
    private String CREAR_TABOA ="CREATE TABLE Contrasinais ( " +
            "id  INTEGER PRIMARY KEY AUTOINCREMENT," +
            "servizo VARCHAR( 50 )  NOT NULL, usuario VARCHAR(50) NOT NULL, contrasinal VARCHAR (50) NOT NULL)";
    private final String TABOA="Contrasinais";
    private final String CONSULTAR ="SELECT * FROM Contrasinais order by servizo";

    public BaseDatos(Context context) {
        super(context, NOME_BD, null, VERSION_BD);
        // TODO Auto-generated constructor stub
    }

    //Metodo que se executa a primeira vez que se instala a aplicacion
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABOA);
    }

    //Metodo que se executa en posibles actualizacions
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Metodo para engadir
    public long engadir(Pares par_engadir){
        ContentValues valores = new ContentValues();
        valores.put("servizo", par_engadir.getServizo());
        valores.put("usuario", par_engadir.getUsuario());
        valores.put("contrasinal", par_engadir.getContrasinal());
        long id = sqlLiteDB.insert("Contrasinais",null,valores);

        return id;
    }

    //Metodo para comprobar existencia antes de insertar
    public boolean consulta(String texto){
        String[] parametros = new String[]{texto};
        Cursor cursor = sqlLiteDB.rawQuery("select count(*) from Contrasinais where servizo = ?", parametros);
        long id=0;
        if (cursor.moveToFirst()) {                // Se non ten datos xa non entra
            while (!cursor.isAfterLast()) {     // Quédase no bucle ata que remata de percorrer o cursor. Fixarse que leva un ! (not) diante
                id =cursor.getLong(0);
                cursor.moveToNext();
            }
        }
        if(id>0) return true;
        return  false;
    }

    //Metodo para devolver o servizo, o usuario e o contrasinal
    public ArrayList<Pares> obter() {
        ArrayList<Pares> devolver = new ArrayList<Pares>(0);

        Cursor datosConsulta = sqlLiteDB.rawQuery(CONSULTAR, null);
        if (datosConsulta.moveToFirst()) {
            Pares p;
            while (!datosConsulta.isAfterLast()) {
                p = new Pares(datosConsulta.getString(1),
                        datosConsulta.getString(2),
                        datosConsulta.getString(3));
                devolver.add(p);
                datosConsulta.moveToNext();
            }
        }

        return devolver;
    }

    //Metodo que elimina
    public void borrar(String dato){
        String condicionwhere = "servizo=?";
        String[] parametros = new String[]{dato};
        int rexistrosafectados = sqlLiteDB.delete(TABOA,condicionwhere,parametros);
    }

    //Metodo que permite modificar os datos
    public void modificar(String dato, String novaPass, String novoUser){
        ContentValues datosexemplo = new ContentValues();
        datosexemplo.put("usuario", Utilidades.Encriptar(novoUser));
        datosexemplo.put("contrasinal", Utilidades.Encriptar(novaPass));
        String condicionwhere = "servizo=?";
        String[] parametros = new String[]{dato};
        int rexistrosafectados = sqlLiteDB.update(TABOA,datosexemplo,condicionwhere,parametros);
    }

    //Metodo que elimina todos os datos da base de datos
    public void eliminarTodo(){
        sqlLiteDB.execSQL("delete from Contrasinais");
    }

}

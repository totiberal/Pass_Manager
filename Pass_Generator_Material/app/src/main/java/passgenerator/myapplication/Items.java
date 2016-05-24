package passgenerator.myapplication;

/**
 * Created by alberto.alvarez on 10/05/16.
 */
public class Items {
    /***********************CLASE XERAL, COMUN PARA TODOS OS MEUS ADAPTADORES***************************/
    private int imaxe;
    private String ruta;

    public Items(int imaxe, String ruta) {
        super();
        this.imaxe = imaxe;
        this.ruta = ruta;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getImaxe() {
        return imaxe;

    }

    public void setImaxe(int imaxe) {
        this.imaxe = imaxe;
    }


}

package passgenerator.myapplication;

/**
 * Created by alberto on 24/03/2016.
 */
public class Pares {

    private String servizo, contrasinal, usuario;

    public Pares(){}

    public Pares(String servizo, String contrasinal){
        this.servizo=servizo;
        this.contrasinal=contrasinal;
    }

    public Pares(String servizo, String usuario, String contrasinal){
        this.servizo=servizo;
        this.usuario=usuario;
        this.contrasinal=contrasinal;
    }

    public String getUsuario(){
        return usuario;
    }

    public void setUsuario(String usuario){
        this.usuario=usuario;
    }

    public String getServizo() {
        return servizo;
    }

    public void setServizo(String servizo) {
        this.servizo = servizo;
    }

    public String getContrasinal() {
        return contrasinal;
    }

    public void setContrasinal(String contrasinal) {
        this.contrasinal = contrasinal;
    }
}

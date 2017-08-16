package br.com.editorapendragon.wazu;

/**
 * Created by Josué on 09/05/2016.
 */
public class Alertas {
    public String id;
    public String id_tipo_alerta;
    public String tipo_alerta;
    public String id_usuario;
    public String usuario;
    public String idata;
    public String hora;
    public String lat;
    public String lng;
    public String comentario;

    public Alertas(String id, String id_tipo_alerta, String tipo_alerta, String id_usuario, String usuario, String idata, String hora, String lat, String lng, String comentario) {
        this.id = id;
        this.id_tipo_alerta = id_tipo_alerta;
        this.id_usuario = id_usuario;
        this.usuario = usuario;
        this.idata = idata;
        this.hora = hora;
        this.lat = lat;
        this.lng = lng;
        this.tipo_alerta = tipo_alerta;
        this.comentario = comentario;
    }
}

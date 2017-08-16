package br.com.editorapendragon.wazu;


import android.graphics.Bitmap;

/**
 * Created by Josué on 09/05/2016.
 */
public class Route {
    public String nome;
    public String icone;
    public Double lat;
    public Double lng;

    public Route(String nome, String icone, Double lat, Double lng) {
        this.nome = nome;
        this.icone = icone;
        this.lat = lat;
        this.lng = lng;
    }
}

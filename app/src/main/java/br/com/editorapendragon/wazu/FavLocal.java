package br.com.editorapendragon.wazu;

/**
 * Created by Josué on 05/05/2016.
 */
public class FavLocal {

    private int id;
    private String nome;
    private double lat;
    private double lng;

    public FavLocal() {}

    public FavLocal (int id, String nome,double lat, double lng) {
        this.id = id;
        this.nome = nome;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return  "'"+id+"'";
    }
}

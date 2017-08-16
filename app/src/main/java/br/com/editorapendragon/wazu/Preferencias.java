package br.com.editorapendragon.wazu;

/**
 * Created by Josué on 05/05/2016.
 */
public class Preferencias {
    private int trafego;
    private float distancia;
    private double dultlat;
    private double dultlng;
    private double sultlat;
    private double sultlng;
    private int som;
    private int vibra;


    public Preferencias() {}

    public Preferencias (int trafego, float distancia,double dultlat, double dultlng,double sultlat, double sultlng,int som,int vibra) {
        this.trafego = trafego;
        this.distancia = distancia;
        this.dultlat = dultlat;
        this.dultlng = dultlng;
        this.sultlat = sultlat;
        this.sultlng = sultlng;
        this.som = som;
        this.vibra = vibra;

    }

    public int getTrafego() {
        return trafego;
    }

    public void setTrafego(int trafego) {
        this.trafego = trafego;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public double getDultlat() {
        return dultlat;
    }

    public void setDultlat(double dultlat) {
        this.dultlat = dultlat;
    }

    public double getDultlng() {
        return dultlng;
    }

    public void setDultlng(double dultlng) {
        this.dultlng = dultlng;
    }

    public double getSultlat() {
        return sultlat;
    }

    public void setSultlat(double sultlat) {
        this.sultlat = sultlat;
    }

    public double getSultlng() {
        return sultlng;
    }

    public void setSultlng(double sultlng) {
        this.sultlng = sultlng;
    }

    public int getSom() {
        return som;
    }

    public void setSom(int som) {
        this.som = som;
    }

    public int getVibra() {
        return vibra;
    }

    public void setVibra(int vibra) {
        this.vibra = vibra;
    }
}


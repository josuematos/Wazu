package br.com.editorapendragon.wazu;

/**
 * Created by Josué on 05/05/2016.
 */
public class FavOnibus {

    private int id;
    private String numero;
    private String linha;
    private String tipo;

    public FavOnibus() {}

    public FavOnibus(int id, String numero, String linha, String tipo) {
        this.id = id;
        this.numero = numero;
        this.linha = linha;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return  "'"+id+"'";
    }
}

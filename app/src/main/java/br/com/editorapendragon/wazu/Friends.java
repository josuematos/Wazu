package br.com.editorapendragon.wazu;


import android.graphics.Bitmap;
import android.media.Image;

/**
 * Created by Josué on 09/05/2016.
 */
public class Friends {
    public String nome;
    public Bitmap foto;
    public String id;

    public Friends(String nome, Bitmap foto, String id) {
        this.nome = nome;
        this.id = id;
        this.foto = foto;
    }
}

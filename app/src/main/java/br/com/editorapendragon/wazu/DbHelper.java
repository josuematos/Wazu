package br.com.editorapendragon.wazu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josué on 05/05/2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "wazu";
    private static final int VERSAO = 1;

    public DbHelper(Context context){
        super(context, NOME_BANCO,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE cadastro ( id text, nome text, email text,versao text)";
        db.execSQL(sql);

        sql = "insert into cadastro (versao) values ('gratuita')";
        db.execSQL(sql);

        sql = "CREATE TABLE preferencias ( trafego int, distancia float, dultlat double, dultlng double,sultlat double, sultlng double, som int, vibra int)";
        db.execSQL(sql);

        sql = "insert into preferencias (trafego,distancia,som,vibra) values (0,1000,1,1)";
        db.execSQL(sql);

        sql = "CREATE TABLE localfavorito ( id integer primary key autoincrement, nome text, lat double,lng double)";
        db.execSQL(sql);

        sql = "CREATE TABLE onibusfavorito ( id integer primary key autoincrement, numero text, linha text,tipo text)";
        db.execSQL(sql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cadastro");
        db.execSQL("DROP TABLE IF EXISTS preferencias");
        db.execSQL("DROP TABLE IF EXISTS localfavorito");
        db.execSQL("DROP TABLE IF EXISTS onibusfavorito");
        onCreate(db);

    }

    public void insertFavLocal(FavLocal favlocal) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("nome",favlocal.getNome());
        cv.put("lat",favlocal.getLat());
        cv.put("lng",favlocal.getLng());

        db.insert("localfavorito",null,cv);
        db.close();
    }

    public void updateFavLocal(FavLocal favlocal) {

        SQLiteDatabase db = getWritableDatabase();

        String where = "id = ?";
        //String id =  favlocal.getId();
        String[] whereArgs = new String[] {favlocal.toString()};

        ContentValues cv = new ContentValues();
        cv.put("nome",favlocal.getNome());
        cv.put("lat",favlocal.getLat());
        cv.put("lng",favlocal.getLng());

        db.update("localfavorito",cv,where,whereArgs);
        db.close();
    }

    public void deleteFavLocal(String id) {

        SQLiteDatabase db = getWritableDatabase();

        String where = "id = ?";
        String[] whereArgs = new String[] {id};

        db.delete("localfavorito",where,whereArgs);
        db.close();
    }

    public List<FavLocal> selectFavLocais(){
        List<FavLocal> listFavLocal = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String sqlSelect = "Select * from localfavorito order by nome";

        Cursor c = db.rawQuery(sqlSelect, null);

        if (c.moveToFirst()){
            do {
                FavLocal favlocal = new FavLocal();
                favlocal.setId(c.getInt(0));
                favlocal.setNome(c.getString(1));
                favlocal.setLat(c.getDouble(2));
                favlocal.setLng(c.getDouble(3));

                listFavLocal.add(favlocal);
            } while (c.moveToNext());

        }
        c.close();
        db.close();
        return listFavLocal;
    }

    public List<Preferencias> selectPreferencias(){
        List<Preferencias> listPreferencias = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String sqlSelect = "Select * from preferencias";

        Cursor c = db.rawQuery(sqlSelect, null);

        if (c.moveToFirst()){
            do {
                Preferencias prefs = new Preferencias();
                prefs.setTrafego(c.getInt(0));
                prefs.setDistancia(c.getFloat(1));
                prefs.setDultlat(c.getDouble(2));
                prefs.setDultlng(c.getDouble(3));
                prefs.setSultlat(c.getDouble(4));
                prefs.setSultlng(c.getDouble(5));
                prefs.setSom(c.getInt(6));
                prefs.setVibra(c.getInt(7));

                listPreferencias.add(prefs);
            } while (c.moveToNext());

        }
        c.close();
        db.close();
        return listPreferencias;
    }

    public void updatePrefs(Preferencias prefs) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("trafego",prefs.getTrafego());
        cv.put("distancia",prefs.getDistancia());
        cv.put("dultlat",prefs.getDultlat());
        cv.put("dultlng",prefs.getDultlng());
        cv.put("sultlat",prefs.getSultlat());
        cv.put("sultlng",prefs.getSultlng());
        cv.put("som",prefs.getSom());
        cv.put("vibra",prefs.getVibra());

        db.update("preferencias",cv,null,null);
        db.close();
    }

    public List<Cadastro> selectCadastro(){
        List<Cadastro> listCadastro = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String sqlSelect = "Select * from cadastro";

        Cursor c = db.rawQuery(sqlSelect, null);

        if (c.moveToFirst()){
            do {
                Cadastro cad = new Cadastro();
                cad.setId(c.getString(0));
                cad.setNome(c.getString(1));
                cad.setEmail(c.getString(2));
                cad.setVersao(c.getString(3));

                listCadastro.add(cad);
            } while (c.moveToNext());

        }
        c.close();
        db.close();
        return listCadastro;
    }

    public void updateCad(Cadastro cad) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("id",cad.getId());
        cv.put("nome",cad.getNome());
        cv.put("email",cad.getEmail());
        cv.put("versao",cad.getVersao());

        db.update("cadastro",cv,null,null);
        db.close();
    }

    public void insertFavOnibus(FavOnibus favonibus) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("numero",favonibus.getNumero());
        cv.put("linha",favonibus.getLinha());
        cv.put("tipo",favonibus.getTipo());

        db.insert("onibusfavorito",null,cv);
        db.close();
    }

    public void updateFavOnibus(FavOnibus favOnibus) {

        SQLiteDatabase db = getWritableDatabase();

        String where = "id = ?";
        //String id =  favlocal.getId();
        String[] whereArgs = new String[] {favOnibus.toString()};

        ContentValues cv = new ContentValues();
        cv.put("numero", favOnibus.getNumero());
        cv.put("linha", favOnibus.getLinha());
        cv.put("tipo", favOnibus.getTipo());

        db.update("onibusfavorito",cv,where,whereArgs);
        db.close();
    }

    public void deleteFavOnibus(String id) {

        SQLiteDatabase db = getWritableDatabase();

        String where = "id = ?";
        String[] whereArgs = new String[] {id};

        db.delete("onibusfavorito",where,whereArgs);
        db.close();
    }

    public List<FavOnibus> selectFavOnibus(){
        List<FavOnibus> listFavOnibus = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String sqlSelect = "Select * from onibusfavorito order by numero";

        Cursor c = db.rawQuery(sqlSelect, null);

        if (c.moveToFirst()){
            do {
                FavOnibus favonibus = new FavOnibus();
                favonibus.setId(c.getInt(0));
                favonibus.setNumero(c.getString(1));
                favonibus.setLinha(c.getString(2));
                favonibus.setTipo(c.getString(3));

                listFavOnibus.add(favonibus);
            } while (c.moveToNext());

        }
        c.close();
        db.close();
        return listFavOnibus;
    }

}


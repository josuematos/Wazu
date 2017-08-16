package br.com.editorapendragon.wazu;

/**
 * Created by Josué on 06/05/2016.
 */
public class Cadastro {
    private String id;
    private String nome;
    private String email;
    private String versao;

    public Cadastro() {}

    public Cadastro (String id, String nome,String email, String versao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.versao = versao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }
}

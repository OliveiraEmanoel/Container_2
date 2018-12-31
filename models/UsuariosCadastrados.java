package br.com.emanoel.oliveira.container.models;

public class UsuariosCadastrados {

    String nome;
    String email;
    String data;

    public UsuariosCadastrados() {
    }

    public UsuariosCadastrados(String nome, String email, String data) {
        this.nome = nome;
        this.email = email;
        this.data = data;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

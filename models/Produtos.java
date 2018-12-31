package br.com.emanoel.oliveira.container.models;

public class Produtos {

    String nome;
    double price;
    String description;
    String fotoPath;
    String dataIn;
    String codigo;
    boolean isActive;
    String tipo;
    int favorito;


    public Produtos() {
    }

    public Produtos(String nome, double price, String description, String fotoPath, String dataIn, String codigo,
                    boolean isActive, String tipo, int favorito) {
        this.nome = nome;
        this.price = price;
        this.description = description;
        this.fotoPath = fotoPath;
        this.dataIn = dataIn;
        this.codigo = codigo;
        this.isActive = isActive;
        this.tipo = tipo;
        this.favorito = favorito;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    public String getDataIn() {
        return dataIn;
    }

    public void setDataIn(String dataIn) {
        this.dataIn = dataIn;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getFavorito() {
        return favorito;
    }

    public void setFavorito(int favorito) {
        this.favorito = favorito;
    }
}

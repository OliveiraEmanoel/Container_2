package br.com.emanoel.oliveira.container.models;

public class Estoque {

    String nomeProduto;
    int qdade;
    String tipoMedida;
    String dataInclusao;
    int totalItemEstoque;
    String usuario;
    boolean ativo;

    public Estoque() {
    }

    public Estoque(String nomeProduto, int qdade, String tipoMedida, String dataInclusao, int totalItemEstoque, String usuario, boolean ativo) {
        this.nomeProduto = nomeProduto;
        this.qdade = qdade;
        this.tipoMedida = tipoMedida;
        this.dataInclusao = dataInclusao;
        this.totalItemEstoque = totalItemEstoque;
        this.usuario = usuario;
        this.ativo = ativo;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getQdade() {
        return qdade;
    }

    public void setQdade(int qdade) {
        this.qdade = qdade;
    }

    public String getTipoMedida() {
        return tipoMedida;
    }

    public void setTipoMedida(String tipoMedida) {
        this.tipoMedida = tipoMedida;
    }

    public String getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(String dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public int getTotalItemEstoque() {
        return totalItemEstoque;
    }

    public void setTotalItemEstoque(int totalItemEstoque) {
        this.totalItemEstoque = totalItemEstoque;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}

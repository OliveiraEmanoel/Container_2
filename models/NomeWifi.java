package br.com.emanoel.oliveira.container.models;

public class NomeWifi {

    String nomeWifi;
    String userId;
    String dataEdicao;

    public NomeWifi() {
    }

    public NomeWifi(String nomeWifi, String userId, String dataEdicao) {
        this.nomeWifi = nomeWifi;
        this.userId = userId;
        this.dataEdicao = dataEdicao;
    }

    public String getNomeWifi() {
        return nomeWifi;
    }

    public void setNomeWifi(String nomeWifi) {
        this.nomeWifi = nomeWifi;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDataEdicao() {
        return dataEdicao;
    }

    public void setDataEdicao(String dataEdicao) {
        this.dataEdicao = dataEdicao;
    }
}

package br.com.emanoel.oliveira.container.models;

public class Mesas {
    String nomeBar;
    int numeroMesa;
    Boolean isOcupada;
    String nomeReserva;
    String mesaId;
    String qrCode;


    public Mesas() {
    }

    public Mesas(String nomeBar, int numeroMesa, Boolean isOcupada, String nomeReserva, String mesaId, String qrCode) {
        this.nomeBar = nomeBar;
        this.numeroMesa = numeroMesa;
        this.isOcupada = isOcupada;
        this.nomeReserva = nomeReserva;
        this.mesaId = mesaId;
        this.qrCode = qrCode;
    }

    public String getNomeBar() {
        return nomeBar;
    }

    public void setNomeBar(String nomeBar) {
        this.nomeBar = nomeBar;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public Boolean getOcupada() {
        return isOcupada;
    }

    public void setOcupada(Boolean ocupada) {
        isOcupada = ocupada;
    }

    public String getNomeReserva() {
        return nomeReserva;
    }

    public void setNomeReserva(String nomeReserva) {
        this.nomeReserva = nomeReserva;
    }

    public String getMesaId() {
        return mesaId;
    }

    public void setMesaId(String mesaId) {
        this.mesaId = mesaId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}

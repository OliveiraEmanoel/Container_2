package br.com.emanoel.oliveira.container.models;

public class Reserva {

    String user;
    String nomeEvento;
    String dataEvento;
    int nroMesa;
    int nroPessoas;
    boolean ativo;
    String dataReserva;

    public Reserva() {
    }

    public Reserva(String user, String nomeEvento, String dataEvento, int nroMesa, int nroPessoas, boolean ativo, String dataReserva) {
        this.user = user;
        this.nomeEvento = nomeEvento;
        this.dataEvento = dataEvento;
        this.nroMesa = nroMesa;
        this.nroPessoas = nroPessoas;
        this.ativo = ativo;
        this.dataReserva = dataReserva;
    }

    public String getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(String dataReserva) {
        this.dataReserva = dataReserva;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public String getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(String dataEvento) {
        this.dataEvento = dataEvento;
    }

    public int getNroMesa() {
        return nroMesa;
    }

    public void setNroMesa(int nroMesa) {
        this.nroMesa = nroMesa;
    }

    public int getNroPessoas() {
        return nroPessoas;
    }

    public void setNroPessoas(int nroPessoas) {
        this.nroPessoas = nroPessoas;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}

package br.com.emanoel.oliveira.container.models;

public class Eventos {

    String nomeEvento;
    String descricaoEvento;
    String dataEvento;
    String photoPath;
    boolean eventoAtivo;

    public Eventos() {
    }

    public Eventos(String nomeEvento, String descricaoEvento, String dataEvento, String photoPath, boolean eventoAtivo) {
        this.nomeEvento = nomeEvento;
        this.descricaoEvento = descricaoEvento;
        this.dataEvento = dataEvento;
        this.photoPath = photoPath;
        this.eventoAtivo = eventoAtivo;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public String getDescricaoEvento() {
        return descricaoEvento;
    }

    public void setDescricaoEvento(String descricaoEvento) {
        this.descricaoEvento = descricaoEvento;
    }

    public String getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(String dataEvento) {
        this.dataEvento = dataEvento;
    }

    public boolean isEventoAtivo() {
        return eventoAtivo;
    }

    public void setEventoAtivo(boolean eventoAtivo) {
        this.eventoAtivo = eventoAtivo;
    }
}

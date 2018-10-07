package br.com.emanoel.oliveira.container.models;

public class AdmUsers {
    String nomeAdmUser;
    String emailAdmUser;
    String dataInclusao;
    Boolean isWorker,isActive;
    String userId;


    public AdmUsers() {
    }

    public AdmUsers(String nomeAdmUser, String emailAdmUser, String dataInclusao, Boolean isWorker, String userId, Boolean isActive) {
        this.nomeAdmUser = nomeAdmUser;
        this.emailAdmUser = emailAdmUser;
        this.dataInclusao = dataInclusao;
        this.isWorker = isWorker;
        this.isActive = isActive;
        this.userId = userId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getNomeAdmUser() {
        return nomeAdmUser;
    }

    public void setNomeAdmUser(String nomeAdmUser) {
        this.nomeAdmUser = nomeAdmUser;
    }

    public String getEmailAdmUser() {
        return emailAdmUser;
    }

    public void setEmailAdmUser(String emailAdmUser) {
        this.emailAdmUser = emailAdmUser;
    }

    public String getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(String dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public Boolean getWorker() {
        return isWorker;
    }

    public void setWorker(Boolean worker) {
        isWorker = worker;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

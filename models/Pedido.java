package br.com.emanoel.oliveira.container.models;

import java.io.Serializable;

/**
 * Created by USUARIO on 22/12/2017.
 */

public class Pedido implements Serializable {

    private String nroPedido;
    private String nomeProduto;
    private String descriProduto;
    private String obs;
    private int qdadeProduto;
    private double valorPedido;
    private boolean viagem;
    private boolean entregue;
    private int nroMesa;
    private String nomeCliente;
    private String celCliente;
    private String dataPedido;
    private String horaPedido;
    private String userId;
    private String status;

    public Pedido() {
    }

    public  Pedido(String nroPedido, String nomeProduto, String descriProduto, String obs, int qdadeProduto, double valorPedido, boolean viagem, boolean entregue, int nroMesa, String nomeCliente,
                  String celCliente, String dataPedido, String horaPedido, String userId, String status) {
        this.nroPedido = nroPedido;
        this.nomeProduto = nomeProduto;
        this.descriProduto = descriProduto;
        this.obs = obs;
        this.qdadeProduto = qdadeProduto;
        this.valorPedido = valorPedido;
        this.viagem = viagem;
        this.entregue = entregue;
        this.nroMesa = nroMesa;
        this.nomeCliente = nomeCliente;
        this.celCliente = celCliente;
        this.dataPedido = dataPedido;
        this.horaPedido = horaPedido;
        this.userId = userId;
        this.status = status;
    }

    public String getNroPedido() {
        return nroPedido;
    }

    public void setNroPedido(String nroPedido) {
        this.nroPedido = nroPedido;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDescriProduto() {
        return descriProduto;
    }

    public void setDescriProduto(String descriProduto) {
        this.descriProduto = descriProduto;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public int getQdadeProduto() {
        return qdadeProduto;
    }

    public void setQdadeProduto(int qdadeProduto) {
        this.qdadeProduto = qdadeProduto;
    }

    public double getValorPedido() {
        return valorPedido;
    }

    public void setValorPedido(double valorPedido) {
        this.valorPedido = valorPedido;
    }

    public boolean isViagem() {
        return viagem;
    }

    public void setViagem(boolean viagem) {
        this.viagem = viagem;
    }

    public boolean isEntregue() {
        return entregue;
    }

    public void setEntregue(boolean entregue) {
        this.entregue = entregue;
    }

    public int getNroMesa() {
        return nroMesa;
    }

    public void setNroMesa(int nroMesa) {
        this.nroMesa = nroMesa;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getCelCliente() {
        return celCliente;
    }

    public void setCelCliente(String celCliente) {
        this.celCliente = celCliente;
    }

    public String getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(String dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getHoraPedido() {
        return horaPedido;
    }

    public void setHoraPedido(String horaPedido) {
        this.horaPedido = horaPedido;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package br.com.emanoel.oliveira.container.activities;

import android.app.Application;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by USUARIO on 10/09/2017.
 */

public class GlobalUserID extends Application {

    private String usuarioId;
    private String usuarioName;
    private long cartItem;
    private Boolean cartPressed;
    private Boolean isLogged = false;
    //tempCart
    private String itemId;
    private String nomeProduto;
    private String codigoProduto;
    private float valorProduto;



    private float valorUnitario;
    private String imageSrc;
    private String qdadeProduto;
    private String descriptionProduto;

    public String getDescriptionProduto() {
        return descriptionProduto;
    }

    public void setDescriptionProduto(String descriptionProduto) {
        this.descriptionProduto = descriptionProduto;
    }

    private String tamanhoProduto;
    private String dataGerado;
    private String dataCompra;
    private String status;

    public Boolean getCartPressed() {
        return cartPressed;
    }

    public void setCartPressed(Boolean cartPressed) {
        this.cartPressed = cartPressed;
    }

    public long getCartItem() {
        return cartItem;
    }

    public void setCartItem(long cartItem) {
        this.cartItem = cartItem;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioName() {
        return usuarioName;
    }

    public void setUsuarioName(String usuarioName) {
        this.usuarioName = usuarioName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public float getValorProduto() {
        return valorProduto;
    }

    public void setValorProduto(float valorProduto) {
        this.valorProduto = valorProduto;
    }

    public String getQdadeProduto() {
        return qdadeProduto;
    }

    public void setQdadeProduto(String qdadeProduto) {
        this.qdadeProduto = qdadeProduto;
    }

    public String getTamanhoProduto() {
        return tamanhoProduto;
    }

    public void setTamanhoProduto(String tamanhoProduto) {
        this.tamanhoProduto = tamanhoProduto;
    }

    public String getDataGerado() {
        return dataGerado;
    }

    public void setDataGerado(String dataGerado) {
        this.dataGerado = dataGerado;
    }

    public String getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(String dataCompra) {
        this.dataCompra = dataCompra;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Boolean getLogged() {
        return isLogged;
    }

    public void setLogged(Boolean logged) {
        isLogged = logged;
    }
    public float getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(float valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public void GlobalUserId_TempCart(String itemId, String nomeProduto, String descriptionProduto, String codigoProduto, String imageSrc, float valorUnitario, float valorProduto, String qdadeProduto, String tamanhoProduto, String dataGerado, String dataCompra, String status) {
        this.itemId = itemId;
        this.nomeProduto = nomeProduto;
        this.codigoProduto = codigoProduto;
        this.descriptionProduto = descriptionProduto;
        this.valorProduto = valorProduto;
        this.qdadeProduto = qdadeProduto;
        this.tamanhoProduto = tamanhoProduto;
        this.dataGerado = dataGerado;
        this.dataCompra = dataCompra;
        this.status = status;
        this.imageSrc = imageSrc;
        this.valorUnitario = valorUnitario;

    }

    public interface MyTesteListener {

        public void onItemClicked(View v, int position);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }







}
package br.com.emanoel.oliveira.container.activities;//package br.com.emanoel.oliveira.sextodecoracoes.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by USUARIO on 25/09/2017.
 */

public class BaseActivity extends AppCompatActivity {

    public Calendar myCalendar;
    public String myFormat = "dd-MM-yyyy";
    public SimpleDateFormat sdf;
    public GlobalUserID globalUserID;
    public FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    public String nome_banco_dados = "container_bar";

    public DatabaseReference myRef = mFirebaseDatabase.getReference(nome_banco_dados);
    android.support.v7.app.ActionBar actionBar;
    public DecimalFormat value = new DecimalFormat("0.00");
    public DecimalFormat f = new DecimalFormat("R$ 0.00");
    public static ArrayList<String> globalArray = new ArrayList<>();
    //public static ArrayList<Produto_Tecido> produtoTedidoArrayList = new ArrayList<>();
   // public static List<Produto_Tecido> cart;
    public static ArrayList<String> produtoKey = new ArrayList<>();
    public String userID;

    public static int nroItensCart;//vou usar como indice do array
    public static double totalCart;
    public static int qdadePecas;
    public static boolean rvHasClicked = false; //used to monitor if recyclerView has received a click
    public static boolean userIsAdmin;// = false;//used to check if user can add new products on database
    public static boolean isNovidade = true; //used to show only news products as default from database



//    public static List<Produto_Tecido> getCart() {
//        if (cart == null) {
//            cart = new Vector<Produto_Tecido>();
//        }
//
//        return cart;
//    }

    public Boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public Boolean isUserAdmin(String user) {

        userIsAdmin = false;

        //todo buscar essa informação no banco de dados

        Log.d("BaseActivity","user = "+user);

        if (user.equals("emanoel_oliveira@hotmail.com") || user.equals("emanoel@alfatektechnologia.com.br")) {
            userIsAdmin = true;
            return true;
        } else {

            return userIsAdmin;
        }

    }

    @VisibleForTesting

    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {

        if (mProgressDialog == null) {

            mProgressDialog = new ProgressDialog(this);

            mProgressDialog.setMessage("Carregando...");

            mProgressDialog.setIndeterminate(true);

        }

        mProgressDialog.show();

    }

    public void hideProgressDialog() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {

            mProgressDialog.dismiss();

        }
    }

    //retrieving the first two strings of a string
    public String firstTwo(String str) {
        return str.length() < 2 ? str : str.substring(0, 2);
    }

    @Override

    public void onStop() {

        super.onStop();

        hideProgressDialog();

    }

    public boolean isConnected() {
        try {
            String command = "ping -c 1 firebase.google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);


        } catch (Exception e) {
            Log.e("BaseActivity", "Error checking internet connection", e);
            return false;

        }
    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;

        try {
            String[] proj = {MediaStore.Images.Media.DATA};

            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getRealTitleFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;

        try {
            String[] proj = {MediaStore.Images.Media.DISPLAY_NAME};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);// getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



    public void myToastCurto(String mensagem) {

        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_SHORT).show();

    }

    public void myToastlongo(String mensagem) {

        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();

    }


}
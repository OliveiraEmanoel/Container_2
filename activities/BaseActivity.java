package br.com.emanoel.oliveira.container.activities;//package br.com.emanoel.oliveira.sextodecoracoes.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.MediaStore;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import br.com.emanoel.oliveira.container.models.AdmUsers;
import br.com.emanoel.oliveira.container.models.NomeWifi;
import br.com.emanoel.oliveira.container.models.Pedido;

/**
 * Created by USUARIO on 25/09/2017.
 */

public class BaseActivity extends AppCompatActivity {

    public Calendar myCalendar;
    public String myFormat = "dd-MM-yyyy";
    String myTime = "hh:mm:ss";
    public SimpleDateFormat sdf,sdfData;
    public GlobalUserID globalUserID;
    public FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    public StorageReference myStorageRef;
    public String nome_banco_dados = "container_bar";
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    public static String nroPedido, nomeProduto, nomeCliente, celCliente, obs, usuarioID;
    public DatabaseReference myRef = mFirebaseDatabase.getReference(nome_banco_dados);

    public static String currentConnectedSSID;
    android.support.v7.app.ActionBar actionBar;
    public DecimalFormat value = new DecimalFormat("0.00");
    public DecimalFormat f = new DecimalFormat("R$ 0.00");
    public static ArrayList<String> globalArray = new ArrayList<>();
    public static List<Pedido> pedido;
    public static Pedido meuPedido = new Pedido();

    public static ArrayList<String> produtoKey = new ArrayList<>();
    public static String userID;
    public static String userNome;
    public static boolean mesaHasUser;
    public static String userLogado;
    List<AdmUsers> admUsersList;
    public static double totalPedido = 0;
    public static int nroMesa;
    public static double totalCart;
    public static int qdadePecas;
    public static boolean rvHasClicked = false; //used to monitor if recyclerView has received a click
    public static boolean userIsAdmin ;// = false;//used to check if user can add new products on database
    public static boolean isNovidade = true; //used to show only news products as default from database
    public static String nomeWifiAtual;//this name is setted at starting login activity
    NomeWifi nomeWifi;
    public long itemCount;

    public static List<Pedido> getPedido() {
        if (pedido == null) {
            pedido = new Vector<Pedido>();
        }

        return pedido;
    }



    //check Wifi info
    public boolean isConnectedTo(String ssid, Context context) {
        boolean retVal = false;
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        if (wifiInfo != null) {
            //https://stackoverflow.com/questions/41512450/string-replace-is-returning-extra-quotation-marks
            currentConnectedSSID = wifiInfo.getSSID().replace("currentConnectedSSID:", "").replaceAll("\"", "");
            Log.e("CHECANDO WIFI", "isConnected : " + currentConnectedSSID + ssid);

            if (currentConnectedSSID.equals(ssid)) {
                retVal = true;
            }
        }
        return retVal;
    }

    public Boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public Boolean isUserAdmin(String user) {

        Log.e("BASE_ACTIVITY", "isUserAdmin: " + userIsAdmin );
        return userIsAdmin;
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
        showProgressDialog();
        try {
            String command = "ping -c 1 firebase.google.com";
            hideProgressDialog();
            return (Runtime.getRuntime().exec(command).waitFor() == 0);


        } catch (Exception e) {
            Log.e("BaseActivity", "Error checking internet connection", e);
            hideProgressDialog();
            return false;

        }
    }

    public void addFragment2Frame(int frame, Fragment fragment){

        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(frame, fragment, backStateName);
            ft.addToBackStack(backStateName);

            ft.commit();
        }

//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack(null);
//        transaction.add(frame,fragment);
//
//        transaction.commit();
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
    //get name of WIFI network
    public String getNomeAtual() {

        try {

           // myRef.child("wifi").orderByChild("nomeWifi");
            //getting itemCount

            myRef.child("wifi").orderByChild("nomeWifi").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    itemCount = dataSnapshot.getChildrenCount();
                    Log.e("GETNOMEWIFI", "onDataChange: " + itemCount);

                    for (DataSnapshot ref : dataSnapshot.getChildren()) {

                        if (!dataSnapshot.exists()) {
                            nomeWifiAtual = " ";
                            return;
                        } else {
                            nomeWifi = ref.getValue(NomeWifi.class);
                           nomeWifiAtual = nomeWifi.getNomeWifi();
                            Log.e("GETNOMEWIFI", "Nome Wifi Atual = : " + nomeWifiAtual);
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });


        } catch (Throwable e) {

            e.printStackTrace();
        }

        return nomeWifiAtual;

    }


    public void settingBar() {

        actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setHomeButtonEnabled(true);
    }

    public void myToastCurto(String mensagem) {

        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_SHORT).show();

    }

    public void myToastlongo(String mensagem) {

        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();

    }

    public void sendEmail2me(String assunto, String local, String erro, String data, String userId){

        String corpo = "Aconteceu um erro em " + local + " // " + erro + " // " + data + " // " + userId;

        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setType("message/rfc822");
        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"emanoel@alfatektecnologia.com.br","emanoel_oliveira@hotmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, assunto);
        i.putExtra(Intent.EXTRA_TEXT   , corpo);
        try {
            //startActivity(Intent.createChooser(i, "Send mail..."));
            getApplicationContext().startActivity(i);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "Não existe software de email instalado...", Toast.LENGTH_SHORT).show();
        }

    }

//    public boolean findItemTotal(String itemEstoque) {
//
//        //todo acessar bd, ordenar por itemNome, somar total , atualizar esse item ao salvar
//        //todo mostrar tvTotal
//
//
//        // Read from the database
//        myRef.child("estoque").orderByChild(itemEstoque).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                if(dataSnapshot.getChildrenCount()!=0){
//                    Estoque estoque = dataSnapshot.getValue(Estoque.class);
//                    itemExist = true;
//                   String itemKey = dataSnapshot.getKey();
//                   int  total = estoque.getTotalItemEstoque();
//
//
//                } else {
//                    itemExist = false;//todo atualizar não/key não existe
//                }
////                String value = dataSnapshot.getValue(String.class);
////                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
//
//        return itemExist;
//
//    }


}
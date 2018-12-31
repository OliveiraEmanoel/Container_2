package br.com.emanoel.oliveira.container.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.adapters.CustomAdapter;
import br.com.emanoel.oliveira.container.models.Pedido;

public class PedidoActivity extends BaseActivity {

    private List<Pedido> pedidoList = getPedido();// = (ArrayList<Pedido>) array.getSerializable("ArrayList");;

    ListView list;
    CustomAdapter myAdapter;
    Pedido myPedido;
    int itensList;
    int qdadeLanche;
    int grupoColor;
    double valorPedido;
   // Button btAddLanche;
    Button btFinalizarPedido;
    EditText etObs, etNomeCliente, etCelCliente, etValorTotal;
    TextView nrPedido;
    CheckBox sms,pago,viagem;
    Bundle array = new Bundle();
    FloatingActionButton fab;
    String indiceKey;
    String ssid = "Emanoel";//todo ????
    Calendar time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        settingBar();

        etObs = findViewById(R.id.et_pedido_obs);
        etCelCliente = findViewById(R.id.et_pedido_cel);
        etNomeCliente = findViewById(R.id.et_pedido_cliente);
        etValorTotal = findViewById(R.id.et_pedido_Vtotal);
        nrPedido = findViewById(R.id.tvNroPedido);
        sms = findViewById(R.id.cbSMS);
        pago = findViewById(R.id.cbPago);
        viagem = findViewById(R.id.cbViagem);

        //setting buttons
       // btAddLanche = findViewById(R.id.bt_AddLanche);
        btFinalizarPedido = findViewById(R.id.bt_pedido_finalizar);

        //checa se usuario está conectado no mesmo wifi do estabelecimento
        //todo get the SSID from a config or database
        if (isConnectedTo(nomeWifiAtual, PedidoActivity.this)) {

            Log.e("CHECANDO WIFI", "onCreate: "+ currentConnectedSSID );

        }else {

            Toast.makeText(this,"Pedidos? Solicite a senha do WIFI/local...",Toast.LENGTH_LONG).show();
            //btAddLanche.setVisibility(View.GONE);
            btFinalizarPedido.setVisibility(View.GONE);
        }
        time = Calendar.getInstance();
        nroPedido = "";

        nroPedido = getNroPedido();
        sdf = new SimpleDateFormat(myTime);

        //double totValue = (myAdapter.getTotalPedido());
        String tot = f.format(totalPedido);
        etValorTotal.setText(tot);


        //setting floating button
        fab = findViewById(R.id.fab_plus_lanche);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveDataPedido();

                onBackPressed();
            }
        });


        //setting listView
        list = findViewById(R.id.lv_pedido);

        //setting global variable to save a temp cart
        globalUserID = (GlobalUserID) getApplication();
        myCalendar = Calendar.getInstance();

        //loadData();

        myAdapter = new CustomAdapter(pedidoList, getApplicationContext(), grupoColor);
        myPedido = new Pedido();
        list.setAdapter(myAdapter);




        //adding listener to buttons

//        btAddLanche.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("+lanche", "clicked");
//                //save name, cel and remarks on static variables
//                saveDataPedido();
//
//                onBackPressed();
//
//            }
//        });

        btFinalizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itensList = list.getChildCount();//get size of list

                if(itensList>0 && validate()){
                    saveDataPedido();
                    myRef.child("pedidos").push().setValue(pedidoList);
                    pedidoList.clear();
                    onBackPressed();
                }else {
                    onBackPressed();
                }


            }
        });


    }

    public String getNroPedido() {

        try {

            myRef.child("pedidos");
            //getting itemCount

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    itemCount = dataSnapshot.getChildrenCount();
                    Log.e("GETNROPEDIDO", "onDataChange: " + itemCount);
                    indiceKey = String.valueOf(itemCount + 1);
                    nroPedido = String.valueOf(nroPedido + indiceKey);
                    nrPedido.setText(nroPedido);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });


        } catch (Throwable e) {

            e.printStackTrace();
        }

        return nroPedido;

    }

    public void saveDataPedido() {



            obs = etObs.getText().toString().toUpperCase();
            nomeCliente = etNomeCliente.getText().toString().toUpperCase();
            celCliente = etCelCliente.getText().toString();
            Date hora = time.getTime();
            String horaPedido = sdf.format(hora);
            meuPedido.setObs(obs);
            meuPedido.setNomeCliente(nomeCliente);
            meuPedido.setCelCliente(celCliente);

            meuPedido.setViagem(viagem.isChecked());
            meuPedido.setStatus("ATIVO");
            meuPedido.setHoraPedido(horaPedido);
            //myPedido.setDataPedido(myCalendar.getTime().toString());
            meuPedido.setNroPedido(nroPedido);
            for (int x=0;x<itensList;x++){
                pedidoList.set(x, meuPedido);



        }


    }

    private boolean validate(){

        boolean validado = false;

        if(etNomeCliente.getText().equals("")){

            Toast.makeText(getApplicationContext(),"Nome do cliente?? ",Toast.LENGTH_SHORT).show();
            return validado;

        }else{

            validado = true;
        }


        return validado;
    }

    @Override
    public void onBackPressed() {
        // saveData();
        Log.e("OnBack", "saved");
        super.onBackPressed();
    }


}




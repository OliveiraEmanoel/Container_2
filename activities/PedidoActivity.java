package br.com.emanoel.oliveira.container.activities;

import android.content.Context;
import android.content.Intent;
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

    Button btFinalizarPedido, btLerQrcode;
    EditText etObs, etNomeCliente, etMesa;
    public EditText etValorTotal;
    TextView nrPedido;
    CheckBox viagem;
    Bundle array = new Bundle();
    FloatingActionButton fab;
    String indiceKey;


    Calendar time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        settingBar();

        etObs = findViewById(R.id.et_pedido_obs);
        etMesa = findViewById(R.id.et_pedido_mesa);
        etNomeCliente = findViewById(R.id.et_pedido_cliente);
        etValorTotal = findViewById(R.id.et_pedido_Vtotal);
        nrPedido = findViewById(R.id.tvNroPedido);

        viagem = findViewById(R.id.cbViagem);

        //setting buttons
        btLerQrcode = findViewById(R.id.btLerQrcode);
        btFinalizarPedido = findViewById(R.id.bt_pedido_finalizar);

        //checa se usuario está conectado no mesmo wifi do estabelecimento
        //todo get the SSID from a config or database
        if (isConnectedTo(nomeWifiAtual, PedidoActivity.this)) {

            Log.e("CHECANDO WIFI", "onCreate: " + currentConnectedSSID);

        } else {

            Toast.makeText(this, "Pedidos? Solicite a senha do WIFI/local...", Toast.LENGTH_LONG).show();
            //btAddLanche.setVisibility(View.GONE);
            btFinalizarPedido.setVisibility(View.GONE);
        }
        time = Calendar.getInstance();
        sdf = new SimpleDateFormat(myTime);
        nroPedido = "";

        nroPedido = getNroPedido();
        sdf = new SimpleDateFormat(myTime);
        sdfData = new SimpleDateFormat(myFormat);
        time.set(Calendar.HOUR_OF_DAY, 24);
        //double totValue = (myAdapter.getTotalPedido());
        String tot = f.format(totalPedido);
        etValorTotal.setText(tot);

        setNroMesa();

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

        btLerQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ScanQrCodeActivity.class);
                startActivityForResult(i, 13);
                setNroMesa();
            }
        });

        btFinalizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itensList = list.getChildCount();//get size of list

                if ((itensList > 0) && (validate())) {
                    saveDataPedido();

                    pedidoList.clear();
                    onBackPressed();
                } else {
                    return;
                }


            }
        });


    }

    public void setNroMesa() {

        if (mesaHasUser) {//nroMesa > 0
            btLerQrcode.setVisibility(View.GONE);
            etMesa.setText(String.valueOf(nroMesa));
            etNomeCliente.setText(userNome.toUpperCase());
        }
    }

    public String getNroPedido() {

        try {

            //getting itemCount
            myRef.child("pedidos").addValueEventListener(new ValueEventListener() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 13) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                setNroMesa();
            }
        }
    }

    public void saveDataPedido() {


        myAdapter.notifyDataSetChanged();
        obs = etObs.getText().toString().toUpperCase();
        nomeCliente = etNomeCliente.getText().toString().toUpperCase();
        celCliente = etMesa.getText().toString();
        Date hora = time.getTime();
        String horaPedido = sdf.format(hora);
        for (int x = 0; x < itensList; x++) {

            myPedido.setQdadeProduto(pedidoList.get(x).getQdadeProduto());
            myPedido.setDescriProduto(pedidoList.get(x).getDescriProduto());
            myPedido.setNomeProduto(pedidoList.get(x).getNomeProduto());
            myPedido.setValorPedido(pedidoList.get(x).getValorPedido());
            myPedido.setObs(obs);
            myPedido.setEntregue(false);
            myPedido.setNomeCliente(nomeCliente);
            //meuPedido.setCelCliente(celCliente);
            myPedido.setNroMesa(nroMesa);
            myPedido.setViagem(viagem.isChecked());
            myPedido.setStatus("aberto");
            myPedido.setHoraPedido(horaPedido);
            Date dataHj = myCalendar.getTime();
            String dataPedido = sdfData.format(dataHj);
            myPedido.setDataPedido(dataPedido);
            myPedido.setNroPedido(nroPedido);
            myPedido.setUserId(userID);

            pedidoList.set(x,myPedido);
            myRef.child("pedidos").child(nomeCliente).push().setValue(myPedido);
        }

    }

    public void getTotalPedido(Context context) {

        String tot = f.format(totalPedido);
        etValorTotal.setText(tot);

    }

    private boolean validate() {

        boolean validado = false;

        String nome = etNomeCliente.getText().toString();

        if (nome.equals("")) {
            etNomeCliente.setError(getString(R.string.obrigatorio));
            //Toast.makeText(getApplicationContext(), "Nome do cliente?? ", Toast.LENGTH_SHORT).show();
            return validado;

        } else {
            etNomeCliente.setError(null);
            validado = true;
        }


        return validado;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}




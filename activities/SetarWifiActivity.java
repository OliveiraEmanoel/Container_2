package br.com.emanoel.oliveira.container.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.emanoel.oliveira.container.R;

import br.com.emanoel.oliveira.container.models.NomeWifi;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetarWifiActivity extends BaseActivity {

    @BindView(R.id.etNomeWifiAtual)
    TextInputEditText etNomeWifiAtual;
    @BindView(R.id.etNomeWifiNovo)
    TextInputEditText etNomeWifiNovo;
    @BindView(R.id.btSalvarNomeWifi)
    Button btSalvarNomeWifi;

    private String userId,dataEdit, nomeWifiNovo;
    private NomeWifi meuModelo;
    private NomeWifi nomeWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setar_wifi);
        ButterKnife.bind(this);

        settingBar();
        getNomeAtual();
        globalUserID = (GlobalUserID) getApplication();

        userId = globalUserID.getUsuarioId();
        nomeWifi = new NomeWifi();
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        etNomeWifiAtual.setText(nomeWifiAtual);
        etNomeWifiAtual.setKeyListener(null);//impede digitação

    }

    @OnClick(R.id.btSalvarNomeWifi)
    public void onViewClicked() {
        myCalendar = Calendar.getInstance();
        Date now = myCalendar.getTime();
        dataEdit = sdf.format(now);
        validate();
        salvarItem(nomeWifiNovo, userId, dataEdit);
        onBackPressed();

    }

    //salva novo nome do Wifi
    public void salvarItem(String nomeWifi, String userId, String dataEdit) {


        try {

            meuModelo = new NomeWifi(nomeWifi, userId, dataEdit);

            myRef.child("wifi").push().setValue(meuModelo);
            //clearForm();

        } catch (Throwable e) {

            e.printStackTrace();
        }
    }



    private boolean validate() {

        boolean validado = false;

        if (etNomeWifiNovo.getText().equals("")) {

            Toast.makeText(getApplicationContext(), "Nome do WIFI local?? ", Toast.LENGTH_SHORT).show();
            return validado;

        } else {
            nomeWifiNovo = etNomeWifiNovo.getText().toString();
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

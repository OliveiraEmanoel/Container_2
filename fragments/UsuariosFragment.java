package br.com.emanoel.oliveira.container.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.activities.BaseActivity;
import br.com.emanoel.oliveira.container.models.AdmUsers;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsuariosFragment extends Fragment {


    public UsuariosFragment() {
        // Required empty public constructor
    }


    BaseActivity baseActivity;
    private EditText nomeUserAdm, emailUserAdm;
    private Boolean isWorker, tipo_user, isActive;
    private Button btsalvarUserAdm,btcancelar;
    public String userId, dataInclusao;
    RadioGroup radioGroup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_usuarios, container, false);
        baseActivity = new BaseActivity();

        nomeUserAdm = view.findViewById(R.id.etAdmUserName);
        emailUserAdm = view.findViewById(R.id.etEmailUserAdm);
        btsalvarUserAdm = view.findViewById(R.id.btSalvarUserAdm);
        btcancelar = view.findViewById(R.id.btAdmUserCancel);
        radioGroup = view.findViewById(R.id.rgAdmUserWorker);

        userId = baseActivity.userID;
        isActive = true;
        tipo_user = false;
        baseActivity.myCalendar = Calendar.getInstance();
        dataInclusao = baseActivity.myCalendar.getTime().toString();
        //baseActivity.nome_banco_dados = "admUsers";

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rbUserAdm) {

                    isWorker = false;
                } else {
                    isWorker = true;
                }
                tipo_user = true;
            }
        });

        btsalvarUserAdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate();

                if (!validate()) {
                    return;
                }

                Log.d("SALVANDO_DADOS", "onClick: ");

                addRegistro(nomeUserAdm.getText().toString().toUpperCase(),
                        emailUserAdm.getText().toString(),
                        dataInclusao,
                        userId,
                        isWorker, isActive);


            }
        });

        btcancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;

    }

    private void addRegistro(String nome, String email, String dataIn,
                             String user, boolean isWorker, boolean isActive) {

        AdmUsers admUsers = new AdmUsers(nome, email, dataIn, isWorker, user, isActive);
        try {
            baseActivity.myRef.child("usuarios_admin").push().setValue(admUsers);
            emailUserAdm.setText("");
            nomeUserAdm.setText("");
            radioGroup.clearCheck();
            tipo_user = false;
        } catch (Exception e) {

            Toast.makeText(getActivity(), "Erro gravando arquivo " + e, Toast.LENGTH_SHORT).show();
        }


    }

    private boolean validate() {
        boolean valid = true;

        String nome = nomeUserAdm.getText().toString().toUpperCase();

        if (TextUtils.isEmpty(nome) || ((!nome.matches("[a-zA-Z ]*")))) {
            nomeUserAdm.setError(getString(R.string.nome_invalido));
            valid = false;
        } else {
            nomeUserAdm.setError(null);
        }

        String email = emailUserAdm.getText().toString().toUpperCase();
        if (TextUtils.isEmpty(nome)|| (!baseActivity.isEmailValid(email))) {
            emailUserAdm.setError(getString(R.string.email_invalido));
            valid = false;
        } else {
            emailUserAdm.setError(null);
        }

        if (!tipo_user) {
            Toast.makeText(getActivity(), "Selecione o tipo de usuário", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            valid = true;
        }
        return valid;

    }




}

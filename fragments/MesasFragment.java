package br.com.emanoel.oliveira.container.fragments;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import br.com.emanoel.oliveira.container.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MesasFragment extends Fragment {
    @BindView(R.id.ivQrCode)
    ImageView ivQrCode;
    @BindView(R.id.etNroMesa)
    TextInputEditText etNroMesa;
    @BindView(R.id.etQdadeLugaresMesa)
    TextInputEditText etQdadeLugaresMesa;
    @BindView(R.id.btGerarQrcode)
    Button btGerarQrcode;
    @BindView(R.id.btSalvarQrCode)
    Button btSalvarQrCode;
    Unbinder unbinder;

    /* criar o qrcode para cada mesa
     * com numero da mesa
     * nome do bar
     * tamanho da mesa?
     *
     * salvar qrcode*/


    public MesasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mesas, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ivQrCode, R.id.btGerarQrcode, R.id.btSalvarQrCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivQrCode:
                break;
            case R.id.btGerarQrcode:
                if(!validateForm()){
                    return;
                }
                //todo gerar qrcode


                break;
            case R.id.btSalvarQrCode:
                //todo checar se qrcode exists...

                break;
        }
    }

    //validate inputs
    private boolean validateForm() {

        boolean valid = true;


        int nroMesa = Integer.parseInt(etNroMesa.getText().toString());

        if (TextUtils.isEmpty(String.valueOf(nroMesa))) {

            etNroMesa.setError(getString(R.string.obrigatorio));

            valid = false;

        } else {

            etNroMesa.setError(null);

        }
        int nroLugares = Integer.parseInt(etQdadeLugaresMesa.getText().toString());

        if (TextUtils.isEmpty(String.valueOf(nroLugares))) {

            etQdadeLugaresMesa.setError(getString(R.string.obrigatorio));

            valid = false;

        } else {

            etQdadeLugaresMesa.setError(null);

        }


        return valid;

    }
}

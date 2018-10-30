package br.com.emanoel.oliveira.container.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.activities.TiposCadastrosActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastroFragment extends Fragment {


    private RadioGroup rgCadastroAdmin;
    String tipoCadastro;

    public CadastroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cadastro, container, false);


        rgCadastroAdmin = view.findViewById(R.id.rgTipoCadastro);


        rgCadastroAdmin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rbCadastroComprasDespesas) {
                    tipoCadastro = "compras";
                    iniciaAtividade(tipoCadastro);
                } else if (checkedId == R.id.rbCadastroGrupoMenu) {
                    tipoCadastro = "grupos";
                    iniciaAtividade(tipoCadastro);
                } else if (checkedId == R.id.rbCadastroMesas) {
                    tipoCadastro = "mesas";
                    iniciaAtividade(tipoCadastro);
                } else if (checkedId == R.id.rbCadastroPagamentosRetiradas) {
                    tipoCadastro = "pagamentos";
                    iniciaAtividade(tipoCadastro);
                } else if (checkedId == R.id.rbCadastroProdutosMenu) {
                    tipoCadastro = "produtos_menu";
                    iniciaAtividade(tipoCadastro);
                } else if (checkedId == R.id.rbCadastroUsuariosAdm) {
                    tipoCadastro = "usuarios";
                    iniciaAtividade(tipoCadastro);
                } else if (checkedId == R.id.rbCadastroEstoque) {
                    tipoCadastro = "estoque";
                    iniciaAtividade(tipoCadastro);
                }

            }
        });

        return view;

    }

    public void iniciaAtividade(String tipo) {

        Intent intent = new Intent(getActivity(), TiposCadastrosActivity.class);
        intent.putExtra("Tipo", tipo);
        startActivity(intent);
        rgCadastroAdmin.clearCheck();
    }


}

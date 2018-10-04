package br.com.emanoel.oliveira.container.fragments;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.activities.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComprasFragment extends Fragment {


    @BindView(R.id.ivFotoNF)
    ImageView ivFotoNF;
    @BindView(R.id.etDataCompra)
    EditText etDataCompra;
    @BindView(R.id.etNomeComprador)
    EditText etNomeComprador;
    @BindView(R.id.etComprasValorTotalNF)
    TextInputEditText etComprasValorTotalNF;
    @BindView(R.id.etComprasNomeProduto)
    TextInputEditText etComprasNomeProduto;
    @BindView(R.id.spComprasQdadeProduto)
    Spinner spComprasQdadeProduto;
    @BindView(R.id.etComprasValorProduto)
    TextInputEditText etComprasValorProduto;
    @BindView(R.id.spComprasUnidadeProduto)
    Spinner spComprasUnidadeProduto;
    @BindView(R.id.btComprasAddProduto)
    Button btComprasAddProduto;
    @BindView(R.id.lvCadastroCompras)
    ListView lvCadastroCompras;
    @BindView(R.id.btComprasFinalizar)
    Button btComprasFinalizar;
    Unbinder unbinder;

    public ComprasFragment() {
        // Required empty public constructor
    }

    BaseActivity baseActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compras, container, false);

        baseActivity = new BaseActivity();


        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ivFotoNF, R.id.btComprasAddProduto, R.id.btComprasFinalizar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivFotoNF:
                //tirar foto do comprovante
                break;
            case R.id.btComprasAddProduto:
                //produto ja cadastrado? addprodutos e limpar views
                break;
            case R.id.btComprasFinalizar:
                //sair
                break;
        }
    }
}

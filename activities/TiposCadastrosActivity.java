package br.com.emanoel.oliveira.container.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.fragments.ComprasFragment;
import br.com.emanoel.oliveira.container.fragments.EstoqueFragment;
import br.com.emanoel.oliveira.container.fragments.GruposFragment;
import br.com.emanoel.oliveira.container.fragments.MenuFragment;
import br.com.emanoel.oliveira.container.fragments.MesasFragment;
import br.com.emanoel.oliveira.container.fragments.PagamentosFragment;
import br.com.emanoel.oliveira.container.fragments.ProdutosMenuFragment;
import br.com.emanoel.oliveira.container.fragments.UsuariosFragment;

public class TiposCadastrosActivity extends BaseActivity {

    private ComprasFragment comprasFragment;
    private GruposFragment gruposFragment;
    private MenuFragment menuFragment;
    private MesasFragment mesasFragment;
    private PagamentosFragment pagamentosFragment;
    private UsuariosFragment usuariosFragment;
    private ProdutosMenuFragment produtosMenuFragment;
    private EstoqueFragment estoqueFragment;
    private int frameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipos_cadastros);

        Bundle bundle = getIntent().getExtras();
        String tipoCadastro = bundle.getString("Tipo");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Cadastrar " + tipoCadastro);

        comprasFragment = new ComprasFragment();
        gruposFragment = new GruposFragment();
        menuFragment = new MenuFragment();
        mesasFragment = new MesasFragment();
        pagamentosFragment = new PagamentosFragment();
        usuariosFragment = new UsuariosFragment();
        produtosMenuFragment = new ProdutosMenuFragment();
        estoqueFragment = new EstoqueFragment();

        frameId = R.id.frConteudoTiposCadastros;

        if (tipoCadastro.equals("compras")){
            callFragment(frameId,comprasFragment);
        } else if (tipoCadastro.equals("grupos")){
            callFragment(frameId,gruposFragment);
        }else if (tipoCadastro.equals("produtos_menu")){
            callFragment(frameId,produtosMenuFragment);
        }else if (tipoCadastro.equals("pagamentos")){
            callFragment(frameId,pagamentosFragment);
        }else if (tipoCadastro.equals("usuarios")){
            callFragment(frameId,usuariosFragment);
        }else if (tipoCadastro.equals("mesas")){
            callFragment(frameId,mesasFragment);
        }else if (tipoCadastro.equals("estoque")) {
            callFragment(frameId, estoqueFragment);
        }

    }

    public void callFragment(int i, Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack(null);
        transaction.replace(i,fragment);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        Log.e("ON_BACK_PRESSED","COUNT=" + count);

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {

            getSupportFragmentManager().popBackStack();

        }
    }
}

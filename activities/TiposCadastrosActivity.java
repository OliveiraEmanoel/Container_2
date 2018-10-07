package br.com.emanoel.oliveira.container.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.fragments.ComprasFragment;
import br.com.emanoel.oliveira.container.fragments.GruposFragment;
import br.com.emanoel.oliveira.container.fragments.MenuFragment;
import br.com.emanoel.oliveira.container.fragments.MesasFragment;
import br.com.emanoel.oliveira.container.fragments.PagamentosFragment;
import br.com.emanoel.oliveira.container.fragments.UsuariosFragment;

public class TiposCadastrosActivity extends AppCompatActivity {

    private ComprasFragment comprasFragment;
    private GruposFragment gruposFragment;
    private MenuFragment menuFragment;
    private MesasFragment mesasFragment;
    private PagamentosFragment pagamentosFragment;
    private UsuariosFragment usuariosFragment;
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

        frameId = R.id.frConteudoTiposCadastros;

        if (tipoCadastro.equals("compras")){
            callFragment(frameId,comprasFragment);
        } else if (tipoCadastro.equals("grupos")){
            callFragment(frameId,gruposFragment);
        }else if (tipoCadastro.equals("menu")){
            callFragment(frameId,menuFragment);
        }else if (tipoCadastro.equals("pagamentos")){
            callFragment(frameId,pagamentosFragment);
        }else if (tipoCadastro.equals("usuarios")){
            callFragment(frameId,usuariosFragment);
        }else if (tipoCadastro.equals("mesas")){
            callFragment(frameId,mesasFragment);
        }


    }

    public void callFragment(int i, Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(i,fragment);
        transaction.commit();

    }
}

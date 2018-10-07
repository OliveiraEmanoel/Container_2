package br.com.emanoel.oliveira.container.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.fragments.CadastroFragment;
import br.com.emanoel.oliveira.container.fragments.ListagemFragment;

public class AdminActivity extends BaseActivity {

    private Button btCadastro, btListagem;
    private TextView tvCadastro, tvListagem;
    CadastroFragment cadastroFragment;
    ListagemFragment listagemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //retirar a elevation da actionbar
       getSupportActionBar().setElevation(0);

       tvCadastro = findViewById(R.id.tvCadastro);
       tvListagem = findViewById(R.id.tvListagem);
       tvListagem.setVisibility(View.GONE);

       btCadastro = findViewById(R.id.btCadastro);
       btListagem = findViewById(R.id.btListagem);


       //instanciando o fragmento padrão

       cadastroFragment = new CadastroFragment();
       listagemFragment = new ListagemFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frAdmin,cadastroFragment);

        transaction.commit();

        btListagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvListagem.setVisibility(View.VISIBLE);
                tvCadastro.setVisibility(View.GONE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frAdmin,listagemFragment);
                transaction.commit();

            }
        });

        btCadastro.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                tvListagem.setVisibility(View.GONE);
                tvCadastro.setVisibility(View.VISIBLE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frAdmin,cadastroFragment);
                transaction.commit();
            }
        });
    }


}

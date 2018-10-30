package br.com.emanoel.oliveira.container.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Calendar;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.activities.BaseActivity;
import br.com.emanoel.oliveira.container.models.Estoque;

/**
 * A simple {@link Fragment} subclass.
 */
public class EstoqueFragment extends Fragment {


    public EstoqueFragment() {
        // Required empty public constructor
    }

    Spinner spProdutos;
    Spinner spTipo;
    BaseActivity baseActivity;
    Button btSalvar, btExcluir, btAtualizar, btPlus, btMinus;
    EditText etQdade, etUser, etData, etTotal;
    boolean ativo = true;
    String itemEstoque, qdadeEstoque, userEstoque, totalEstoque, tipoMedidaItemEstoque,codigoRef;
    String dataEntrada;
    long itemCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_estoque, container, false);
        spProdutos = v.findViewById(R.id.spNomeProdutoEstoque);
        spTipo = v.findViewById(R.id.spTipoMedidaEstoque);
        btAtualizar = v.findViewById(R.id.btAtualizar);
        btExcluir = v.findViewById(R.id.btRemover);
        btMinus = v.findViewById(R.id.btMenosEstoque);
        btPlus = v.findViewById(R.id.btMaisEstoque);
        btSalvar = v.findViewById(R.id.btSalvarEstoque);
        etData = v.findViewById(R.id.etDataEstoque);
        etUser = v.findViewById(R.id.etUsernameEstoque);
        etQdade = v.findViewById(R.id.etQdadeEstoque);
        etTotal = v.findViewById(R.id.etTotalEstoque);

        baseActivity = new BaseActivity();

        //setting data to edittext
        etUser.setText(baseActivity.userNome);
        userEstoque = etUser.getText().toString();
        etData.setText(String.valueOf(Calendar.getInstance().getTime()));
        etData.setKeyListener(null);//desabilita a edição

        //sorting string-array
        String[] produtos = getResources().getStringArray(R.array.produtos_estoque);
        Arrays.sort(produtos);
        //creating an adapter to be able to set the sorted array
        ArrayAdapter<String> arrayProdutos = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, produtos);

        //setting the adapter with sorted data and defined layout
        spProdutos.setAdapter(arrayProdutos);

        spProdutos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itemEstoque = spProdutos.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                itemEstoque = null;

            }
        });

        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tipoMedidaItemEstoque = spTipo.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                tipoMedidaItemEstoque = null;

            }
        });

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // baseActivity.showProgressDialog();
                if (!validateForm()) {
                   // baseActivity.hideProgressDialog();
                    return;
                }

                try {

                    checkItemCount();

                    codigoRef = "Item-" + (itemCount + 1);

                    Log.d("SALVANDO_DADOS_ESTOQUE", "onClick: " + codigoRef);

                    addRegistro(itemEstoque,
                            Integer.parseInt(etQdade.getText().toString()),
                            tipoMedidaItemEstoque.toUpperCase(),
                            dataEntrada,
                            Integer.parseInt(etTotal.getText().toString()),
                            userEstoque,
                            ativo);



                } catch (Exception error) {

                    //todo tratar erro
                }


            }
        });

        //todo setup views, validate data, save data to firebase

        return v;
    }

    private void addRegistro(String nome, int qdade, String tipoMedida,  String dataIn,
                             int tot, String usuario, boolean isActive) {

        Estoque estoque = new Estoque(nome, qdade, tipoMedida, dataIn,tot, usuario,isActive);
        baseActivity.myRef.child("produtos").push().setValue(estoque);
    }

    public void checkItemCount() {

        baseActivity.myRef.child("estoque").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                itemCount = dataSnapshot.getChildrenCount();


                Log.d("CHECK_ITEM_COUNT", "onDataChange: " + itemCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //todo

                Log.d("EstoqueFragment", "onCancelled: " + databaseError);
            }
        });


    }

    //validate inputs
    private boolean validateForm() {
        dataEntrada = String.valueOf(Calendar.getInstance().getTime());


        boolean valid = true;


        qdadeEstoque = etQdade.getText().toString();

        if (TextUtils.isEmpty(qdadeEstoque)) {

            etQdade.setError(getString(R.string.obrigatorio));

            valid = false;

        } else {
            etTotal.setText(qdadeEstoque);
            etQdade.setError(null);

        }


        return valid;

    }


}

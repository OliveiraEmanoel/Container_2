package br.com.emanoel.oliveira.container.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
    boolean item,itemExist;
    String itemEstoque, qdadeEstoque, userEstoque, totalEstoque, tipoMedidaItemEstoque,codigoRef;
    String dataEntrada;
    String TAG = "ESTOQUE_FRAGMENT";
    long itemCount;
    String itemKey;
    int total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_estoque, container, false);
        getActivity().setTitle("Estoque");
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
        etUser.setKeyListener(null);
        userEstoque = etUser.getText().toString();
        etData.setText(String.valueOf(Calendar.getInstance().getTime()));
        etData.setKeyListener(null);//desabilita a edição
        etTotal.setKeyListener(null);

        etQdade.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_GO){
                    total = Integer.parseInt(etQdade.getText().toString()) + Integer.parseInt(etTotal.getText().toString());
                    etTotal.setText(String.valueOf(total));
                  return true;
                }
                return false;
            }
        });

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

                itemEstoque = spProdutos.getItemAtPosition(position).toString();//ok
                itemExist = findItemTotal(itemEstoque);//checa se item existe

                if(itemExist){//only update database
                    etTotal.setVisibility(View.VISIBLE);
                    etTotal.setText(String.valueOf(total));
                    btAtualizar.setVisibility(View.VISIBLE);
                    btSalvar.setVisibility(View.INVISIBLE);
                    etTotal.setText(String.valueOf(total));

                }else {
                    etTotal.setVisibility(View.VISIBLE);
                    etTotal.setText(String.valueOf(total));
                    btAtualizar.setVisibility(View.INVISIBLE);
                    btSalvar.setVisibility(View.VISIBLE);
                }

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

        btAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //atualizar totalEstoque
                baseActivity.myRef.child("estoque").child(itemKey)
                .child("totalItemEstoque").setValue(total);
                etQdade.setText("");
                etTotal.setText("");
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

        //etTotal.setText(qdadeEstoque);



        return v;
    }
    private boolean findItemTotal(String itemEstoque) {

        //todo acessar bd, ordenar por itemNome, somar total , atualizar esse item ao salvar
        //todo mostrar tvTotal


        // Read from the database
        baseActivity.myRef.child("estoque").orderByChild(itemEstoque).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.getChildrenCount()!=0){
                    Estoque estoque = dataSnapshot.getValue(Estoque.class);
                    item = true;
                    itemKey = dataSnapshot.getKey();
                    total = estoque.getTotalItemEstoque();


                } else {
                    item = false;//todo atualizar não/key não existe
                }
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return item;

    }


    private void addRegistro(String nome, int qdade, String tipoMedida,  String dataIn,
                             int tot, String usuario, boolean isActive) {

        Estoque estoque = new Estoque(nome, qdade, tipoMedida, dataIn,tot, usuario,isActive);
        baseActivity.myRef.child("estoque").push().setValue(estoque);
        etQdade.setText(" ");
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
            etTotal.setText(String.valueOf(total));
            etQdade.setError(null);

        }


        return valid;

    }


}

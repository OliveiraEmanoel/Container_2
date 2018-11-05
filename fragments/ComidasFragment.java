package br.com.emanoel.oliveira.container.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.activities.BaseActivity;
import br.com.emanoel.oliveira.container.adapters.ComidasRecyclerViewAdapter;
import br.com.emanoel.oliveira.container.models.Produtos;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComidasFragment extends Fragment {


    @BindView(R.id.rvComidas)
    RecyclerView rvComidas;
    Unbinder unbinder;
    ComidasRecyclerViewAdapter adapter;
    private List<Produtos> produtosArrayList;
    static Boolean persistence = false;
    BaseActivity baseActivity;
    private final String TAG = "READING_FROM_DATABASE";
    private Produtos produto;


    Context context;


    public ComidasFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_comidas, container, false);
        getActivity().setTitle("Comidas&Porções");
        rvComidas = v.findViewById(R.id.rvComidas);
        baseActivity = new BaseActivity();

        unbinder = ButterKnife.bind(this, v);

        initializeData();

        setLayoutAdapter();

        return v;


    }
    private void initializeData() {

        produtosArrayList = new ArrayList<>();
       produtosArrayList.clear();//clear previous data

        // Read from the database
        baseActivity.myRef.child("produtos").orderByChild("tipo").equalTo("comidas")
                .addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot produtosSnapshot : dataSnapshot.getChildren()){
                    produto = produtosSnapshot.getValue(Produtos.class);
                    produtosArrayList.add(produto);
                    //Log.d(TAG, "Title: " + roupa.getTitle() + ",description " + roupa.getDescription() + " price" + roupa.getPrice());
                }
                setLayoutAdapter();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void setLayoutAdapter(){

        //setting layout manager
        LinearLayoutManager llm = new LinearLayoutManager(ComidasFragment.this.getContext());

        rvComidas.setHasFixedSize(true);
        rvComidas.setLayoutManager(llm);
        //setting adapter
        adapter = new ComidasRecyclerViewAdapter(ComidasFragment.this.getContext(), produtosArrayList);
        rvComidas.setAdapter(adapter);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

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

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.activities.BaseActivity;
import br.com.emanoel.oliveira.container.adapters.SugestoesRecyclerViewAdapter;
import br.com.emanoel.oliveira.container.models.Produtos;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SugestaoFragment extends Fragment {


    @BindView(R.id.rvSugestao)
    RecyclerView rvSugestao;
    Unbinder unbinder;
    SugestoesRecyclerViewAdapter adapter;
    private List<Produtos> produtosArrayList;
    static Boolean persistence = false;
    BaseActivity baseActivity;
    private final String TAG = "READING_FROM_DATABASE";
    private Produtos produto;
    private FirebaseAnalytics mFirebaseAnalytics;

    Context context;


    public SugestaoFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_sugestao, container, false);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        getActivity().setTitle("Sugestões");
        rvSugestao = v.findViewById(R.id.rvSugestao);
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
        baseActivity.myRef.child("produtos").orderByChild("tipo").equalTo("sugestao")
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
        LinearLayoutManager llm = new LinearLayoutManager(SugestaoFragment.this.getContext());

        rvSugestao.setHasFixedSize(true);
        rvSugestao.setLayoutManager(llm);
        //setting adapter
        adapter = new SugestoesRecyclerViewAdapter(SugestaoFragment.this.getContext(), produtosArrayList);
        rvSugestao.setAdapter(adapter);

    }


//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
}

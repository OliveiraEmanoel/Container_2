package br.com.emanoel.oliveira.container.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
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
    private FirebaseAnalytics mFirebaseAnalytics;
    LinearLayoutManager llmComidas;
    Bundle mRvState;
    private final String KEY_RECYCLER_STATE = "recycler_state";


    Context context;


    public ComidasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if(savedInstanceState!=null){

            Parcelable rvState = mRvState.getParcelable(KEY_RECYCLER_STATE);
            rvComidas.getLayoutManager().onRestoreInstanceState(rvState);

        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mRvState = new Bundle();
        View v = inflater.inflate(R.layout.fragment_comidas, container, false);
        getActivity().setTitle("Comidas&Porções");
        rvComidas = v.findViewById(R.id.rvComidas);
        baseActivity = new BaseActivity();


        unbinder = ButterKnife.bind(this, v);

        initializeData();

        setLayoutAdapter();

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    Parcelable rvState = rvComidas.getLayoutManager().onSaveInstanceState();

                    mRvState.putParcelable(KEY_RECYCLER_STATE,rvState);
                    onSaveInstanceState(mRvState);
                   getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });

        return v;


    }

    private void initializeData() {

        produtosArrayList = new ArrayList<>();
        produtosArrayList.clear();//clear previous data

        // Read from the database
        baseActivity.myRef.child("produtos").orderByChild("tipo").equalTo("comidas")
                //baseActivity.myRef.keepSynced(true);
                //baseActivity.myRef
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot produtosSnapshot : dataSnapshot.getChildren()) {
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

    private void setLayoutAdapter() {

        //setting layout manager
        llmComidas = new LinearLayoutManager(ComidasFragment.this.getContext());


        rvComidas.setHasFixedSize(true);
        rvComidas.setItemViewCacheSize(20);
        rvComidas.setDrawingCacheEnabled(true);
        rvComidas.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvComidas.setLayoutManager(llmComidas);
        //setting adapter
        adapter = new ComidasRecyclerViewAdapter(ComidasFragment.this.getContext(), produtosArrayList);
        rvComidas.setAdapter(adapter);

    }
    @Override
    public void onSaveInstanceState(Bundle RvState){

//        mRvState = new Bundle();
//        Parcelable rvState = rvComidas.getLayoutManager().onSaveInstanceState();
//        mRvState.putParcelable(KEY_RECYCLER_STATE,rvState);
        super.onSaveInstanceState(RvState);
    }

    @Override
    public void onStop() {
        super.onStop();

        super.onSaveInstanceState(mRvState);

    }

    @Override
    public void onPause() {
        super.onPause();
        //save state of recycler view

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mRvState!=null){

            Parcelable rvState = mRvState.getParcelable(KEY_RECYCLER_STATE);
            rvComidas.getLayoutManager().onRestoreInstanceState(rvState);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind();
        mRvState = new Bundle();
        Parcelable rvState = rvComidas.getLayoutManager().onSaveInstanceState();
        mRvState.putParcelable(KEY_RECYCLER_STATE,rvState);
        super.onSaveInstanceState(mRvState);
    }
}

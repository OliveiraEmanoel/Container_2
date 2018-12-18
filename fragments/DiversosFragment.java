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
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.activities.BaseActivity;
import br.com.emanoel.oliveira.container.adapters.DiversosRecyclerViewAdapter;
import br.com.emanoel.oliveira.container.models.Promocao;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiversosFragment extends Fragment {

    CalendarView cVEventos;

    RecyclerView rvDiversos;
    TextView tvMaisDetalhes;

    String dataEvento;

    DiversosRecyclerViewAdapter adapter;
    private List<Promocao> eventosArrayList;

    BaseActivity baseActivity;
    private final String TAG = "READING_FROM_DATABASE";
    private Promocao evento;
    private FirebaseAnalytics mFirebaseAnalytics;
    LinearLayoutManager llmEventos;
    Bundle mRvState;
    private final String KEY_RECYCLER_STATE = "recycler_state";


    Context context;

    public DiversosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_diversos, container, false);
        getActivity().setTitle("Promoções");
        baseActivity = new BaseActivity();
        rvDiversos = v.findViewById(R.id.rvDetalhesDiversos);

        initializeData();
        setLayoutAdapter();

        return v;
    }

    private void initializeData() {

        eventosArrayList = new ArrayList<>();
        eventosArrayList.clear();//clear previous data

        // Read from the database
        baseActivity.myRef.child("promocoes").orderByChild("eventoAtivo").equalTo(true)
                //baseActivity.myRef.keepSynced(true);
                //baseActivity.myRef
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot eventosSnapshot : dataSnapshot.getChildren()) {
                            evento = eventosSnapshot.getValue(Promocao.class);
                            eventosArrayList.add(evento);
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
        llmEventos = new LinearLayoutManager(DiversosFragment.this.getContext());


        rvDiversos.setHasFixedSize(true);
        rvDiversos.setItemViewCacheSize(20);
        rvDiversos.setDrawingCacheEnabled(true);
        rvDiversos.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvDiversos.setLayoutManager(llmEventos);
        //setting adapter
        adapter = new DiversosRecyclerViewAdapter(DiversosFragment.this.getContext(), eventosArrayList);
        rvDiversos.setAdapter(adapter);

    }

}

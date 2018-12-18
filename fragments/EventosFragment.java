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
import br.com.emanoel.oliveira.container.adapters.EventosRecyclerViewAdapter;
import br.com.emanoel.oliveira.container.models.Eventos;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventosFragment extends Fragment {



    CalendarView cVEventos;

    RecyclerView rvEventos;
    TextView tvMaisDetalhes;

    String dataEvento;

    EventosRecyclerViewAdapter adapter;
    private List<Eventos> eventosArrayList;

    BaseActivity baseActivity;
    private final String TAG = "READING_FROM_DATABASE";
    private Eventos evento;
    private FirebaseAnalytics mFirebaseAnalytics;
    LinearLayoutManager llmEventos;
    Bundle mRvState;
    private final String KEY_RECYCLER_STATE = "recycler_state";


    Context context;


    public EventosFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_eventos, container, false);
        getActivity().setTitle("Eventos");
        baseActivity = new BaseActivity();
        rvEventos = v.findViewById(R.id.rvDetalhesEventos);

        initializeData();
        setLayoutAdapter();
//        cVEventos = v.findViewById(R.id.cV_eventos);
//
//
//        cVEventos.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//
//                dataEvento = String.valueOf(dayOfMonth) + "/" + String.valueOf(month+1)
//                        + "/" + String.valueOf(year);
//                //Toast.makeText(getContext(),"Data: " + dataEvento ,Toast.LENGTH_SHORT).show();
//                initializeData();
//
//                setLayoutAdapter();
//            }
//        });


        return v;
    }
    private void initializeData() {

        eventosArrayList = new ArrayList<>();
        eventosArrayList.clear();//clear previous data

        // Read from the database
        baseActivity.myRef.child("eventos").orderByChild("eventoAtivo").equalTo(true)
                //baseActivity.myRef.keepSynced(true);
                //baseActivity.myRef
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot eventosSnapshot : dataSnapshot.getChildren()) {
                            evento = eventosSnapshot.getValue(Eventos.class);
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
        llmEventos = new LinearLayoutManager(EventosFragment.this.getContext());


        rvEventos.setHasFixedSize(true);
        rvEventos.setItemViewCacheSize(20);
        rvEventos.setDrawingCacheEnabled(true);
        rvEventos.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvEventos.setLayoutManager(llmEventos);
        //setting adapter
        adapter = new EventosRecyclerViewAdapter(EventosFragment.this.getContext(), eventosArrayList);
        rvEventos.setAdapter(adapter);

    }


//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//
//    }


}

package br.com.emanoel.oliveira.container.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.emanoel.oliveira.container.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListagemFragment extends Fragment {


    public ListagemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listagem, container, false);
    }

}

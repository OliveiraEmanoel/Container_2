package br.com.emanoel.oliveira.container.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import br.com.emanoel.oliveira.container.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {


    public MenuFragment() {
        // Required empty public constructor
    }

    private ImageView ivBebida, ivComida, ivSugestao;
    private BebidasFragment bebidasFragment;
    private ComidasFragment comidasFragment;
    private SugestaoFragment sugestaoFragment;

    private int frameId;
    String teste;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        ivBebida = view.findViewById(R.id.ivQueroBeber);
        ivComida = view.findViewById(R.id.ivQueroComer);
        ivSugestao = view.findViewById(R.id.ivSugestaoDia);

        bebidasFragment = new BebidasFragment();
        comidasFragment = new ComidasFragment();
        sugestaoFragment = new SugestaoFragment();

        frameId = R.id.frMenuPrincipal;

        ivSugestao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //carregar fragment sugestao
                callFragment(frameId, sugestaoFragment);
            }
        });

        ivComida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //carregar fragment comida

                callFragment(frameId, comidasFragment);
            }
        });

        ivBebida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //carregar fragment bebida

                callFragment(frameId, bebidasFragment);
            }
        });

        //restaura o titulo do menu
        getActivity().setTitle("Menu");

        return view;


    }


    public void callFragment(int i, Fragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().addToBackStack(teste);
        transaction.replace(i, fragment);
        transaction.commit();

    }


}

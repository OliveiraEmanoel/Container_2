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
public class RifaFragment extends Fragment {
/* colocar nomes de bandas de rock
* cliente clica no nome da banda e aparece uma caixa de dialogo para ele marcar o nome, data e pagamento
* se cliente reservou o clic fica indisponivel? ou deixamos aparecer  a caixa de dialogo com os dados do comprador?
* marcar nome ou telefone ou userID?
* uma foto do brinde?*/

    public RifaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rifa, container, false);
    }

}

package br.com.emanoel.oliveira.container.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.activities.BaseActivity;
import br.com.emanoel.oliveira.container.fragments.DiversosFragment;
import br.com.emanoel.oliveira.container.fragments.ReservasFragment;
import br.com.emanoel.oliveira.container.models.Promocao;
import br.com.emanoel.oliveira.container.models.Reserva;

import static br.com.emanoel.oliveira.container.activities.BaseActivity.userID;

public class DiversosRecyclerViewAdapter extends RecyclerView.Adapter<DiversosRecyclerViewAdapter.ViewHolder> {

    private List<Promocao> promocoes;
    private Context context;
    String TAG = "RECYCLERVIEW_HOLDER";
    private ProgressDialog progressDialog;
    TextView tvDetalhes;
    BaseActivity baseActivity;
    DiversosFragment diversosFragment;
    ReservasFragment reservasFragment;
    Button btReserva;
    String user, nomeEvento, dataEvento, dataReserva;
    boolean ativo = true;
    boolean hasReserva;
    int mesa, qdadePessoas;
    CoordinatorLayout coordinatorLayout;


    View v;
    DecimalFormat f;


    public DiversosRecyclerViewAdapter(Context context, List<Promocao> promocoes) {
        this.promocoes = promocoes;
        this.context = context;
    }

    private Context getContext() {

        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalhes_eventos, parent, false);

        tvDetalhes = v.findViewById(R.id.tvDetalhesEvento);
        btReserva = v.findViewById(R.id.btReservar);
        baseActivity = new BaseActivity();
        diversosFragment = new DiversosFragment();
        reservasFragment = new ReservasFragment();

        btReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = userID;

                //todo chamar reserva activity ou fragment...
                addRegistro(user, nomeEvento, dataEvento, mesa, qdadePessoas, ativo, dataReserva);
            }
        });



        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        f = new DecimalFormat("'R$' 0.00");
        final Promocao promocao = promocoes.get(position);
        Log.d(TAG, "position = " + position + "title = " + promocao.getNomeEvento());
        //TextView tvNameItemRoupas = (TextView) v.findViewById(R.id.tv_name_item_roupas);

        holder.tvNomeEvento.setText(promocao.getNomeEvento());
        nomeEvento = promocao.getNomeEvento();
        dataEvento = promocao.getDataEvento();
        //dataReserva =
        mesa = 0;
        qdadePessoas = 1;
        //holder.tvDetalhesEvento.setText("mais detalhes...");
        holder.tvDescricaoEvento.setText(promocao.getDescricaoEvento());

        if(checaIfUserHasReserva(userID,nomeEvento)){
            btReserva.setVisibility(View.INVISIBLE);
        }

        // showProgressDialog();
        Picasso picasso = new Picasso.Builder(getContext())
                .memoryCache(new LruCache(250000))
                .build();

        picasso.get()
                //.load("https://firebasestorage.googleapis.com/v0/b/container-3749e.appspot.com/o/container%2Fprodutos%2F20181026_221205.jpg?alt=media&token=e9ca3f59-bda6-4723-9f81-53a8fac99371")
                //.placeholder(R.drawable.progress_animation)
                .load(promocao.getPhotoPath())
                .error(R.drawable.logo_container_original)
                .into(holder.ivEvento, new Callback() {
                    @Override
                    public void onSuccess() {
                        //hideProgressDialog();
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: " + e.toString() + promocao.getPhotoPath().toString());
                        // hideProgressDialog();
                    }

                });


    }

    @Override
    public int getItemCount() {
        return promocoes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivEvento;
        TextView tvNomeEvento;
        TextView tvDescricaoEvento;
        Button btReserva;
        TextView tvDetalhesEvento;
        CardView cvEventos;

        public ViewHolder(View itemView) {
            super(itemView);

            tvDescricaoEvento = itemView.findViewById(R.id.tvDescricaoEvento);
            tvNomeEvento = itemView.findViewById(R.id.tvNomeDetalheEvento);
            //tvDetalhesEvento = itemView.findViewById(R.id.tvDetalhesEvento);
            ivEvento = itemView.findViewById(R.id.ivDetalheEvento);
            btReserva = itemView.findViewById(R.id.btReservar);

        }
    }

    private void addRegistro(String user, String nomeEvento, String dataEvento, int mesa, int qdadePessoas
            , boolean isActive, String dataReserva) {

        Reserva reserva = new Reserva(user, nomeEvento, dataEvento, mesa, qdadePessoas, isActive, dataReserva);
        baseActivity.myRef.child("reservas").push().setValue(reserva);

        Toast.makeText(getContext(), "Reserva feita com sucesso! Entraremos em contato. Obrigado.", Toast.LENGTH_LONG).show();

        btReserva.setVisibility(View.INVISIBLE);

    }

    private boolean checaIfUserHasReserva (String user, final String nomeEvento){

        hasReserva = false;
        user = userID;

        baseActivity.myRef.child("reservas").orderByChild("user").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Reserva reserva = new Reserva();
                reserva = dataSnapshot.getValue(Reserva.class);

              //  if(dataSnapshot.getChildrenCount()!= 0 && reserva.getNomeEvento().equals(nomeEvento) ){

                   hasReserva = true;
                    btReserva.setVisibility(View.INVISIBLE);

               // }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getContext(),"Erro verificando se tem reserva" + databaseError.toString(),Toast.LENGTH_SHORT).show();

            }
        });


        return hasReserva;
    }
}
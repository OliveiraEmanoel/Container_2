package br.com.emanoel.oliveira.container.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.models.Produtos;

public class BebidasRecyclerViewAdapter extends RecyclerView.Adapter<BebidasRecyclerViewAdapter.ViewHolder> {

    private List<Produtos> produtos;
    private Context context;
    String TAG = "RECYCLERVIEW_HOLDER";
    private ProgressDialog progressDialog;
    View v;
    DecimalFormat f;



    public BebidasRecyclerViewAdapter(Context context, List<Produtos> produtos) {
        this.produtos = produtos;
        this.context = context;
    }

    private Context getContext() {

        return context;
    }



    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produtos_bebidas, parent, false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        f = new DecimalFormat("'R$' 0.00");
        final Produtos produto = produtos.get(position);
        Log.d(TAG, "position = " + position + "title = " + produto.getNome());
        //TextView tvNameItemRoupas = (TextView) v.findViewById(R.id.tv_name_item_roupas);

        holder.tvNomeItem.setText(produto.getNome());
        holder.tvPrecoItemInteiro.setText(f.format(produto.getPrice()));
        holder.tvDescricaoItem.setText(produto.getDescription());

        //showProgressDialog();

        Picasso picasso = new Picasso.Builder(getContext())
                .memoryCache(new LruCache(250000))
                .build();


        picasso.get()
                //.load("https://firebasestorage.googleapis.com/v0/b/container-3749e.appspot.com/o/container%2Fprodutos%2F20181026_221205.jpg?alt=media&token=e9ca3f59-bda6-4723-9f81-53a8fac99371")
                //.placeholder(R.drawable.progress_animation)
                .load(produto.getFotoPath())
                .error(R.drawable.logo_container_original)
                .into(holder.imageView2, new Callback() {
                    @Override
                    public void onSuccess() {
                        //hideProgressDialog();
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: " + e.toString() + produto.getFotoPath().toString());
                       // hideProgressDialog();
                    }


                });

    }


    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //    @BindView(R.id.imageView2)
        ImageView imageView2;
        //    @BindView(R.id.tvNomeItem)
        TextView tvNomeItem;
        //    @BindView(R.id.tvDescricaoItem)
        TextView tvDescricaoItem;
        //    @BindView(R.id.tvPrecoItemInteiro)
        TextView tvPrecoItemInteiro;
        //    @BindView(R.id.tvPrecoItemMeia)
        TextView tvPrecoItemMeia;
        //    @BindView(R.id.cvComidas)
        CardView cvComidas;

        public ViewHolder(View itemView) {
            super(itemView);

            tvDescricaoItem = itemView.findViewById(R.id.tvDescricaoItem);
            tvNomeItem = itemView.findViewById(R.id.tvNomeItem);
            tvPrecoItemInteiro = itemView.findViewById(R.id.tvPrecoItemInteiro);
            imageView2 = itemView.findViewById(R.id.ivQrCode);

        }
    }

    public void showProgressDialog() {

        if (progressDialog == null) {

            progressDialog = new ProgressDialog(getContext());

            progressDialog.setCancelable(false);

            progressDialog.setMessage("Carregando...");

        }
        progressDialog.show();
    }

    public void hideProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {

            progressDialog.dismiss();

        }

    }
}

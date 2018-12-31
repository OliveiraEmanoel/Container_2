package br.com.emanoel.oliveira.container.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.activities.BaseActivity;
import br.com.emanoel.oliveira.container.activities.PedidoActivity;
import br.com.emanoel.oliveira.container.activities.ScanQrCodeActivity;
import br.com.emanoel.oliveira.container.models.Pedido;
import br.com.emanoel.oliveira.container.models.Produtos;

public class ComidasRecyclerViewAdapter extends RecyclerView.Adapter<ComidasRecyclerViewAdapter.ViewHolder> {

    private List<Produtos> produtos;
    private List<Pedido> pedido;
    private Context context;
    String TAG = "RECYCLERVIEW_HOLDER";
    private ProgressDialog progressDialog;
    BaseActivity baseActivity;
    View v;
    DecimalFormat f;
    PedidoActivity pedidoActivity;
    Pedido meuPedido;



    public ComidasRecyclerViewAdapter(Context context, List<Produtos> produtos) {
        this.produtos = produtos;
        this.context = context;
    }

    private Context getContext() {

        return context;
    }



    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produtos_comidas, parent, false);
        baseActivity = new BaseActivity();

        //to avoid fileuriexposeexcption
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

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

        if(produto.getFavorito()>0){

            holder.tvFavoritoCount.setText(String.valueOf(produto.getFavorito()));

        }else {
            holder.tvFavoritoCount.setText("");
        }

       // showProgressDialog();

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
        ImageView imageView2;
        TextView tvNomeItem;
        TextView tvDescricaoItem;
        TextView tvPrecoItemInteiro;
        TextView tvPrecoItemMeia;
        CardView cvComidas;

        ImageButton ibFavorito,ibComprar,ibShare;
        TextView tvFavoritoCount;

        public ViewHolder(View itemView) {
            super(itemView);

            tvDescricaoItem = itemView.findViewById(R.id.tvDescricaoItem);
            tvNomeItem = itemView.findViewById(R.id.tvNomeItem);
            tvPrecoItemInteiro = itemView.findViewById(R.id.tvPrecoItemInteiro);
            imageView2 = itemView.findViewById(R.id.imageView2);

            ibComprar = itemView.findViewById(R.id.ibComprar);
            ibFavorito = itemView.findViewById(R.id.ibfavorito);
            ibShare = itemView.findViewById(R.id.ibShare);
            tvFavoritoCount = itemView.findViewById(R.id.tvfavoritoCount);

            ibShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    imageView2.buildDrawingCache();

                    saveAndShare(imageView2);
                }


            });

            ibComprar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo checar nropedido, nro de mesa,salvar pedido, mostrar pedido

                    //checa se usuario está conectado no mesmo wifi do estabelecimento

                    if (baseActivity.nomeWifiAtual!= "") {

                        Log.e("CHECANDO WIFI", "onCreate: "+ baseActivity.currentConnectedSSID );

                    }else {

                        Toast.makeText(getContext(),"Pedidos? Solicite a senha do WIFI/local...",Toast.LENGTH_LONG).show();
                        //btAddLanche.setVisibility(View.GONE);
                        ibComprar.setVisibility(View.GONE);
                    }

                    if(!baseActivity.mesaHasUser) {//se nro >0 significa que já foi lido o Qrcode...
                        lerQRcodeDaMesa(); //reading table number, retorna -1 qdo der errado a leitura
                    }else{//dar sequencia na compra do item

                        //Lanches lanche = (Lanches) object;
                       // String nome = viewNome.getText().toString();
                       // lanche = mList.get(getLayoutPosition());
                        String nroPedido = "1";
                        String nomeProduto = tvNomeItem.getText().toString();
                        String descriProduto = tvDescricaoItem.getText().toString();
                        String obs = "";
                        int qdadeProduto = 1;
                        double valorPedido = Double.parseDouble(tvPrecoItemInteiro.getText().toString());
                        boolean viagem = false;
                        boolean entregue = false;
                        int nroMesa = baseActivity.nroMesa;
                        String nomeCliente = baseActivity.userNome;
                        String celCliente = "";
                        String dataPedido = "";
                        String horaPedido = "";
                        String userId = baseActivity.userID;
                        String status = "aberto";



                        salvarPedidoTemp(nroPedido, nomeProduto, descriProduto,
                                 obs, qdadeProduto,
                                valorPedido, viagem, entregue, nroMesa, nomeCliente,
                                celCliente, dataPedido, horaPedido,
                                userId, status
                        );

                    }

                }
            });

        }
    }

    public void salvarPedidoTemp(String nroPedido, String nomeProduto, String descriProduto,
                                 String obs, int qdade, double valorPedido, boolean viagem,
                                 boolean entregue, int nroMesa, String nomeCliente, String celCliente, String dataPedido
            , String horaPedido, String userId, String status) {

        pedido.add(new Pedido(nroPedido, nomeProduto, descriProduto, obs, qdade, valorPedido,
                viagem, entregue, nroMesa,nomeCliente, celCliente, dataPedido, horaPedido, userId, status
        ));

        meuPedido.setNroPedido(nroPedido);
        meuPedido.setNomeProduto(nomeProduto);
        meuPedido.setDescriProduto(descriProduto);

        meuPedido.setQdadeProduto(qdade);
        meuPedido.setValorPedido(valorPedido);
        meuPedido.setViagem(viagem);
        meuPedido.setEntregue(entregue);
        meuPedido.setNomeCliente(nomeCliente);
        meuPedido.setCelCliente(celCliente);
        meuPedido.setDataPedido(dataPedido);
        meuPedido.setHoraPedido(horaPedido);
        meuPedido.setUserId(userId);
        meuPedido.setStatus(status);

        baseActivity.totalPedido = baseActivity.totalPedido + meuPedido.getQdadeProduto()*meuPedido.getValorPedido();

        // pedido.add(meuPedido);
        Log.e("Salvando pedido", "tamanho = " + pedido.size() + " qdade= " + qdade);

        getContext().startActivity(new Intent(getContext(),PedidoActivity.class));

    }

    public  void saveAndShare(ImageView p0){

        BitmapDrawable drawable = (BitmapDrawable) p0.getDrawable();
        Bitmap img = drawable.getBitmap();

        //escolhe onde será gravada a imagem
        File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //context.getExternalFilesDir(null);
        //define o nome do arquivo
        String filename = "temp.jpg";
        File file = new File(storageLoc, filename);
        //grava o arquivo
        try {
            FileOutputStream fos = new FileOutputStream(file);
            img.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            //atualiza o view da pasta onde foi graqvado o arquivo
            Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            scanIntent.setData(Uri.fromFile(file));
            getContext().sendBroadcast(scanIntent);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //compartilha a imagem gravada utilizando o programa que o cliente quiser
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        getContext().startActivity(Intent.createChooser(share, "Compartilhar usando: "));

    }

    public void lerQRcodeDaMesa(){

                Intent intent = new Intent(getContext(),ScanQrCodeActivity.class);

                getContext().startActivity(intent);

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

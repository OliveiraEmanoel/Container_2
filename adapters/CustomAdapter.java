package br.com.emanoel.oliveira.container.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.activities.BaseActivity;
import br.com.emanoel.oliveira.container.models.Pedido;

import static br.com.emanoel.oliveira.container.activities.BaseActivity.totalPedido;

public class CustomAdapter extends ArrayAdapter<Pedido> implements View.OnClickListener{
    BaseActivity baseActivity;
    List<Pedido> pedido = baseActivity.getPedido();

    public DecimalFormat f = new DecimalFormat("R$ 0.00");
    Context mContext;
    int grupoColor;
    RelativeLayout relativeLayout;
    boolean cbChecked;

 
    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtQdade;
        CheckBox cbDelete;
        TextView tvValor;

    }

    public CustomAdapter(List<Pedido> pedido, Context context, int grupoColor) {
        super(context, R.layout.list_rv_pedido, pedido);
        this.pedido = pedido;
        this.mContext=context;
        this.grupoColor = grupoColor;

    }
 
    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Pedido dataModel=(Pedido)object;


      }
 
    private int lastPosition = -1;
 
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Pedido dataModel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag


 
        final View result;
 
        if (convertView == null) {
 
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_rv_pedido, parent, false);

            viewHolder.txtName = (TextView) convertView.findViewById(R.id.tvNomeLV);
            viewHolder.txtQdade = (TextView) convertView.findViewById(R.id.tvQdadeLV);
            viewHolder.cbDelete = convertView.findViewById(R.id.cbExcluirLV);
            viewHolder.tvValor = convertView.findViewById(R.id.tvValorItemPedido);


 
            result=convertView;
 
            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.cbDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    String tot;
                    totalPedido = totalPedido - pedido.get(position).getValorPedido();
                    pedido.remove(position);
                    notifyDataSetChanged();//to refresh listView
                    viewHolder.cbDelete.setChecked(false);


                }
            }
        });
 

        viewHolder.txtName.setText(dataModel.getNomeProduto());
        viewHolder.txtQdade.setText(String.valueOf(dataModel.getQdadeProduto()));
        String valorItem = String.valueOf(dataModel.getValorPedido());

        viewHolder.tvValor.setText(f.format(pedido.get(position).getValorPedido()));

        return convertView;
    }

    public double getTotalPedido(){

        return totalPedido;
    }
}
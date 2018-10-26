package br.com.emanoel.oliveira.container.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.activities.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProdutosMenuFragment extends Fragment {


    @BindView(R.id.ivFotoProduto)
    ImageView ivFotoProduto;
    @BindView(R.id.btBrowse)
    Button btBrowse;
    @BindView(R.id.etNome)
    EditText etNome;
    @BindView(R.id.etValor)
    EditText etValor;
    @BindView(R.id.etDetalhes)
    EditText etDetalhes;
    @BindView(R.id.btLimparDescription)
    Button btLimparDescription;
    @BindView(R.id.btSalvarProduto)
    Button btSalvarProduto;
    Unbinder unbinder;

    BaseActivity baseActivity;

    public ProdutosMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_produtos_menu, container, false);
        unbinder = ButterKnife.bind(this, v);
        baseActivity = new BaseActivity();
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ivFotoProduto)
    public void onIvFotoProdutoClicked() {
    }

    @OnClick(R.id.btBrowse)
    public void onBtBrowseClicked() {

//        checkItemCount();
//        Log.d("CHECK_ITEM_COUNT", "onDataChange: " + baseActivity.itemCount);
//        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.btLimparDescription)
    public void onBtLimparDescriptionClicked() {

        etDetalhes.setText("");
    }

    @OnClick(R.id.btSalvarProduto)
//    public void onBtSalvarProdutoClicked() {
//
//        validate();
//
//        if (!validate()){
//            return;
//        }
//
//        checkItemCount();
//
//        codigoRef = "ALM-" + (itemCount+1);
//
//        Log.d("SALVANDO_DADOS", "onClick: " + codigoRef);
//
//        addRegistro(etNome.getText().toString().toUpperCase(),
//                Double.parseDouble(etValor.getText().toString()),
//                description,
//                photoUrl,
//                dataEntrada,
//                etTamanho.getText().toString(),
//                etTecido.getText().toString().toUpperCase(),
//                codigoRef,
//                isActive,isNovidade);
//
//    }
//
//    private void addRegistro(String nome, double price,String description,String fotoPath,String dataIn,
//                             String tamanho,String tecido,String codigo,boolean isActive,boolean isNovidade) {
//
//        Almofada almofada = new Almofada(nome,price,description,photoUrl,dataIn,tamanho,tecido,codigo,isActive,isNovidade);
//        myRef.child("almofadas").push().setValue(almofada);
//    }
//
//    private boolean validate() {
//        dataEntrada = String.valueOf(Calendar.getInstance().getTime());
//        boolean valid = true;
//        if (imageURI!=null) {
//
//
//            String nome = etNome.getText().toString().toUpperCase();
//            if (TextUtils.isEmpty(nome)) {
//                etNome.setError(getString(R.string.obrigatorio));
//                valid = false;
//            } else {
//                etNome.setError(null);
//            }
//            String valunit = etValor.getText().toString();
//            if (TextUtils.isEmpty(valunit)) {
//                etValor.setError(getString(R.string.obrigatorio));
//                valid = false;
//            } else {
//                etValor.setError(null);
//            }
//
//            String valM = etTamanho.getText().toString();
//            if (TextUtils.isEmpty(valM)) {
//                etTamanho.setError(getString(R.string.obrigatorio));
//                valid = false;
//            } else {
//                etTamanho.setError(null);
//            }
//            String valG = etTecido.getText().toString().toUpperCase();
//            if (TextUtils.isEmpty(valG)) {
//                etTecido.setError(getString(R.string.obrigatorio));
//                valid = false;
//            } else {
//                etTecido.setError(null);
//                //description = etTecido.getText().toString();
//            }
//            String valGG = etDetalhes.getText().toString().toUpperCase();
//            if (TextUtils.isEmpty(valGG)) {
//                etDetalhes.setError(getString(R.string.obrigatorio));
//                valid = false;
//            } else {
//                etDetalhes.setError(null);
//                description = etDetalhes.getText().toString().toUpperCase();
//            }
//        } else{
//
//            Toast.makeText(getContext(),"Clic no botão BROWSE para escolher uma foto.",Toast.LENGTH_LONG).show();
//            valid = false;
//        }
//        return valid;
//
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if result is ok shows the image in imageview
        super.onActivityResult(requestCode, resultCode, data);
        //ContentResolver c ;

//        if (resultCode == RESULT_OK){
//            Uri targetUri = data.getData();
//
//            imageURI =  getRealPathFromURI(this,targetUri);
//            imageFileName = getRealTitleFromURI(this,targetUri);
//
//            salvarFotoOnCloud();
//
//
//            //Toast.makeText(this,imageURI,Toast.LENGTH_LONG).show();
//            Log.d("Cadastro",imageFileName);
//            Bitmap bitmap;
//            try {
//                //todo
//                // bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
//                //ivProduto.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
    }

    public void checkItemCount(){

        baseActivity.myRef.child("almofadas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //itemCount = dataSnapshot.getChildrenCount();


                //Log.d("CHECK_ITEM_COUNT", "onDataChange: " + itemCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //todo

                Log.d("Cadastro.class", "onCancelled: " + databaseError);
            }
        });


    }

//    public void salvarFotoOnCloud(){
//
//        baseActivity.myStorageRef = FirebaseStorage.getInstance().getReference("sextoDir/almofadas");
//        Uri file = Uri.fromFile(new File(imageURI));
//
//
//
//        myStorageRef.child(imageFileName.toLowerCase()).putFile(file)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // Get a URL to the uploaded content
//                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
//
//                        photoUrl = downloadUrl.toString();
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle unsuccessful uploads
//                        // ...
//                        Toast.makeText(CadastroProdutos.this,"Erro salvando foto! " + exception.toString(),Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//    }
}

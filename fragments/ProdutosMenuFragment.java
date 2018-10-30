package br.com.emanoel.oliveira.container.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.activities.BaseActivity;
import br.com.emanoel.oliveira.container.models.Produtos;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProdutosMenuFragment extends Fragment {



    ImageView ivFotoProduto;

    Button btBrowse;

    EditText etNome;

    EditText etValor;

    EditText etDetalhes;

    Button btLimparDescription;

    Button btSalvarProduto;


    BaseActivity baseActivity;
    private long itemCount;
    private String imageURI, imageFileName;
    private String codigoRef, tipo;
    private String photoUrl, description, dataEntrada;
    private boolean isActive = true;

    public ProdutosMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_produtos_menu, container, false);

        baseActivity = new BaseActivity();
        btBrowse = v.findViewById(R.id.btBrowse);
        ivFotoProduto = v.findViewById(R.id.ivFotoProduto);
        etDetalhes = v.findViewById(R.id.etDetalhes);
        etNome = v.findViewById(R.id.etNome);
        etValor = v.findViewById(R.id.etValor);
        btLimparDescription = v.findViewById(R.id.btLimparDescription);
        btSalvarProduto = v.findViewById(R.id.btSalvarProduto);

        btBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkItemCount();
                Log.d("CHECK_ITEM_COUNT", "onDataChange: " + itemCount);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        btLimparDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDetalhes.setText("");
            }
        });

        btSalvarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.showProgressDialog();
                validate();

                if (!validate()) {
                    baseActivity.hideProgressDialog();
                    return;
                }

                checkItemCount();

                codigoRef = "Produto-" + (itemCount + 1);

                Log.d("SALVANDO_DADOS", "onClick: " + codigoRef);

                addRegistro(etNome.getText().toString().toUpperCase(),
                        Double.parseDouble(etValor.getText().toString()),
                        description,
                        photoUrl,
                        dataEntrada,
                        codigoRef,
                        isActive,
                        tipo);

            }
        });


        return v;
    }


    private void addRegistro(String nome, double price, String description, String fotoPath, String dataIn,
                             String codigo, boolean isActive, String tipo) {

        Produtos produtos = new Produtos(nome, price, description, photoUrl, dataIn, codigo, isActive, tipo);
        baseActivity.myRef.child("produtos").push().setValue(produtos);
    }

    private boolean validate() {
        dataEntrada = String.valueOf(Calendar.getInstance().getTime());
        boolean valid = true;
        if (imageURI != null) {

            String nome = etNome.getText().toString().toUpperCase();
            if (TextUtils.isEmpty(nome)) {
                etNome.setError(getString(R.string.obrigatorio));
                valid = false;
            } else {
                etNome.setError(null);
            }
            String valunit = etValor.getText().toString();
            if (TextUtils.isEmpty(valunit)) {
                etValor.setError(getString(R.string.obrigatorio));
                valid = false;
            } else {
                etValor.setError(null);
            }

            String valGG = etDetalhes.getText().toString().toUpperCase();//valGG nada a ver...apenas reaproveitando codigo
            if (TextUtils.isEmpty(valGG)) {
                etDetalhes.setError(getString(R.string.obrigatorio));
                valid = false;
            } else {
                etDetalhes.setError(null);
                description = etDetalhes.getText().toString().toUpperCase();
            }
        } else {

            Toast.makeText(getContext(), "Clic no botão BROWSE para escolher uma foto.", Toast.LENGTH_LONG).show();
            valid = false;
        }
        return valid;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if result is ok shows the image in imageview
        super.onActivityResult(requestCode, resultCode, data);
        //ContentResolver c ;

        if (resultCode == getActivity().RESULT_OK) {
            Uri targetUri = data.getData();

            imageURI = baseActivity.getRealPathFromURI(getContext(), targetUri);
            imageFileName = baseActivity.getRealTitleFromURI(getContext(), targetUri);

            salvarFotoOnCloud();


            //Toast.makeText(this,imageURI,Toast.LENGTH_LONG).show();
            Log.d("Cadastro", imageFileName);
            Bitmap bitmap;
            try {

                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                ivFotoProduto.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void checkItemCount() {

        baseActivity.myRef.child("produtos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                itemCount = dataSnapshot.getChildrenCount();


                Log.d("CHECK_ITEM_COUNT", "onDataChange: " + itemCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //todo

                Log.d("Cadastro.class", "onCancelled: " + databaseError);
            }
        });


    }

    public void salvarFotoOnCloud() {

        //baseActivity.showProgressDialog();

        try {

            baseActivity.myStorageRef = FirebaseStorage.getInstance().getReference("container/produtos");
            Uri file = Uri.fromFile(new File(imageURI));


            baseActivity.myStorageRef.child(imageFileName.toLowerCase()).putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getUploadSessionUri();//todo metodo mudou aqui... ver se funciona

                            photoUrl = downloadUrl.toString();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Toast.makeText(getActivity(), "Erro salvando foto! " + exception.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
           // baseActivity.hideProgressDialog();

        } catch (Exception error) {

            Log.e("salvarFotoOnCloud", "Erro salvando foto: " + error);
        }
    }

}

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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
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
    RadioGroup radioGroup;
    Button btSalvarProduto;
    BaseActivity baseActivity;
    private long itemCount;
    private String imageURI, imageFileName, imageName, downloadUrl;
    private String codigoRef, tipo;
    private String photoUrl, description, dataEntrada;
    private boolean isActive = true;
    private String TAG = "ProdutosMenuFragment: ";
    Uri file;
    private FirebaseAnalytics mFirebaseAnalytics;

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
        radioGroup = v.findViewById(R.id.rgTipoProdutoMenu);

        btSalvarProduto = v.findViewById(R.id.btSalvarProduto);

        baseActivity.myStorageRef = FirebaseStorage.getInstance().getReference("container/produtos");

        btBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkItemCount();
                Log.d("CHECK_ITEM_COUNT", "onDataChange: " + itemCount);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);

            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbBebidasMenu) {
                    tipo = "bebidas";
                } else if (checkedId == R.id.rbComidasMenu) {
                    tipo = "comidas";
                } else if (checkedId == R.id.rbSugestaoMenu) {
                    tipo = "sugestao";
                }
            }
        });

        btSalvarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // baseActivity.showProgressDialog();
                validate();

                if (!validate()) {
                    // baseActivity.hideProgressDialog();
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
                        tipo,0);

            }
        });


        return v;
    }


    private void addRegistro(String nome, double price, String description, String fotoPath, String dataIn,
                             String codigo, boolean isActive, String tipo, int favorito) {

        Produtos produtos = new Produtos(nome, price, description, photoUrl, dataIn, codigo, isActive, tipo,favorito);
        baseActivity.myRef.child("produtos").push().setValue(produtos);
        limparViews();
    }

    private void limparViews() {

        etValor.setText("");
        etNome.setText("");
        etDetalhes.setText("");
        ivFotoProduto.setImageResource(R.drawable.logo_container_original);
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
            imageName = imageFileName.toLowerCase();


            file = Uri.fromFile(new File(imageURI));

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


        final UploadTask uploadTask = baseActivity.myStorageRef.child(imageName).putFile(file);
        try {

            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){

                        Log.e(TAG, "onComplete: " + "SUCESSO NA GRAVAÇÃO NA NUVEM");

                    } else {

                        Log.e(TAG, "onComplete: " + " Erro uploading file" );
                    }
                }
            });

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri >>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task)
                        throws Exception {
                    baseActivity.myStorageRef.child(imageName).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                               photoUrl = task.getResult().toString();
                            }
                            Log.e(TAG, "onComplete: " + task.getException());
                        }
                    });
                    return baseActivity.myStorageRef.child(imageName).getDownloadUrl();
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "salvarFotoOnCloud: " + e.toString() );

        }

    }
}

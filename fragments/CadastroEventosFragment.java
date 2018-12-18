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

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.activities.BaseActivity;
import br.com.emanoel.oliveira.container.models.Eventos;
import br.com.emanoel.oliveira.container.models.Promocao;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastroEventosFragment extends Fragment {


    private ImageView ivFotoEvento;
    Button btBrowseEvento;
    EditText etNomeEvento;
    EditText etDescricaoEvento;
    EditText etDataEvento;
    Button btSalvarEvento;
    RadioGroup rgTipo;

    BaseActivity baseActivity;
    String dataEntrada, description;
    private long itemCount;
    private String imageURI, imageFileName, imageName, downloadUrl;
    private String codigoRef, tipo;
    private String photoUrl;
    private String tipoEvento;
    private boolean isActive = true;
    private String TAG = "CadastroEventosFragment: ";
    Uri file;
    private FirebaseAnalytics mFirebaseAnalytics;

    public CadastroEventosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cadastro_eventos, container, false);

        baseActivity = new BaseActivity();
        baseActivity.myStorageRef = FirebaseStorage.getInstance().getReference("container/eventos");
        getActivity().setTitle("Cadastro de eventos");

        ivFotoEvento = view.findViewById(R.id.ivFotoEvento);
        etNomeEvento = view.findViewById(R.id.etNomeEvento);
        etDataEvento = view.findViewById(R.id.etDataEvento);
        etDescricaoEvento = view.findViewById(R.id.etDescricaoEvento);
        btBrowseEvento = view.findViewById(R.id.btBrowseEvento);
        btSalvarEvento = view.findViewById(R.id.btSalvarEvento);
        rgTipo = view.findViewById(R.id.rgEventoPromocao);

        btBrowseEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checkItemCount(); retirei em 17/12
                Log.d("CHECK_ITEM_COUNT", "onDataChange: " + itemCount);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);


            }
        });

        rgTipo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.rbEvento){

                    tipoEvento = "evento";

                }else {
                    tipoEvento = "promoção";
                }

            }
        });
        btSalvarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // baseActivity.showProgressDialog();
                validate();

                if (!validate()) {
                    // baseActivity.hideProgressDialog();
                    return;
                }

                checkItemCount();

                if(tipoEvento.equals("evento")) {
                    codigoRef = "Evento-" + (itemCount + 1);
                }else {codigoRef = "Promocao-" + (itemCount + 1);}
                Log.d("SALVANDO_DADOS", "onClick: " + codigoRef);

                addRegistro(etNomeEvento.getText().toString().toUpperCase(),
                        description,
                        dataEntrada,
                        photoUrl,
                        isActive
                        );

            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
    private void addRegistro(String nome, String description, String dataIn, String fotoPath
                             , boolean isActive) {
        if(tipoEvento.equals("evento")) {
            Eventos eventos = new Eventos(nome, description, dataIn, fotoPath, isActive);
            baseActivity.myRef.child("eventos").push().setValue(eventos);
        }else{
            Promocao promocao = new Promocao(nome, description, dataIn, fotoPath, isActive);
            baseActivity.myRef.child("promocoes").push().setValue(promocao);
        }
        limparViews();
    }

    private void limparViews() {


        etNomeEvento.setText("");
        etDataEvento.setText("");
        ivFotoEvento.setImageResource(R.drawable.logo_container_original);
        rgTipo.clearCheck();
    }

    private boolean validate() {

        boolean valid = true;
        if (imageURI != null) {

            String nome = etNomeEvento.getText().toString().toUpperCase();
            if (TextUtils.isEmpty(nome)) {
                etNomeEvento.setError(getString(R.string.obrigatorio));
                valid = false;
            } else {
                etNomeEvento.setError(null);
            }

            dataEntrada = etDataEvento.getText().toString();
            if (TextUtils.isEmpty(dataEntrada)) {
                etDataEvento.setError(getString(R.string.obrigatorio));
                valid = false;
            } else {
                etDataEvento.setError(null);
            }

            if (tipoEvento.equals("")) {
                Toast.makeText(getContext(), "Selecione Eventos ou Promoções...", Toast.LENGTH_LONG).show();
                valid = false;

            }

            String valGG = etDescricaoEvento.getText().toString().toUpperCase();//valGG nada a ver...apenas reaproveitando codigo
            if (TextUtils.isEmpty(valGG)) {
                etDescricaoEvento.setError(getString(R.string.obrigatorio));
                valid = false;
            } else {
                etDescricaoEvento.setError(null);
                description = etDescricaoEvento.getText().toString().toUpperCase();
            }
        } else {

            Toast.makeText(getContext(), "Clic no botão BROWSE para escolher um flyer.", Toast.LENGTH_LONG).show();
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
                ivFotoEvento.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void checkItemCount() {


        if(tipoEvento.equals("evento")) {
            baseActivity.myRef.child("eventos").addValueEventListener(new ValueEventListener() {
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
        }else {
            baseActivity.myRef.child("promocoes").addValueEventListener(new ValueEventListener() {
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

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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

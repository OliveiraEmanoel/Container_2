package br.com.emanoel.oliveira.container.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.models.EncryptionHelper;
import br.com.emanoel.oliveira.container.models.Mesas;
import br.com.emanoel.oliveira.container.models.QRCodeHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class MesasFragment extends Fragment {

    ImageView ivQrCode;

    EditText etNroMesa;

    EditText etQdadeLugaresMesa;

    Button btGerarQrcode;

    Button btSalvarQrCode;
    String filename;
    Bitmap bitmap;

    /* criar o qrcode para cada mesa
     * com numero da mesa
     * nome do bar
     * tamanho da mesa?
     *
     * salvar qrcode*/


    public MesasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mesas, container, false);
        //to avoid fileuriexposeexcption
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        ivQrCode = v.findViewById(R.id.ivQrCode);
        etQdadeLugaresMesa = v.findViewById(R.id.etQdadeLugaresMesa);
        etNroMesa = v.findViewById(R.id.etNroMesa);
        btGerarQrcode = v.findViewById(R.id.btGerarQrcode);
        btSalvarQrCode = v.findViewById(R.id.btSalvarQrCode);
        btSalvarQrCode.setVisibility(View.GONE);

        btGerarQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm()) {
                    return;
                }
                //todo gerar qrcode

                Mesas mesa = new Mesas();

                mesa.setNomeBar("Container Bar Conceito");
                mesa.setNumeroMesa(Integer.parseInt(etNroMesa.getText().toString()));

                //UserObject(fullName = fullNameEditText.text.toString(), age = Integer.parseInt(ageEditText.text.toString()))
                String serializeString = new Gson().toJson(mesa);
                String encryptedString = EncryptionHelper.getInstance().encryptionString(serializeString).encryptMsg();

                setImageBitmap(encryptedString);


            }
        });

        btSalvarQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateForm()) {
                    btSalvarQrCode.setVisibility(View.GONE);//houve erro ou já foi compartilhado
                    return;
                }
                //determina o nome do arquivo
                filename = "mesa" + etNroMesa.getText().toString() + ".jpg";

                etNroMesa.setText("");

                //compartilha a imagem gravada utilizando o programa que o cliente quiser
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(salvaImagemLocal(filename)));
                startActivity(Intent.createChooser(share, "Compartilhar usando: "));

            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void setImageBitmap(String encryptedString) {
        bitmap = QRCodeHelper.newInstance(getContext()).setContent(encryptedString).setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                .setMargin(2).getQRCOde();

        ivQrCode.setImageBitmap(bitmap);

        btSalvarQrCode.setVisibility(View.VISIBLE);


    }

    //salva o bitmap no celular
    private File salvaImagemLocal(String filename) {

        //recupera a imagem exibida no imageview
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ivQrCode.getDrawable();
        Bitmap bit = bitmapDrawable.getBitmap();
        //escolhe onde será gravada a imagem
        File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //context.getExternalFilesDir(null);
        //define o nome do arquivo
        File file = new File(storageLoc, filename);
        //grava o arquivo
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bit.compress(Bitmap.CompressFormat.JPEG, 100, fos);
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
        return file;
    }

    private static void scanFile(Context context, Uri imageUri) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        context.sendBroadcast(scanIntent);

    }

    //validate inputs
    private boolean validateForm() {

        boolean valid = true;




        if (TextUtils.isEmpty(etNroMesa.getText().toString())) {

            etNroMesa.setError(getString(R.string.obrigatorio));

            valid = false;

        } else {

            etNroMesa.setError(null);

        }


        if (TextUtils.isEmpty(etQdadeLugaresMesa.getText().toString())) {

            etQdadeLugaresMesa.setError(getString(R.string.obrigatorio));

            valid = false;

        } else {

            etQdadeLugaresMesa.setError(null);

        }


        return valid;

    }
}

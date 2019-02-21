package br.com.emanoel.oliveira.container.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.Result;

import br.com.emanoel.oliveira.container.models.EncryptionHelper;
import br.com.emanoel.oliveira.container.models.Mesas;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQrCodeActivity extends BaseActivity implements ZXingScannerView.ResultHandler {


    private ZXingScannerView qrCodeScanner;

    private String HUAWEI = "huawei";
    private int MY_CAMERA_REQUEST_CODE = 6515;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qrCodeScanner = new ZXingScannerView(this);
        setContentView(qrCodeScanner);


    }

    @Override
    protected void onResume() {
        super.onResume();

        qrCodeScanner.setResultHandler(this);
        qrCodeScanner.startCamera();

    }

    /**
     * To check if user grant camera permission then called openCamera function.If not then show not granted
     * permission snack bar.
     *
     * @param requestCode  specify which request result came from operating system.
     * @param permissions  to specify which permission result is came.
     * @param grantResults to check if user granted the specific permission or not.
     */


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                openCamera();
            else if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(this, "Precisa de permissão para usar a camera", Toast.LENGTH_SHORT).show();
            // showCameraSnackBar();
        }
    }


    private void openCamera() {
        qrCodeScanner.startCamera();
        qrCodeScanner.setResultHandler(this);
    }

    /**
     * stop the qr code camera scanner when activity is in onPause state.
     */

    @Override
    public void onPause() {
        super.onPause();
        qrCodeScanner.stopCamera();
    }

    @Override
    public void handleResult(Result p0) {
        if (p0 != null) {
            try {
                String decryptedString = EncryptionHelper.getInstance().getDecryptionString(p0.toString());
                Mesas mesa;
                mesa = new Gson().fromJson(decryptedString, Mesas.class);
                // Do something with the result here
                // Prints scan results
                Log.e("result", p0.getText());
                // Prints the scan format (qrcode, pdf417 etc.)
                Log.e("result", p0.getBarcodeFormat().toString());
                //If you would like to resume scanning, call this method below:
                //mScannerView.resumeCameraPreview(this);

                Toast.makeText(getApplicationContext(), "Nro. Mesa =  " + mesa.getNumeroMesa(), Toast.LENGTH_SHORT).show();

                nroMesa = mesa.getNumeroMesa();
                mesaHasUser = true;
                Intent intent = new Intent();
                intent.putExtra("KEY_QR_CODE", p0.getText());
                intent.putExtra("mesa",mesa.getNumeroMesa());
                setResult(RESULT_OK, intent);
                onBackPressed();
                finish();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Erro lendo QrCode" + e.toString(), Toast.LENGTH_SHORT).show();
                onBackPressed();
                finish();
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}

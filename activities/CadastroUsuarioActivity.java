package br.com.emanoel.oliveira.container.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.models.UsuariosCadastrados;

public class CadastroUsuarioActivity extends BaseActivity {

    String TAG = "CADASTRO_USUARIO";

    private boolean internet;
    CoordinatorLayout coordinatorLayout;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    private EditText etNome, etEmail, etSenha, etConfirmaSenha;
    private Button btEntrar;
    String nome, email, password;
    UsuariosCadastrados usuariosCadastrados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        internet = false;
        internet = isConnected();
        if (!internet) {
            Snackbar.make(coordinatorLayout, "Verifique sua conexão de Internet!!", Snackbar.LENGTH_LONG)
                    .setAction("Sair", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.exit(0);
                        }
                    })
                    .show();

        }

        usuariosCadastrados = new UsuariosCadastrados();
        myCalendar = Calendar.getInstance();

        //setting up firebese auth
        try {
            mAuth = FirebaseAuth.getInstance();
        } catch (Exception error) {
            Log.e(TAG, "Setting instance: " + error);

        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        etNome = findViewById(R.id.etNomeUsuario);
        etSenha = findViewById(R.id.etSenhaUsuario);
        etEmail = findViewById(R.id.etEmailUsuario);
        etConfirmaSenha = findViewById(R.id.etSenhaUsuarioConfirma);
        btEntrar = findViewById(R.id.btSalvarNovoUser);


        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateForm()) {

                    return;

                }

                //cadastrar

                Log.d(TAG, nome + " " + password);

                cadastrar(nome, email, password);
            }
        });


    }


    private void cadastrar(final String nome, final String email, String password) {
        Log.d(TAG, "createUser:" + email);


        showProgressDialog();

        //Create account

        //todo check if user exist?

        try {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());


                            if (!task.isSuccessful()) {
//                                Toast.makeText(LoginActivity.this, R.string.auth_failed + task.getException(),
//                                        Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.getException());
                            } else {

                                Toast.makeText(CadastroUsuarioActivity.this, R.string.auth_success,
                                        Toast.LENGTH_SHORT).show();

                                salvarUsuario(nome, email);

                                entrar();

                            }
                            hideProgressDialog();
                            // ...
                        }
                    });
        } catch (Exception error) {

            myToastCurto(error.toString());
            finish();
        }
    }

    private void salvarUsuario(String nome, String email) {

        usuariosCadastrados.setNome(nome);
        usuariosCadastrados.setEmail(email);
        String dataHj = myCalendar.getTime().toString();
        usuariosCadastrados.setData(dataHj);

        try {
            myRef.child("usuarios").push().setValue(usuariosCadastrados);
        } catch (Exception e) {
            sendEmail2me("Suporte","CadastroUsuario-->cloud",e.toString(),dataHj,userID);
            Toast.makeText(CadastroUsuarioActivity.this, "Erro salvando usuário" + e.toString(),
                    Toast.LENGTH_LONG).show();
        }


    }

    //validate inputs from email and password
    private boolean validateForm() {

        boolean valid = true;


        email = etEmail.getText().toString();

        if (TextUtils.isEmpty(email) || (!isEmailValid(email))) {

            etEmail.setError(getString(R.string.obrigatorio));

            valid = false;

        } else {

            etEmail.setError(null);

        }

        nome = etNome.getText().toString();

        if (TextUtils.isEmpty(nome)) {

            etNome.setError(getString(R.string.obrigatorio));

            valid = false;

        } else {

            etNome.setError(null);

        }


        password = etSenha.getText().toString();

        if (TextUtils.isEmpty(password)) {

            etSenha.setError(getString(R.string.obrigatorio));

            valid = false;

        } else {

            etSenha.setError(null);

        }

        String confirmaPassword = etConfirmaSenha.getText().toString();

        if (!(password.equals(confirmaPassword))) {

            etConfirmaSenha.setError(getString(R.string.senha_não_bate));

            valid = false;

        } else if ((TextUtils.isEmpty(confirmaPassword))) {

            etConfirmaSenha.setError(getString(R.string.obrigatorio));

            valid = false;

        } else {

            etSenha.setError(null);

        }

        return valid;

    }

    private void entrar() {

        showProgressDialog();

        Log.d(TAG, "signIn:" + email);


        try {
            mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etSenha.getText().toString()).

                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());


                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                etSenha.setText("");//clear etsenha

                                Toast.makeText(CadastroUsuarioActivity.this, "Falha na autentificação de usuário!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                //habilitar administração/cadastro?
                                isUserAdmin(etEmail.getText().toString());

                                startActivity(new Intent(CadastroUsuarioActivity.this, MenuContainerActivity.class));
                                finish();
                            }

                            hideProgressDialog();
                            // ...
                        }
                    });
        } catch (Exception e) {

            myToastCurto("Erro de login" + TAG);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}

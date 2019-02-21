package br.com.emanoel.oliveira.container.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.models.AdmUsers;


public class LoginActivity extends BaseActivity {


    private String TAG = "AUTH_FIREBASE";
    String email;
    private String password;
    EditText etEmail;
    String currentUser;
    EditText etSenha;
    TextView tvClick;
    private Button btEntrar;
    private FirebaseAnalytics mFirebaseAnalytics;

    GlobalUserID globalUserID;

    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        } catch (Exception e) {
            Log.e(TAG, "onCreate: " + e.toString());
        }
        if (!isConnected()) {
            try {
                Snackbar.make(coordinatorLayout, "Verifique sua conexão de Internet!!", Snackbar.LENGTH_LONG)
                        .setAction("Sair", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.exit(0);
                            }
                        })
                        .show();
            } catch (Exception e) {

                myToastlongo("Verifique seu sinal de internet ");
                finish();

            }

        }

        //setting up firebese auth
        try {
            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser().getEmail();
        } catch (Exception error) {
            Log.e(TAG, "Setting instance: " + error);

        }

        try {
            //trying to check if user is admin and get this answer before isadmin method
            Query task = myRef.child("usuarios_admin").orderByChild("emailAdmUser").equalTo(mAuth.getCurrentUser().getEmail());

            task.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        AdmUsers users = userSnapshot.getValue(AdmUsers.class);

                        if (users.getActive()) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    userIsAdmin = true;

                                }
                            }, 2000);
                            userNome = users.getNomeAdmUser();

                            Log.e("LOGIN_IS_USER_ADMIN", "onDataChange: " + users.getNomeAdmUser() + " " + userIsAdmin);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }catch (Exception e){

            Toast.makeText(getApplicationContext()," Erro query task..." + e.toString(),Toast.LENGTH_SHORT).show();
        }

//        Thread readDataFromFirebase = new Thread() {//creating thread
//
//            public void run() {//run method of thread--here we set priority and all tasks we want that run in background
//
//                //setting priority
//                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
//                //trying to check if user is admin and get this answer before isadmin method
//                myRef.child("usuarios_admin").orderByChild("emailAdmUser").equalTo(mAuth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
//
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                            AdmUsers users = userSnapshot.getValue(AdmUsers.class);
//
//                            if (users.getActive()) {
//                                Handler handler = new Handler();
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        userIsAdmin = true;
//
//                                    }
//                                }, 2000);
//                                userNome = users.getNomeAdmUser();
//
//                                Log.e("LOGIN_IS_USER_ADMIN", "onDataChange: " + users.getNomeAdmUser() + " " + userIsAdmin);
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//
//                });
//
//            }
//
//
//        };
        //readDataFromFirebase.setDaemon(true);
        // readDataFromFirebase.start();

        //setting buttons

        btEntrar = findViewById(R.id.btLoginEntrar);

        //setting edittext
        etEmail = findViewById(R.id.etLoginEmail);
        etSenha = findViewById(R.id.etLoginSenha);
        tvClick = findViewById(R.id.tvLoginEsqueciSenha);


        globalUserID = (GlobalUserID) getApplication(); //getApplicationContext();

        //checking if user exists
        if (mAuth.getCurrentUser() != null) {

            globalUserID.setUsuarioId(mAuth.getCurrentUser().getUid());
            userID = globalUserID.getUsuarioId();

            etEmail.setText(mAuth.getCurrentUser().getEmail());
            userLogado = mAuth.getCurrentUser().getEmail();
            etEmail.setKeyListener(null);
            etSenha.requestFocus();
        } else {
            //se usuario não existe, cadastrar
            startActivity(new Intent(getApplicationContext(), CadastroUsuarioActivity.class));
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

        //listener to buttons

        tvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setting email and password
                email = etEmail.getText().toString();
                password = etSenha.getText().toString();
                if (mAuth.getCurrentUser() != null) {

                    mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail());
                    Toast.makeText(LoginActivity.this, "Senha enviada para seu email!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                entrar();

            }
        });


        etSenha.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 6) {

                    entrar();
                }

                return false;
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        nomeWifiAtual = getNomeAtual();//getting name WIFI

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        finish();
    }


    @Override
    protected void onPause() {

        super.onPause();
        finish();


    }


    private void entrar() {

        showProgressDialog();

        Log.d(TAG, "signIn:" + email);

        if (!validateForm()) {
            hideProgressDialog();
            return;

        }


        try {
            mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etSenha.getText().toString()).

                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());


                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                etSenha.setText("");//clear etsenha

                                Toast.makeText(LoginActivity.this, "Falha na autentificação de usuário!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                //habilitar administração/cadastro?
                                isUserAdmin(mAuth.getCurrentUser().getEmail());
                                Log.d(TAG, "user is admin= " + userIsAdmin + " " + mAuth.getCurrentUser().getEmail());
                                startActivity(new Intent(LoginActivity.this, MenuContainerActivity.class));
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


        password = etSenha.getText().toString();

        if (TextUtils.isEmpty(password)) {

            etSenha.setError(getString(R.string.obrigatorio));

            valid = false;

        } else {

            etSenha.setError(null);

        }


        return valid;

    }

}

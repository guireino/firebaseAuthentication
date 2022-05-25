package com.example.firebaseauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_Login, btn_cadastrar;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Login = (Button) findViewById(R.id.btn_login);
        btn_cadastrar = (Button) findViewById(R.id.btn_cadastrar);

        btn_Login.setOnClickListener(this);
        btn_cadastrar.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        stateAuth();
    }

    private void stateAuth(){
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    Toast.makeText(getBaseContext(), "Usuario " + user.getEmail() + " esta logado", Toast.LENGTH_SHORT).show();
                }else{

                }

            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_login:

                //FirebaseUser user = FirebaseAuth().getInstance().getCurrentUser();

                user = auth.getCurrentUser();

                if (user == null){
                    startActivity(new Intent(this, LoginEmailActivity.class));
                }else{
                    startActivity(new Intent(this, PrincipalActivity.class));
                }

               // startActivity(new Intent(this, LoginEmailActivity.class));
            break;

            case R.id.btn_cadastrar:

//                Intent intent = new Intent(this, CadastrarActivity.class);
//                startActivity(intent);

                startActivity(new Intent(this, CadastrarActivity.class));
            break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (authStateListener != null){
            auth.removeAuthStateListener(authStateListener);
        }
    }
}
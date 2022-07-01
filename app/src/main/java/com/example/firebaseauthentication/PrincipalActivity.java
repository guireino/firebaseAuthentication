package com.example.firebaseauthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class PrincipalActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_Deslogar;
    //private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Button btn_Deslogar = (Button) findViewById(R. id.button_Deslogar);
        btn_Deslogar.setOnClickListener(this);

        //auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){

            case R.id.button_Deslogar:

                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                //auth.signOut();
                //FirebaseAuth.getInstance().signOut();

                mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                mGoogleSignInClient.signOut();
                finish();

                startActivity(new Intent(getBaseContext(), MainActivity.class));

            break;
        }
    }
}
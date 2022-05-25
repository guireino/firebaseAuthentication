package com.example.firebaseauthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class PrincipalActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_Deslogar;
    //private FirebaseAuth auth;

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
                //auth.signOut();
                FirebaseAuth.getInstance().signOut();
                finish();
            break;
        }
    }
}
package com.example.firebaseauthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_Login, btn_cadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Login = (Button) findViewById(R.id.btn_login);
        btn_cadastrar = (Button) findViewById(R.id.btn_cadastrar);

        btn_Login.setOnClickListener(this);
        btn_cadastrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_login:
                startActivity(new Intent(this, LoginEmailActivity.class));
            break;

            case R.id.btn_cadastrar:

//                Intent intent = new Intent(this, CadastrarActivity.class);
//                startActivity(intent);

                startActivity(new Intent(this, CadastrarActivity.class));
            break;
        }
    }
}
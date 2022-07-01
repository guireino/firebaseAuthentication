package com.example.firebaseauthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AberturaActivity extends AppCompatActivity implements Runnable {

    private ProgressBar progressBar;
    private Thread thread;
    private Handler handler;
    private int second;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abertura);

        auth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        handler = new Handler();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {

        second = 1;

        try {

            while(second <= 100){ // criando um tempo para mostra tela logo do app
                Thread.sleep(50);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        second++;
                        progressBar.setProgress(second);
                    }
                });
            }

            FirebaseUser user = auth.getCurrentUser();

            if(user == null){  // verificar se usuario esta logando para entra tireto da pagina principal
                finish();
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }else{
                finish();
                startActivity(new Intent(getBaseContext(), PrincipalActivity.class));
            }

        }catch (InterruptedException e){

        }
    }
}
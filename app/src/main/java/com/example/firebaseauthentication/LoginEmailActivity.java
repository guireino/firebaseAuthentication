package com.example.firebaseauthentication;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginEmailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_Email, editText_Senha;
    private Button btn_Login, btn_RecuperarSenha;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_email);

        editText_Email = (EditText) findViewById(R.id.editText_EmailLogin);
        editText_Senha = (EditText) findViewById(R.id.editText_SenhaLogin);

        btn_Login = (Button) findViewById(R.id.btn_login);
        btn_RecuperarSenha = (Button) findViewById(R.id.btn_RecuperarSenha);

        btn_Login.setOnClickListener(this);
        btn_RecuperarSenha.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_login:
                loginEmail();
            break;

            case R.id.btn_RecuperarSenha:

        }
    }

    private void loginEmail(){

        String email = editText_Email.getText().toString().trim();
        String senha = editText_Senha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()){
            Toast.makeText(getBaseContext(), "Insire os campos obrigatorios", Toast.LENGTH_SHORT).show();
        }else{
            if (verificarInternet()){
                confirmarLoginEmail(email, senha);
            }else{
                Toast.makeText(getBaseContext(), "Erro - Verifique se sua wifi ou 3G esta funcionando", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean verificarInternet(){

        ConnectivityManager conexao = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = conexao.getActiveNetworkInfo();

        if(info != null && info.isConnected()){
            return true;
        }else{
            return false;
        }
    }

    private void confirmarLoginEmail(String email, String senha){

        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    startActivity(new Intent(getBaseContext(), PrincipalActivity.class));
                    Toast.makeText(getBaseContext(), "Usuario Logado com Sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    String resposta = task.getException().toString();
                    optionsErro(resposta);
                    //Toast.makeText(getBaseContext(), "Erro ao Logar usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void optionsErro(String resposta){

        if(resposta.contains("least 6 characters")){
            Toast.makeText(getBaseContext(), "Digite uma senha maior que 6 characters", Toast.LENGTH_LONG).show();
        }else if(resposta.contains("address is badly")){
            Toast.makeText(getBaseContext(), "E-mail inválido", Toast.LENGTH_LONG).show();
        }else if(resposta.contains("interrupted connection")){
            Toast.makeText(getBaseContext(), "Sem conexão com o Firebase", Toast.LENGTH_LONG).show();
        }else if(resposta.contains("password is invalid")){
            Toast.makeText(getBaseContext(), "senha invalida", Toast.LENGTH_LONG).show();
        }else if(resposta.contains("There is no user")){
            Toast.makeText(getBaseContext(), "Este e-mail não esta cadastrado", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getBaseContext(), resposta, Toast.LENGTH_LONG).show();
        }
    }

}
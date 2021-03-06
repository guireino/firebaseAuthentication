package com.example.firebaseauthentication;

import static androidx.core.content.ContextCompat.getSystemService;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
                recuperarSenha();
            break;
        }
    }

    private void recuperarSenha(){

        String email = editText_Email.getText().toString().trim();

        if (email.isEmpty()){
            Toast.makeText(getBaseContext(), "Insire pelo menos seu e-mail para poder Recuperar sua senha", Toast.LENGTH_SHORT).show();
        }else{
            enviarEmail(email);
        }
    }

    private void enviarEmail(String email) {

        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getBaseContext(), "Enviamos uma MSG para o seu email com um link para voce redefinir a sua senha",
                        Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String erro = e.toString();
                Util.optionsErro(getBaseContext(), erro);
                // Toast.makeText(getBaseContext(), "Erro ao enviar email", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginEmail(){

        String email = editText_Email.getText().toString().trim();
        String senha = editText_Senha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()){
            Toast.makeText(getBaseContext(), "Insire os campos obrigatorios", Toast.LENGTH_SHORT).show();
        }else{
            if (Util.verificarInternet(this)){
                ConnectivityManager conexao = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                confirmarLoginEmail(email, senha);
            }else{
                Toast.makeText(getBaseContext(), "Erro - Verifique se sua wifi ou 3G esta funcionando", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void confirmarLoginEmail(String email, String senha){

        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                FirebaseUser user = auth.getCurrentUser();

                if (task.isSuccessful()){

                    if (user.isEmailVerified()){
                        startActivity(new Intent(getBaseContext(), PrincipalActivity.class));
                        Toast.makeText(getBaseContext(), "Usuario Logado com Sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(getBaseContext(), "Seu E-mail ainda nao foi confirmado", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    String resposta = task.getException().toString();
                    Util.optionsErro(getBaseContext(), resposta);
                    //Toast.makeText(getBaseContext(), "Erro ao Logar usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
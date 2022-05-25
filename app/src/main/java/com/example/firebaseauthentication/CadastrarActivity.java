package com.example.firebaseauthentication;

import android.net.ConnectivityManager;
import android.net.Network;
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

public class CadastrarActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_Email, editText_Senha, editText_SenhaRepetir;
    private Button btn_Cadastrar, btn_Cancelar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        editText_Email = (EditText) findViewById(R.id.editText_EmailCadastro);
        editText_Senha = (EditText) findViewById(R.id.editText_SenhaCadastro);
        editText_SenhaRepetir = (EditText) findViewById(R.id.editText_SenhaRepetirCadastro);

        btn_Cadastrar = (Button) findViewById(R.id.btn_CadastrarUsuario);
        btn_Cancelar = (Button) findViewById(R.id.btn_Cancelas);

        btn_Cadastrar.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_CadastrarUsuario:
                cadastrar();
            break;
        }
    }

    private void cadastrar() {

        String email = editText_Email.getText().toString().trim();
        String senha = editText_Senha.getText().toString().trim();
        String confirmaSenha = editText_SenhaRepetir.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty() || confirmaSenha.isEmpty()){
            Toast.makeText(getBaseContext(), "Erro - Preencha os Campos", Toast.LENGTH_LONG).show();
        }else{

            if(senha.contentEquals(confirmaSenha)){

                if(Util.verificarInternet(this)){
                    criarUsuario(email, senha);
                }else{
                    Toast.makeText(getBaseContext(), "Erro - Verifique se sua wifi ou 3G esta funcionando", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getBaseContext(), "Erro - Senhas Diferentes", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void criarUsuario(String email, String senha){

        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                ///boolean resultado = task.isSuccessful();

                if(task.isSuccessful()){
                    Toast.makeText(getBaseContext(), "Cadastro efetuado com Sucesso", Toast.LENGTH_LONG).show();
                }else{
                    String resposta = task.getException().toString();
                    //Toast.makeText(getBaseContext(), resposta, Toast.LENGTH_LONG).show();
                    Util.optionsErro(getBaseContext(), resposta);
                }
            }
        });
    }
}
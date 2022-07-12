package com.example.firebaseauthentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    private Button btn_Login, btn_cadastrar;

    private CardView cardView_LoginGoogle, cardView_LoginFacebook, cardView_LoginAnonimo,
            cardView_LoginCadastrar, cardView_LoginEmail;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private GoogleSignInClient mGoogleSignInClient;

    private BeginSignInRequest signInRequest;
//    private SignInClient oneTapClient;

    private CallbackManager callbackManager;

    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleSignInOptions googleSignInOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardView_LoginEmail = (CardView) findViewById(R.id.cardView_LoginEmail);
        cardView_LoginCadastrar = (CardView) findViewById(R.id.cardView_LoginCadastrar);
        cardView_LoginFacebook = (CardView) findViewById(R.id.cardView_LoginFacebook);
        cardView_LoginGoogle = (CardView) findViewById(R.id.cardView_LoginGoogle);
        cardView_LoginAnonimo = (CardView) findViewById(R.id.cardView_LoginAnonimo);

        cardView_LoginEmail.setOnClickListener(this);
        cardView_LoginCadastrar.setOnClickListener(this);
        cardView_LoginFacebook.setOnClickListener(this);
        cardView_LoginGoogle.setOnClickListener(this);
        cardView_LoginAnonimo.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        servicesAuth();
        servicesGoogle();
        servicesFacebook();
    }

    //--------------------------- SERVICOS LOGIN ---------------------------

    private void servicesFacebook(){

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //AccessToken count loginResult.getAccessToken();
                addfirebaseAuthWithFacebook(loginResult.getAccessToken());
                startActivity(new Intent(getBaseContext(), PrincipalActivity.class));
            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "Cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {

                String resultado = error.getMessage();

                Util.optionsErro(getBaseContext(), resultado);

                //Toast.makeText(getBaseContext(), "Erro ao fazer login com Facebook", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void servicesGoogle() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void servicesAuth(){
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null && user.isEmailVerified()){
                    Toast.makeText(getBaseContext(), "Usuario " + user.getEmail() + " esta logado", Toast.LENGTH_SHORT).show();
                }else{

                }

            }
        };
    }

    //-------------------------------- TRATAMENTO DE CLICKS --------------------------------

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.cardView_LoginFacebook:
                signInFacebook();
            break;

            case R.id.cardView_LoginGoogle:
                signInGoogle();
            break;

            case R.id.cardView_LoginEmail:

                //FirebaseUser user = FirebaseAuth().getInstance().getCurrentUser();
                signInEmail();
            break;

            case R.id.cardView_LoginCadastrar:

//                Intent intent = new Intent(this, CadastrarActivity.class);
//                startActivity(intent);

                startActivity(new Intent(this, CadastrarActivity.class));
            break;

            case R.id.cardView_LoginAnonimo:
                signInAnonimo();
            break;
        }
    }

    //-------------------------------- METODOS DE LOGIN --------------------------------

    private void signInFacebook(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    private void signInGoogle(){

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account == null){
            Intent intent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(intent, 555);
        }else{
            // ja existe alem conectado pelo google
            Toast.makeText(getBaseContext(), "Ja logado", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getBaseContext(), PrincipalActivity.class));
            // mGoogleSignInClient.signOut();
        }
    }

    private void signInEmail(){

        user = auth.getCurrentUser();

        if(user != null && user.isEmailVerified()){
            finish();
            startActivity(new Intent(this, PrincipalActivity.class));
        }else{
            startActivity(new Intent(this, LoginEmailActivity.class));
        }

//        if (user == null || !user.isEmailVerified()){
//            finish();
//            startActivity(new Intent(this, LoginEmailActivity.class));
//        }else{
//            finish();
//           startActivity(new Intent(this, PrincipalActivity.class));
//        }

    }

    private void signInAnonimo(){
        accessAnonymousAccountFirebase();
    }

    //-------------------------------- AUTENTICACAO NO FIREBASE --------------------------------

    private void addfirebaseAuthWithFacebook(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getBaseContext(),PrincipalActivity.class));
                        } else {

                            String resultado = task.getException().toString();

                            Util.optionsErro(getBaseContext(), resultado);

                            //Toast.makeText(getBaseContext(),"Erro ao Criar Conta Facebook",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void addfirebaseAuthWithGoogle(GoogleSignInAccount acct){

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getBaseContext(),PrincipalActivity.class));
                        } else {
                            Toast.makeText(getBaseContext(),"Erro ao Criar Conta Google",Toast.LENGTH_LONG).show();
                            String resultado = task.getException().toString();
                            Util.optionsErro(getBaseContext(), resultado);
                        }
                    }
                });
    }

    private void accessAnonymousAccountFirebase(){

        auth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            finish();
                            startActivity(new Intent(getBaseContext(),PrincipalActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            //Toast.makeText(getBaseContext(),"Erro ao Criar Conta Anonima",Toast.LENGTH_LONG).show();

                            String resultado = task.getException().toString();
                            Util.optionsErro(getBaseContext(), resultado);
                        }
                    }
                });
    }

    //-------------------------------- METADOS DA ACTIVITY --------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 555){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                addfirebaseAuthWithGoogle(account);

                //startActivity(new Intent(getBaseContext(), PrincipalActivity.class));
            } catch (ApiException e){
                // String res = task.getException().toString();
                // Toast.makeText(getBaseContext(), "Erro ao Logar com conta do Google", Toast.LENGTH_LONG).show();
                // Toast.makeText(getBaseContext(), res, Toast.LENGTH_LONG).show();

                String resultado = e.getMessage();
                Util.optionsErro(getBaseContext(), resultado);
            }
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
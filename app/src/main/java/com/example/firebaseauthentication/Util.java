package com.example.firebaseauthentication;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Util {

    public static boolean verificarInternet(Context context) {

        ConnectivityManager conexao = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = conexao.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static void optionsErro(Context context, String resposta) {

        if (resposta.contains("least 6 characters")) {
            Toast.makeText(context, "Digite uma senha maior que 6 characters", Toast.LENGTH_LONG).show();
        }else if (resposta.contains("address is badly")) {
            Toast.makeText(context, "E-mail inválido", Toast.LENGTH_LONG).show();
        }else if (resposta.contains("interrupted connection")) {
            Toast.makeText(context, "Sem conexão com o Firebase", Toast.LENGTH_LONG).show();
        }else if (resposta.contains("password is invalid")) {
            Toast.makeText(context, "senha invalida", Toast.LENGTH_LONG).show();
        }else if (resposta.contains("There is no user")) {
            Toast.makeText(context, "Este e-mail não esta cadastrado", Toast.LENGTH_LONG).show();
        }else if (resposta.contains("address is already")) {
            Toast.makeText(context, "E-mail ja existe cadastrado", Toast.LENGTH_LONG).show();
        }else if (resposta.contains("INVALID_EMAIL")) {
            Toast.makeText(context, "E-mail inválido", Toast.LENGTH_LONG).show();
        }else if (resposta.contains("EMAIL_NOT_FOUND")) {
            Toast.makeText(context, "E-mail não esta cadastrado ainda", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context, resposta, Toast.LENGTH_LONG).show();
        }
    }
}
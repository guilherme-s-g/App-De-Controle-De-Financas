package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity
{
    private TextInputEditText editEmailLogin, editSenhaLogin;
    private Button buttonEntrar;
    private String emailLoginDigitado, senhaLoginDigitada;
    private Usuario usuario;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        autenticacao = FirebaseAuth.getInstance();
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            editEmailLogin = findViewById(R.id.editEmailLogin);
            editSenhaLogin = findViewById(R.id.editSenhaLogin);
            buttonEntrar = findViewById(R.id.buttonEntrar);

            buttonEntrar.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    emailLoginDigitado = editEmailLogin.getText().toString();
                    senhaLoginDigitada = editSenhaLogin.getText().toString();
                    if (!emailLoginDigitado.isEmpty())
                    {
                        if (!senhaLoginDigitada.isEmpty())
                        {
                            usuario = new Usuario();
                            usuario.setEmail(emailLoginDigitado);
                            usuario.setSenha(senhaLoginDigitada);
                            autenticarUsuario();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Prencha o campo senha!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Prencha o campo email!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void autenticarUsuario()
    {
        try
        {

            autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        abrirTelaPrincipal();
                        Toast.makeText(getApplicationContext(), "Usuário autenticado", Toast.LENGTH_SHORT).show();
                        Log.i("CreateUser", "Sucesso ao autenticar o usuário");
                    }
                    else
                    {
                        String execao = "";
                        try
                        {
                            throw task.getException();
                        }
                        catch (FirebaseAuthInvalidUserException e)
                        {
                            execao = "Usário não está cadastrado";
                        }
                        catch (FirebaseAuthInvalidCredentialsException e)
                        {
                            execao = "E-mail e senha não correspondem a um usuário cadastrado";
                        }
                        catch (Exception e)
                        {
                            execao = "Erro ao cadastrar usuário" + e.getMessage();
                            e.printStackTrace();
                        }
                        Toast.makeText(LoginActivity.this, execao, Toast.LENGTH_SHORT).show();
                        Log.i("CreateUser", "Erro ao cirar o usuário");
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void abrirTelaPrincipal()
    {
        Intent intent = new Intent(getApplicationContext(),PrincipalActivity.class);
        startActivity(intent);
        finish();
    }
}
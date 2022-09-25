package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.ConfiguracaoFirebase;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity
{
    private TextInputEditText editNomeCadastrar, editEmailCadastrar, editSenhaCadastrar;
    private Button buttonCadastrar;
    private String nomeDigitado,emailDigitado,senhaDigitada;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cadastro);
            editNomeCadastrar = findViewById(R.id.editNomeCadastrar);
            editEmailCadastrar = findViewById(R.id.editEmailCadastrar);
            editSenhaCadastrar = findViewById(R.id.editSenhaCadastrar);
            buttonCadastrar = findViewById(R.id.buttonEntrar);

            buttonCadastrar.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    nomeDigitado = editNomeCadastrar.getText().toString();
                    emailDigitado = editEmailCadastrar.getText().toString();
                    senhaDigitada = editSenhaCadastrar.getText().toString();

                    //Validar se os campos foram prrenchidos
                    if (!nomeDigitado.isEmpty())
                    {
                        if (!emailDigitado.isEmpty())
                        {
                            if (!senhaDigitada.isEmpty())
                            {
                                usuario = new Usuario();
                                usuario.setNome(nomeDigitado);
                                usuario.setEmail(emailDigitado);
                                usuario.setSenha(senhaDigitada);
                                cadastrarUsuario();
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
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Prencha o campo nome!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void cadastrarUsuario ()
    {  try
        {
            autenticacao = FirebaseAuth.getInstance();
            autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
            autenticacao.createUserWithEmailAndPassword(usuario.getEmail(),usuario.getSenha())
                    .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {   String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                                usuario.setIdUsuario(idUsuario);
                                usuario.salvar();
                                Toast.makeText(getApplicationContext(), "Usuário criado com sucesso", Toast.LENGTH_SHORT).show();
                                Log.i("CreateUser", "Sucesso ao cirar o usuário");
                                finish();
                            }
                            else
                            {
                                String execao = "";
                                try
                                {
                                    throw task.getException();
                                }
                                catch (FirebaseAuthWeakPasswordException e)
                                {
                                    execao = "Digite uma senha mais forte!";
                                }
                                catch (FirebaseAuthInvalidCredentialsException e)
                                {
                                    execao = "Digite um email valido!";
                                }
                                catch (FirebaseAuthUserCollisionException e)
                                {
                                    execao = "Está conta já foi cadastrada";
                                }
                                catch (Exception e)
                                {
                                    execao = "Erro ao cadastrar usuário";
                                    e.printStackTrace();
                                }
                                Toast.makeText(getApplicationContext(), execao, Toast.LENGTH_SHORT).show();
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
}
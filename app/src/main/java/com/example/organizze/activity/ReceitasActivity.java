package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.ConfiguracaoFirebase;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.helper.DateCustom;
import com.example.organizze.model.Movimentacao;
import com.example.organizze.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitasActivity extends AppCompatActivity
{
    private TextInputEditText editDataReceitas, editCategoriaReceitas, editDescricaoReceitas;
    private EditText editValorReceitas;
    private Movimentacao movimentacao;
    private String valorDigitado,dataDigitada, categoriaDigitada, descricaoDigitada;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double receitaTotal;
    private Double receitaAtualizada;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        editDataReceitas = findViewById(R.id.editDataReceitas);
        editCategoriaReceitas = findViewById(R.id.editCategoriaReceitas);
        editDescricaoReceitas = findViewById(R.id.editDescricaoReceitas);

        editValorReceitas = findViewById(R.id.editValorReceitas);

        //Preencher data com o dia atual
        editDataReceitas.setText(DateCustom.dataAtual());
        recuperarDespesaTotal();
    }

    public void salvarReceita(View view)
    {
        if (validarCamposDespesa())
        {
            movimentacao = new Movimentacao();
            String data = editDataReceitas.getText().toString();
            Double valorRecuperado = Double.parseDouble(editValorReceitas.getText().toString());
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(editCategoriaReceitas.getText().toString());
            movimentacao.setDescricao(editDescricaoReceitas.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("r");


            receitaAtualizada = receitaTotal + valorRecuperado;
            atualizarReceita(receitaAtualizada);
            movimentacao.salvarMovimentacao(data);
            Toast.makeText(getApplicationContext(), "Receita salva com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    public Boolean validarCamposDespesa()
    {
        valorDigitado = editValorReceitas.getText().toString();
        dataDigitada = editDataReceitas.getText().toString();
        categoriaDigitada = editCategoriaReceitas.getText().toString();
        descricaoDigitada = editDescricaoReceitas.getText().toString();

        if (!valorDigitado.isEmpty())
        {
            if (!dataDigitada.isEmpty())
            {
                if (!categoriaDigitada.isEmpty())
                {
                    if (!descricaoDigitada.isEmpty())
                    {
                        return true;
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Prencha o campo descrição!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Prencha o campo categoria!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Prencha o campo data!", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Prencha o campo valor!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void recuperarDespesaTotal()
    {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Usuario usuario = snapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void atualizarReceita(Double receita)
    {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(receita);
    }
}
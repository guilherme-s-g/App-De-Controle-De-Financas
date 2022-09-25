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

public class DespesasActivity extends AppCompatActivity
{
    private TextInputEditText editDataDespesas, editCategoriaDespesas, editDescricaoDespesas;
    private EditText editValorDespesas;
    private Movimentacao movimentacao;
    private String valorDigitado,dataDigitada, categoriaDigitada, descricaoDigitada;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double despesaTotal;
    private Double despesaAtualizada;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        editDataDespesas = findViewById(R.id.editDataDespesas);
        editCategoriaDespesas = findViewById(R.id.editCategoriaDespesas);
        editDescricaoDespesas = findViewById(R.id.editDescricaoDespesas);

        editValorDespesas = findViewById(R.id.editValorDespesas);

        //Preencher data com o dia atual
        editDataDespesas.setText(DateCustom.dataAtual());
        recuperarDespesaTotal();

    }

    public void salvarDespesa(View view)
    {
        if (validarCamposDespesa())
        {
            movimentacao = new Movimentacao();
            String data = editDataDespesas.getText().toString();
            Double valorRecuperado = Double.parseDouble(editValorDespesas.getText().toString());
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(editCategoriaDespesas.getText().toString());
            movimentacao.setDescricao(editDescricaoDespesas.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("d");


            despesaAtualizada = despesaTotal + valorRecuperado;
            atualizarDespesa(despesaAtualizada);
            movimentacao.salvarMovimentacao(data);
            Toast.makeText(getApplicationContext(), "Despesa salva com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    public Boolean validarCamposDespesa()
    {
        valorDigitado = editValorDespesas.getText().toString();
        dataDigitada = editDataDespesas.getText().toString();
        categoriaDigitada = editCategoriaDespesas.getText().toString();
        descricaoDigitada = editDescricaoDespesas.getText().toString();

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
                despesaTotal = usuario.getDespesaTotal();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void atualizarDespesa(Double despesa)
    {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("despesaTotal").setValue(despesa);
    }
}
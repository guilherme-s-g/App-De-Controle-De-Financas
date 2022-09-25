package com.example.organizze.adapter;

import android.content.Context;
import android.content.pm.ModuleInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizze.R;
import com.example.organizze.model.Movimentacao;

import java.util.List;

public class AdapterMovimentacoes extends RecyclerView.Adapter<AdapterMovimentacoes.MyViewHolder>
{
    Context context;
    List<Movimentacao> movimentacoes;

    public AdapterMovimentacoes(List<Movimentacao> movimentacoes, Context context)
    {
        this.movimentacoes = movimentacoes;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterMovimentacoes.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.movimentacoes_detalhes, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMovimentacoes.MyViewHolder holder, int position)
    {
        Movimentacao movimentacao = movimentacoes.get(position);

        holder.textDescricaoMovimentacao.setText(movimentacao.getDescricao());
        holder.textValorMovimentacao.setText("R$" + String.valueOf(movimentacao.getValor()));
        holder.textCategoriaMovimentacao.setText(movimentacao.getCategoria());
        holder.textValorMovimentacao.setTextColor(context.getResources().getColor(R.color.cor_enfase));

        if (movimentacao.getTipo().equals("d"))
        {
            holder.textValorMovimentacao.setTextColor(context.getResources().getColor(R.color.cor_secundaria));
            holder.textValorMovimentacao.setText("R$" + "-" + movimentacao.getValor());
        }

    }

    @Override
    public int getItemCount()
    {
        return movimentacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textDescricaoMovimentacao, textCategoriaMovimentacao, textValorMovimentacao;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textDescricaoMovimentacao = itemView.findViewById(R.id.textDescricaoMovimentacao);
            textCategoriaMovimentacao = itemView.findViewById(R.id.textCategoriaMovimentacao);
            textValorMovimentacao = itemView.findViewById(R.id.textValorMovimentacao);

        }
    }
}

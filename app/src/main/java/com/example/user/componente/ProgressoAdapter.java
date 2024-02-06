package com.example.user.componente;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.classesDominio.NivelConteudo;
import com.example.user.projetoquimica.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ProgressoAdapter extends RecyclerView.Adapter<ProgressoAdapter.MyViewHolder> {
    private ArrayList<NivelConteudo> listaProgresso;
    private ProgressoOnClickListener progressoOnClickListener;
    private Context context;


    public ProgressoAdapter(ArrayList<NivelConteudo> listaProgresso, ProgressoOnClickListener progressoOnClickListener) {
        this.listaProgresso = listaProgresso;
        this.progressoOnClickListener = progressoOnClickListener;
        this.context = context;
    }

    @Override
    public ProgressoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row_progresso, parent, false);

        return new ProgressoAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProgressoAdapter.MyViewHolder holder, final int position) {
        NivelConteudo nivelConteudo = listaProgresso.get(position);
        holder.tvProgressoNomeConteudo.setText(nivelConteudo.getConteudo().getNomeConteudo());
        holder.tvTentativas.setText("Numero de Tentativas Restante: "+ String.valueOf(nivelConteudo.getTentativas()));

        holder.tvMostrarQuantidaVidas.setText(nivelConteudo.getVidas() + "x");
        holder.imAtomoVidas.setImageDrawable(nivelConteudo.getImagemVidasConteudo(this.context));


        Log.d("Tentativas","Tentativas Adapter: " + nivelConteudo.getTentativas());

        // clique no item do cliente
        if (progressoOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressoOnClickListener.onClickProgresso(holder.itemView, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return listaProgresso.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTentativas, tvProgressoNomeConteudo, tvMostrarQuantidaVidas;
        ImageView imAtomoVidas;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTentativas = (TextView) itemView.findViewById(R.id.tvProgressoTentativas);
            tvProgressoNomeConteudo = (TextView) itemView.findViewById(R.id.tvProgressoNomeConteudo);
//
//            tvMostrarQuantidaVidas = (TextView) itemView.findViewById(R.id.tvMostrarQuantidaVidas);
//            imAtomoVidas = (ImageView) itemView.findViewById(R.id.imAtomoVidas);
        }
    }

    public interface ProgressoOnClickListener {
        public void onClickProgresso(View view, int position);
    }
}


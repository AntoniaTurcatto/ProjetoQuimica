package com.example.user.componente;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.user.classesDominio.Pergunta;
import com.example.user.projetoquimica.R;

import java.util.ArrayList;


public class RelatorioPerguntaAdapter extends RecyclerView.Adapter<RelatorioPerguntaAdapter.MyViewHolder> {
    private ArrayList<Pergunta> listaPerguntas;
    private PerguntaOnClickListener perguntaOnClickListener;
    private Context context;


    public RelatorioPerguntaAdapter(ArrayList<Pergunta> listaPerguntas, PerguntaOnClickListener perguntaOnClickListener, Context context) {
        this.listaPerguntas = listaPerguntas;
        this.perguntaOnClickListener = perguntaOnClickListener;
        this.context = context;
    }

    @Override
    public RelatorioPerguntaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row_pergunta, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RelatorioPerguntaAdapter.MyViewHolder holder, final int position) {
        Pergunta perg = listaPerguntas.get(position);
        holder.tvRelatorioPergunta.setText("Questão: "+ position+1);
        if(perg.getEnunciado().length()<= 10){
            holder.tvRelatorioEnunciadoPergunta.setText("Enunciado: "+perg.getEnunciado());
        }else{
            String novoEnunciado = perg.getEnunciado().substring(0,10)+"...";
            holder.tvRelatorioEnunciadoPergunta.setText(novoEnunciado);
        }


        holder.tvRelatorioConteudoPergunta.setText("Conteudo: "+perg.getConteudo().getNomeConteudo());
        String respostaCorreta = String.valueOf(perg.getAlternativaCorreta());
        String respostaEscolhida = String.valueOf(perg.getOpcaoEscolhida());
        holder.tvRelatorioRespostaCorreta.setText("Reposta Correta: "+ respostaCorreta);
        holder.tvRelatorioRespostaEscolhida.setText("Resposta Escolhida: "+ respostaEscolhida);
        if (respostaCorreta.equals(respostaEscolhida)){
            holder.ivRelatorioResultado.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_acerto));
            holder.ivRelatorioResultado.setColorFilter(context.getResources().getColor(R.color.verde));
            holder.tvRelatorioRespostaEscolhida.setTextColor(context.getResources().getColor(R.color.verde));
        } else{
            holder.ivRelatorioResultado.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_erro));
            holder.ivRelatorioResultado.setColorFilter(context.getResources().getColor(R.color.vermelho));
            holder.tvRelatorioRespostaEscolhida.setTextColor(context.getResources().getColor(R.color.vermelho));
        }
        /* CUIDADO: .setText() precisa sempre de String. Se for outro tipo de dado, deve ser feita a conversão com o String.valueOf() */

        // clique no item do cliente
        if (perguntaOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    perguntaOnClickListener.onClickPergunta(holder.itemView, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return listaPerguntas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvRelatorioEnunciadoPergunta,tvRelatorioPergunta, tvRelatorioConteudoPergunta, tvRelatorioRespostaCorreta, tvRelatorioRespostaEscolhida;
        ImageView ivRelatorioResultado;

        public MyViewHolder(View itemView) {
            super(itemView);
            //TextView
            tvRelatorioEnunciadoPergunta = itemView.findViewById(R.id.tvRelatorioEnunciadoPergunta);
            tvRelatorioPergunta = itemView.findViewById(R.id.tvRelatorioPergunta);
            tvRelatorioConteudoPergunta = itemView.findViewById(R.id.tvRelatorioConteudoPergunta);
            tvRelatorioRespostaCorreta = itemView.findViewById(R.id.tvRelatorioRespostaCorreta);
            tvRelatorioRespostaEscolhida = itemView.findViewById(R.id.tvRelatorioRepostaEscolhida);
            //ImageView
            ivRelatorioResultado = itemView.findViewById(R.id.ivRelatorioResultado);
        }
    }

    public interface PerguntaOnClickListener {
        void onClickPergunta(View view, int position);
    }
}



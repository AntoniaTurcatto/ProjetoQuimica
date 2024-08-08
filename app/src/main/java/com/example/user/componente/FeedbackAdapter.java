package com.example.user.componente;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.banco.InformacoesApp;
import com.example.user.classesDominio.ClasseIntermediaria;
import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.Feedback;
import com.example.user.classesDominio.NivelConteudo;
import com.example.user.classesDominio.Usuario;
import com.example.user.projetoquimica.R;

import java.util.ArrayList;
import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder> {

    private List<Feedback> listaFeedback;
    private List<NivelConteudo> listaNivelConteudo;
    private ArrayList<Conteudo> listaConteudos;
    private FeedbackOnClickListener feedbackOnClickListener;
    private ClasseIntermediaria classeIntermediaria;
    private InformacoesApp app;
    private List<String> listaExplicacoes;
    private Usuario usuario;

    public FeedbackAdapter(Usuario usuario, List<Feedback> listaFeedback, ArrayList<Conteudo> listaConteudo, FeedbackOnClickListener feedbackOnClickListener, Context context) {
        this.listaFeedback = listaFeedback;
        this.feedbackOnClickListener = feedbackOnClickListener;
        classeIntermediaria = new ClasseIntermediaria(context);
        //LOGICA DA EXPLICAÇÃO:======================================================================================
        //coisas necessárias:
        //      ranking atual .................................................................................(int para identificar)
        //      ranking anterior (detectar progresso ou regresso)..............................................(int para identificar)
        //      taxa de acerto dos últimos 5 questionários (média) (ou menos caso não tenham suficientem)......(float (0.0 a 1.0))
        //      taxa de acerto último questionário.............................................................(float (0.0 a 1.0))

        //PARÂMETROS:
        //      Usuario usuario, List<NivelConteudo> listaNivelConteudo
        listaNivelConteudo = classeIntermediaria.carregaListaDeNivelConteudoComConteudo(listaConteudos, usuario);
        listaExplicacoes = classeIntermediaria.criaVariasExplicacoesFeedback(usuario, listaConteudo);
    }

    @Override
    public FeedbackAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_row_feedback,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FeedbackAdapter.MyViewHolder holder, final int position){
        Feedback feedbackAtual = listaFeedback.get(position);
        NivelConteudo nivelConteudoAtual = listaNivelConteudo.get(position);
        holder.tvNomeConteudoTelaFeedback.setText(feedbackAtual.getConteudo().getNomeConteudo());
        switch (nivelConteudoAtual.getNivel().getValor()){
            case 1:
                holder.tvNivelAtualTelaFeedback.setText("Cobre");
                break;
            case 2:
                holder.tvNivelAtualTelaFeedback.setText("Bronze");
                break;
            case 3:
                holder.tvNivelAtualTelaFeedback.setText("Prata");
                break;
            case 4:
                holder.tvNivelAtualTelaFeedback.setText("Ouro");
                break;
            case 5:
                holder.tvNivelAtualTelaFeedback.setText("Diamante");
                break;
            default:
                holder.tvNivelAtualTelaFeedback.setText("ERRO");
        }

        holder.tvQuantidadeVidasTelaFeedback.setText(nivelConteudoAtual.getVidas()+"x");
        holder.tvTentativasRestantesTelaFeedback.setText(nivelConteudoAtual.getTentativas());
        //explicação
        holder.tvExplicacaoTelaFeedback.setText(listaExplicacoes.get(position));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvNomeConteudoTelaFeedback,
                //tvUltimaPontuacaoTelaFeedback,
                tvNivelAtualTelaFeedback,
                tvTentativasRestantesTelaFeedback,
                tvQuantidadeVidasTelaFeedback,
                tvExplicacaoTelaFeedback;

        ImageView ivIconeNivelTelaFeedback, ivQuantidadeVidasTelaFeedback;

        public MyViewHolder(View itemView){
            super(itemView);
            tvNomeConteudoTelaFeedback = itemView.findViewById(R.id.tvNomeConteudoTelaFeedback);
            //tvUltimaPontuacaoTelaFeedback = itemView.findViewById(R.id.tvUltimaPontuacaoTelaFeedback);
            tvNivelAtualTelaFeedback = itemView.findViewById(R.id.tvNivelAtualTelaFeedback);
            tvTentativasRestantesTelaFeedback = itemView.findViewById(R.id.tvTentativasRestantesTelaFeedback);
            tvExplicacaoTelaFeedback = itemView.findViewById(R.id.tvExplicacaoTelaFeedback);
            tvQuantidadeVidasTelaFeedback = itemView.findViewById(R.id.tvQuantidadeVidasTelaFeedback);

            ivIconeNivelTelaFeedback = itemView.findViewById(R.id.ivIconeNivelTelaFeedback);
            ivQuantidadeVidasTelaFeedback = itemView.findViewById(R.id.ivQuantidadeVidasTelaFeedback);
        }
    }

    public interface FeedbackOnClickListener{
        public void onClickFeedback(View view, int position);
    }
}

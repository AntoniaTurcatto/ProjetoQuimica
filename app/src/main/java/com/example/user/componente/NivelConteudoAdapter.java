package com.example.user.componente;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.classesDominio.NivelConteudo;
import com.example.user.projetoquimica.R;

import java.util.List;

public class NivelConteudoAdapter extends RecyclerView.Adapter<NivelConteudoAdapter.MyViewHolder> {
    private List<NivelConteudo> listaNivelConteudos;
    private NivelConteudoOnClickListener nivelConteudoOnClickListener;
    private Context context;

    public NivelConteudoAdapter(List<NivelConteudo> listaNivelConteudos, NivelConteudoOnClickListener nivelConteudoOnClickListener, Context context) {
        this.listaNivelConteudos = listaNivelConteudos;
        this.nivelConteudoOnClickListener = nivelConteudoOnClickListener;
        this.context = context;
    }

    @Override
    public NivelConteudoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NivelConteudoAdapter.MyViewHolder holder, final int position) {
        NivelConteudo meuNivelConteudo = listaNivelConteudos.get(position);

        Log.d("TesteVidas", "VidasAdapter: "+meuNivelConteudo.getVidas());
        Log.d("Adapter","Tentativas Restantes: " + meuNivelConteudo.getTentativas());


        holder.tvNomeConteudo.setText(meuNivelConteudo.getConteudo().getNomeConteudo());
        //holder.tvNomeNivel.setText("Nível: " + meuNivelConteudo.getNivel().name());
        String nivel = meuNivelConteudo.getNivel().name();
        holder.tvNomeNivel.setText("Nível: " + nivel.substring(0,1).toUpperCase() + nivel.substring(1).toLowerCase()); // enum retorna tudo em maíusculo e quero apenas a primeira letra em caixa alta
        //obter qual o icone do nivel conteudo (img)
        holder.imImagemNivel.setImageDrawable(meuNivelConteudo.getImagemNivelAlternativo(this.context));


        holder.tvMostrarQuantidaVidas.setText(String.valueOf(meuNivelConteudo.getVidas()) + "x");
        holder.imAtomoVidas.setImageDrawable(meuNivelConteudo.getImagemVidasConteudo(this.context));

        //clique no item de conteudo
        if (nivelConteudoOnClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nivelConteudoOnClickListener.onClickNivelConteudo(holder.itemView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaNivelConteudos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imImagemNivel, imAtomoVidas;
        TextView tvNomeConteudo, tvNomeNivel, tvMostrarQuantidaVidas;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvNomeConteudo = (TextView) itemView.findViewById(R.id.tvNomeConteudo);

            imImagemNivel = (ImageView) itemView.findViewById(R.id.imImagemNivel);
            tvNomeNivel = (TextView) itemView.findViewById(R.id.tvNomeNivel);

            tvMostrarQuantidaVidas = (TextView) itemView.findViewById(R.id.tvMostrarQuantidaVidas);
            imAtomoVidas = (ImageView) itemView.findViewById(R.id.imAtomoVidas);
        }
    }

    public interface NivelConteudoOnClickListener {
        public void onClickNivelConteudo(View view, int position);
    }
}

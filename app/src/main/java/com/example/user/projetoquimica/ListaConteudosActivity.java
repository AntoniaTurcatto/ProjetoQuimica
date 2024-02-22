package com.example.user.projetoquimica;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.user.banco.ConteudoDB;
import com.example.user.banco.InformacoesApp;
import com.example.user.banco.NivelConteudoDB;
import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.NivelConteudo;
import com.example.user.componente.ListaConteudosAdapter;
import com.example.user.componente.ProgressoAdapter;

import java.util.ArrayList;

public class ListaConteudosActivity extends AppCompatActivity {
    ListaConteudosAdapter listaConteudosAdapter;
    InformacoesApp informacoesApp;
    ConteudoDB conteudoDB = new ConteudoDB(ListaConteudosActivity.this);
    RecyclerView rvListaConteudos;
    ArrayList<Conteudo> lstConteudos;

    TextView tvMostrarQuantidaVidas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_conteudos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvListaConteudos = findViewById(R.id.rvListaConteudos);
        tvMostrarQuantidaVidas = findViewById(R.id.tvMostrarQuantidaVidas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        informacoesApp = (InformacoesApp) getApplicationContext();

        lstConteudos = conteudoDB.buscaConteudos(informacoesApp.getTipoConteudo());

        if(lstConteudos != null){
            listaConteudosAdapter = new ListaConteudosAdapter(lstConteudos, trataCliqueItem);
            rvListaConteudos.setLayoutManager(new LinearLayoutManager(ListaConteudosActivity.this));
            rvListaConteudos.setItemAnimator(new DefaultItemAnimator());
            rvListaConteudos.setAdapter(listaConteudosAdapter);
        }

    }

    ListaConteudosAdapter.ConteudoOnClickListener trataCliqueItem = new ListaConteudosAdapter.ConteudoOnClickListener() {
        @Override
        public void onClickConteudo(View view, int position) {
            Conteudo cont = lstConteudos.get(position);
            Intent it = new Intent(ListaConteudosActivity.this, GraficoDesempenhoConteudo.class);
            it.putExtra("conteudo", cont);
            startActivity(it);
        }
    };

}

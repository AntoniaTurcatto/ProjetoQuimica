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
import android.widget.Toast;

import com.example.user.banco.InformacoesApp;
import com.example.user.banco.NivelConteudoDB;
import com.example.user.classesDominio.NivelConteudo;
import com.example.user.classesDominio.Pergunta;
import com.example.user.componente.ListaConteudosAdapter;
import com.example.user.componente.ProgressoAdapter;

import java.util.ArrayList;

public class VisualizaProgressoActivity extends AppCompatActivity {
    ProgressoAdapter progressoAdapter;
    InformacoesApp informacoesApp;
    RecyclerView rvProgresso;
    NivelConteudoDB nivelConteudoDB = new NivelConteudoDB(VisualizaProgressoActivity.this);
    ArrayList<NivelConteudo> listaNivelConteudo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_progresso);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvProgresso = findViewById(R.id.rvProgresso);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        informacoesApp = (InformacoesApp) getApplicationContext();

        Intent it = getIntent();

        if (it.getSerializableExtra("listaConteudo" ) != null) {
            listaNivelConteudo = (ArrayList<NivelConteudo>) it.getSerializableExtra("listaConteudo");

        }else{
            listaNivelConteudo = nivelConteudoDB.carregaListaCompleta(informacoesApp.getMeuUsuario(), informacoesApp.getTipoConteudo());
        }

        progressoAdapter = new ProgressoAdapter(listaNivelConteudo, trataCliqueItem, VisualizaProgressoActivity.this);
        rvProgresso.setLayoutManager(new LinearLayoutManager(VisualizaProgressoActivity.this));
        rvProgresso.setItemAnimator(new DefaultItemAnimator());
        rvProgresso.setAdapter(progressoAdapter);

    }
        ProgressoAdapter.ProgressoOnClickListener trataCliqueItem = new ProgressoAdapter.ProgressoOnClickListener() {
            @Override
            public void onClickProgresso(View view, int position) {
                NivelConteudo nivelConteudo = listaNivelConteudo.get(position);
                Intent it = new Intent(VisualizaProgressoActivity.this, VisualizaProgressoActivity.class);
                startActivity(it);
            }
        };
    }
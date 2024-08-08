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
import android.view.Menu;
import android.view.MenuItem;
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
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        //VOLTAR A TELA ANTERIOR
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        //QUIMICA ORGANICA
        if(informacoesApp.getTipoConteudo() == 1){
            menu.findItem(R.id.iv_organica_ou_inorganica).setIcon(R.mipmap.organica);
        } else {
            menu.findItem(R.id.iv_organica_ou_inorganica).setIcon(R.mipmap.inorganica);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.iv_organica_ou_inorganica){
            //tipo de quimica (inorganica ou organica) por escrito
            String tipoQuimica;
            //QUIMICA ORGANICA
            if(informacoesApp.getTipoConteudo() == 1){
                tipoQuimica = "Organica";

            } else {
                //QUIMICA INORGANICA
                tipoQuimica = "Inorganica";
            }
            Toast.makeText(informacoesApp, "Você está no modo Química "+ tipoQuimica + "\nCaso deseja trocar volte ao menu de escolha de modo (organica ou inorganica)", Toast.LENGTH_SHORT).show();
        }

        if(id == R.id.action_informacoes){
            Toast.makeText(informacoesApp, "Clicou no item de settings", Toast.LENGTH_SHORT).show();
        }

        return true;
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
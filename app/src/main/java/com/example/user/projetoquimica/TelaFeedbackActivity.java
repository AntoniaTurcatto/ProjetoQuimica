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
import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.Feedback;
import com.example.user.classesDominio.NivelConteudo;
import com.example.user.componente.FeedbackAdapter;

import java.util.ArrayList;

public class TelaFeedbackActivity extends AppCompatActivity {

    InformacoesApp informacoesApp;
    RecyclerView rvFeedbacksConteudosTelaFeedback;
    FeedbackAdapter feedbackAdapter;
    Intent it;
    ArrayList<Conteudo> listaConteudos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_feedback);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        informacoesApp = (InformacoesApp)getApplicationContext();
        rvFeedbacksConteudosTelaFeedback = findViewById(R.id.rvFeedbacksConteudosTelaFeedback);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        it = getIntent();
        if(it != null){
            listaConteudos = (ArrayList<Conteudo>) it.getSerializableExtra("listaConteudos");
            if (listaConteudos != null){
                feedbackAdapter = new FeedbackAdapter(informacoesApp.getMeuUsuario(), listaConteudos, feedbackAdapterOnClickListener, getApplicationContext());
                rvFeedbacksConteudosTelaFeedback.setLayoutManager(new LinearLayoutManager(TelaFeedbackActivity.this));
                rvFeedbacksConteudosTelaFeedback.setItemAnimator(new DefaultItemAnimator());
                rvFeedbacksConteudosTelaFeedback.setAdapter(feedbackAdapter);
            }
        }


    }

    FeedbackAdapter.FeedbackOnClickListener feedbackAdapterOnClickListener = new FeedbackAdapter.FeedbackOnClickListener() {
        @Override
        public void onClickFeedback(View view, int position, NivelConteudo nivelConteudo, String explicacao) {
            Intent it = new Intent(TelaFeedbackActivity.this, TelaFeedbackDetalhada.class);
            it.putExtra("nivelConteudo", nivelConteudo);
            it.putExtra("explicacao", explicacao);
            startActivity(it);
        }
    };

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
        if (id == R.id.iv_organica_ou_inorganica) {
            //tipo de quimica (inorganica ou organica) por escrito
            String tipoQuimica;
            //QUIMICA ORGANICA
            if (informacoesApp.getTipoConteudo() == 1) {
                tipoQuimica = "Organica";

            } else {
                //QUIMICA INORGANICA
                tipoQuimica = "Inorganica";
            }
            Toast.makeText(informacoesApp, "Você está no modo Química " + tipoQuimica + "\nCaso deseja trocar volte ao menu de escolha de modo (organica ou inorganica)", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.action_informacoes) {
            Toast.makeText(informacoesApp, "Clicou no item de settings", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}

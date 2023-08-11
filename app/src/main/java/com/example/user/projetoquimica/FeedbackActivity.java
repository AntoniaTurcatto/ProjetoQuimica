package com.example.user.projetoquimica;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.user.banco.InformacoesApp;
import com.example.user.banco.NivelConteudoDB;
import com.example.user.classesDominio.DesempenhoConteudo;
import com.example.user.classesDominio.Feedback;
import com.example.user.classesDominio.NivelConteudo;

import java.util.ArrayList;

public class FeedbackActivity extends AppCompatActivity {
    ArrayList<NivelConteudo> listaNivelConteudos;
    ArrayList<Feedback> listaFeedbacks;
    Feedback meuFeedback;
    NivelConteudo nivelConteudo;
    InformacoesApp informacoesApp;
    int tipoDesempenho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        informacoesApp = (InformacoesApp)getApplicationContext();
        Intent it = getIntent();

        if (it != null) {
            listaNivelConteudos = (ArrayList<NivelConteudo>)it.getSerializableExtra("listaNivelConteudos");
            listaFeedbacks = (ArrayList<Feedback>)it.getSerializableExtra("listaFeedbacks");
            tipoDesempenho = it.getIntExtra("tipoDesempenho", tipoDesempenho);
        }

        for (int x = 0; x < listaFeedbacks.size(); x++){
            Log.d("Teste", "FEEDBACK Lista Feedbacks = Conteudo: " + listaFeedbacks.get(x).getConteudo().getNomeConteudo() + ", nivel anterior: " + listaFeedbacks.get(x).getNivelAnterior() + ", nivel atual: " + listaFeedbacks.get(x).getNivelAtual());
        }

        for (int x = 0; x < listaFeedbacks.size(); x++) {
            Log.d("Teste", "entrei no for de feedback");
            meuFeedback = listaFeedbacks.get(x);
            Log.d("Teste", "FEEDBACK Conteudo: " + meuFeedback.getConteudo().getNomeConteudo() + ", nivel anterior: " + meuFeedback.getNivelAnterior() + ", niveis avançados: " + meuFeedback.getNiveisAvancados() + ", nivel atual: " + meuFeedback.getNivelAtual());
            nivelConteudo = listaNivelConteudos.get(x);
            if (meuFeedback.getNiveisAvancados() > 0) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(FeedbackActivity.this);
                alerta.setTitle("Feedback");
                alerta
                        .setIcon(R.mipmap.ic_feedback)
                        .setMessage("Você prestou um diagnóstico do conteúdo: " + meuFeedback.getConteudo().getNomeConteudo() + ", seu nível era: " + meuFeedback.getNivelAnterior() + " e agora, você pode avançar em: " + meuFeedback.getNiveisAvancados() + " níveis, para o nível " + meuFeedback.getNivelAtual() + "\n Deseja consolidar essa troca?")
                        .setCancelable(false)
                        //testar
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // usuário optou por não consolidar as alterações
                            }
                        })
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // consolida os avanços e adiciona na lista para atualizar
                                Log.d("Teste", "Antes de atualizar - :" + nivelConteudo.getNivel());
                                nivelConteudo.setNivel(meuFeedback.getNivelAtual());
                                Log.d("Teste", "Atualizado - :" + nivelConteudo.getNivel());
                                // Vou consolidar no banco o novo nivel
                                NivelConteudoDB nivelConteudoDB = new NivelConteudoDB(getApplicationContext());
                                nivelConteudoDB.incrementaNivel(nivelConteudo, informacoesApp.getMeuUsuario());
                                Intent it = new Intent(FeedbackActivity.this, VisualizacaoNiveisActivity.class);
                                it.putExtra("listaNivelConteudos", listaNivelConteudos);
                                it.putExtra("tipoDesempenho", tipoDesempenho);
                                startActivity(it);
                            }
                        });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
            } else {
                AlertDialog.Builder alerta = new AlertDialog.Builder(FeedbackActivity.this);
                alerta.setTitle("Feedback");
                alerta
                        .setIcon(R.mipmap.ic_feedback)
                        .setMessage("Você prestou um diagnóstico do conteúdo: " + meuFeedback.getConteudo().getNomeConteudo() + ", seu nível era: " + meuFeedback.getNivelAnterior() + ", e se manteve assim!")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // nenhuma alteração acontece
                            }
                        });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
            }
        }
    }
}

package com.example.user.projetoquimica;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.banco.ConteudoDB;
import com.example.user.banco.InformacoesApp;
import com.example.user.banco.NivelConteudoDB;
import com.example.user.classesDominio.ClasseIntermediaria;
import com.example.user.componente.ListaConteudosAdapter;

public class QuizCadastroActivity extends AppCompatActivity {

    Button bCadastroPerguntas, bCadastroConteudos, bQuiz, bConteudoNivel, bVisualizarNiveisConteudos,
    bDiagnostico, bGraficoDesempenhoConteudo, bGraficoDezQuestionarios, bGraficoNiveisPorConteudo, bProgresso, bFeedbackMenuCadastro;
    InformacoesApp informacoesApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_cadastro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        bCadastroPerguntas = findViewById(R.id.bCadastroPerguntas);
//        bCadastroConteudos = findViewById(R.id.bCadastroConteudos);
        bQuiz = findViewById(R.id.bQuiz);
//        bConteudoNivel = findViewById(R.id.bConteudoNivel);
        bVisualizarNiveisConteudos = findViewById(R.id.bVisualizarlistaNiveisConteudos);
        bDiagnostico = findViewById(R.id.bDiagnostico);
        bGraficoDesempenhoConteudo = findViewById(R.id.bGraficoDesempenhoConteudo);
//        bGraficoDezQuestionarios = findViewById(R.id.bGraficoDezQuestionarios);
        bGraficoNiveisPorConteudo = findViewById(R.id.bGraficoNiveisPorConteudo);
//        bProgresso = findViewById(R.id.bProgresso);
        informacoesApp = (InformacoesApp)getApplicationContext();
        bFeedbackMenuCadastro = findViewById(R.id.bFeedbackMenuCadastro);



        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        bCadastroPerguntas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(QuizCadastroActivity.this, EntradaDePerguntasActivity.class);
//                startActivity(it);
//            }
//        });
//-----------------------------------
//        bCadastroConteudos.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(QuizCadastroActivity.this, EntradaDeConteudosActivity.class);
//                startActivity(it);
//            }
//        });
//-------------------------------------
        bQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(QuizCadastroActivity.this, FiltroActivity.class);
                startActivity(it);
            }
        });

//        bConteudoNivel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent it = new Intent(QuizCadastroActivity.this, TelaTeste.class);
//                startActivity(it);
//            }
//        });

        bVisualizarNiveisConteudos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NivelConteudoDB nivelConteudoDB = new NivelConteudoDB(getApplicationContext());
                InformacoesApp app = (InformacoesApp) getApplicationContext();

                Intent it = new Intent(QuizCadastroActivity.this, VisualizacaoNiveisActivity.class);
                it.putExtra("listaNivelConteudos", nivelConteudoDB.carregaListaCompleta(app.getMeuUsuario(),app.getTipoConteudo()));
                startActivity(it);
            }
        });

        bDiagnostico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(QuizCadastroActivity.this, FiltroDiagnosticoActivity.class);
                startActivity(it);
            }
        });

        bGraficoDesempenhoConteudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(QuizCadastroActivity.this, ListaConteudosActivity.class);
                startActivity(it);
            }
        });
//        bGraficoDezQuestionarios.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent it = new Intent(QuizCadastroActivity.this, GraficoDezQuestionariosActivity.class);
//                startActivity(it);
//            }
//        });
        bGraficoNiveisPorConteudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(QuizCadastroActivity.this, GraficoNiveisPorConteudoActivity.class);
                startActivity(it);
            }
        });

        bFeedbackMenuCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(QuizCadastroActivity.this, TelaFeedbackActivity.class);
                it.putExtra("listaConteudos",new ConteudoDB(getApplicationContext()).buscaConteudos(informacoesApp.getTipoConteudo()));
                startActivity(it);
            }
        });
//----------------------------------------------
//        bProgresso.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(QuizCadastroActivity.this, VisualizaProgressoActivity.class);
//                startActivity(it);
//            }
//        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu_quiz, menu);
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
        } else if(id == R.id.cadastroConteudo){
            Intent it = new Intent(QuizCadastroActivity.this, EntradaDeConteudosActivity.class);
            startActivity(it);

        } else if(id == R.id.cadastroPergunta){
            Intent it = new Intent(QuizCadastroActivity.this, EntradaDePerguntasActivity.class);
            startActivity(it);

        } else if(id == R.id.adicionarNivelConteudo){
            Intent it = new Intent(QuizCadastroActivity.this, TelaTeste.class);
            startActivity(it);

        } else if(id == R.id.dezQuesionarios){
            Intent it = new Intent(QuizCadastroActivity.this, GraficoDezQuestionariosActivity.class);
            startActivity(it);

        } else if(id == R.id.progresso){
            Intent it = new Intent(QuizCadastroActivity.this, VisualizaProgressoActivity.class);
            startActivity(it);
        }



        return true;
    }

}

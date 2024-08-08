package com.example.user.projetoquimica;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.banco.ConteudoDB;
import com.example.user.banco.InformacoesApp;
import com.example.user.banco.PerguntaDB;
import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.Pergunta;
import com.example.user.componente.MultiSelectionSpinner;

import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;

public class FiltroActivity extends AppCompatActivity {
    ConteudoDB conteudoDB;
    Button bSalvar;
    RadioButton rbSelecionar, rbSortear;
    EditText etQuantidadePerguntas, etQuantidadeConteudos;
    ArrayList<Conteudo> listaConteudos;
    ArrayList<Conteudo> listaConteudosSelecionados;
    MultiSelectionSpinner spMultiConteudos;
    int quantidadePerguntas = 0, quantidadeConteudos;
    Spinner spTestePrevio;
    InformacoesApp informacoesApp;
    int testePrevio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        etQuantidadePerguntas = findViewById(R.id.etQuantidadePerguntas);
        etQuantidadeConteudos = findViewById(R.id.etQuantidadeConteudos);
        bSalvar = findViewById(R.id.bSalvar);
        rbSelecionar = findViewById(R.id.rbSelecionar);
        rbSortear = findViewById(R.id.rbSortear);
        spMultiConteudos = findViewById(R.id.spMultiConteudos);
        spTestePrevio = findViewById(R.id.spTestePrevio);

        informacoesApp = (InformacoesApp)getApplicationContext();


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listaConteudos = new ArrayList<>();

        conteudoDB = new ConteudoDB(getApplicationContext());
        listaConteudos = conteudoDB.buscaConteudos(informacoesApp.getTipoConteudo());
        Log.d("Teste"," Lista de Conteudos: " + listaConteudos.size() + " Tipo Conteudo:" + informacoesApp.getTipoConteudo());
        spMultiConteudos.setItems(listaConteudos);


        spMultiConteudos.setEnabled(false);

        rbSelecionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etQuantidadePerguntas.setEnabled(true);
                etQuantidadeConteudos.setEnabled(false);
                spMultiConteudos.setEnabled(true);
            }
        });

        rbSortear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etQuantidadePerguntas.setEnabled(true);
                etQuantidadeConteudos.setEnabled(true);
                spMultiConteudos.setEnabled(false);
            }
        });

        bSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etQuantidadePerguntas.getText().toString().equals("")){

                    quantidadePerguntas = Integer.parseInt(etQuantidadePerguntas.getText().toString());
                    if(spTestePrevio.getSelectedItemPosition() != 0){

                        //VERIFICANDO SE USUÁRIO DESEJA TESTE PRÉVIO OU NÃO:
                        if(spTestePrevio.getSelectedItemPosition() == 1){
                            testePrevio = 1; //com teste previo
                        } else {
                            testePrevio = 0; // sem teste previo
                        }


                        if (rbSelecionar.isChecked()){
                            if (spMultiConteudos.getSelectedSize() > 0) {
                                // chamando o método para obter a lista de conteúdos que foram selecionados
                                listaConteudosSelecionados = spMultiConteudos.getSelectedItems();



                                //ver se no modo SelecionarConteudos tem perguntas suficientes no banco para os conteudos selecionados
                                PerguntaDB perguntaDB = new PerguntaDB(getApplicationContext());
                                ArrayList<Pergunta> listaPerguntas = perguntaDB.buscaPergunta(getApplicationContext());

                                if(perguntasSuficientes(quantidadePerguntas, listaConteudosSelecionados, listaPerguntas)){
                                    // fazer o que precisar com a lista, vou mostrar na tela
                                    String msg = "";
                                    for (int x = 0; x < listaConteudosSelecionados.size(); x++) {
                                        Conteudo conteudo = listaConteudosSelecionados.get(x);

                                        msg = msg + "\nConteúdo: " + conteudo.getNomeConteudo();
                                    }
                                    Intent it = new Intent(FiltroActivity.this, QuizDiagnosticoActivity.class);
                                    it.putExtra("listaConteudos", listaConteudosSelecionados);
                                    it.putExtra("tipo",1); //1 = quiz, 2 = diagnóstico (FiltroDiagnosticoActivity)
                                    it.putExtra("quantidade",quantidadePerguntas);
                                    it.putExtra("testePrevio", testePrevio);
                                    startActivity(it);
                                    Toast.makeText(FiltroActivity.this, "Conteúdos selecionados: " + msg, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(informacoesApp, "Erro: foi solicitado uma quantidade de perguntas maior que a cadastrada nos conteudos solicitados", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(FiltroActivity.this, "Selecione os conteúdos que deseja!", Toast.LENGTH_SHORT).show();
                            }
                        } else if (rbSortear.isChecked()){
                            if(!etQuantidadeConteudos.getText().toString().equals("")){
                                quantidadeConteudos = Integer.parseInt(etQuantidadeConteudos.getText().toString());
                                int tipoConteudo = informacoesApp.getTipoConteudo();




                                //verificando se há conteudos suficientes
                                ConteudoDB conteudoDB = new ConteudoDB(getApplicationContext());
                                ArrayList<Conteudo> listaTodosConteudos = conteudoDB.buscaConteudos(tipoConteudo);

                                if (listaTodosConteudos.size() >= quantidadeConteudos){ //SE lista de conteudos >= quantidade de conteudos selecionados
                                    listaConteudosSelecionados = conteudoDB.buscaConteudosAleatorios(quantidadeConteudos, tipoConteudo);

                                    PerguntaDB perguntaDB = new PerguntaDB(getApplicationContext());
                                    ArrayList<Pergunta> listaPerguntas = perguntaDB.buscaPergunta(getApplicationContext());

                                    if(perguntasSuficientes(quantidadePerguntas, listaConteudosSelecionados, listaPerguntas)){
                                        Intent it = new Intent(FiltroActivity.this, QuizDiagnosticoActivity.class);
                                        it.putExtra("listaConteudos", listaConteudosSelecionados);
                                        it.putExtra("quantidade",quantidadePerguntas);
                                        it.putExtra("tipo", 1); //1 = quiz, 2 = diagnóstico (FiltroDiagnosticoActivity)
                                        it.putExtra("testePrevio", testePrevio);
                                        startActivity(it);
                                    } else {
                                        Toast.makeText(FiltroActivity.this, "Erro: Conteudos insuficientes no banco de dados para esses conteudos", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(FiltroActivity.this, "Erro: Conteudos insuficientes no banco de dados", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                etQuantidadeConteudos.setError("Informe a quantidade de conteúdos!");
                                etQuantidadeConteudos.requestFocus();
                            }
                        }
                    } else {
                        Toast.makeText(informacoesApp, "Por favor, selecione se deseja realizar teste prévio", Toast.LENGTH_SHORT).show();
                        spTestePrevio.requestFocus();
                    }
                } else {
                    etQuantidadePerguntas.setError("Informe a quantidade de perguntas!");
                    etQuantidadePerguntas.requestFocus();
                }
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

    private boolean perguntasSuficientes(int quantidadePerguntas, ArrayList<Conteudo> listaConteudosSelecionados, ArrayList<Pergunta> listaPerguntas){
        //ver se no modo SelecionarConteudos tem perguntas suficientes no banco para os conteudos selecionados
        PerguntaDB perguntaDB = new PerguntaDB(getApplicationContext());
        boolean perguntasSuficientes = true;

        for (int x=0; x< listaConteudosSelecionados.size(); x++){
            int perguntasDoConteudo=0;
            Conteudo conteudoAtual = listaConteudosSelecionados.get(x);
            //percorrer lista de perguntas para achar as com conteudo == ao conteudo do for superior
            for (int x2 = 0; x2 < listaPerguntas.size(); x2++){
                if (listaPerguntas.get(x2).getConteudo().getIdConteudo() == conteudoAtual.getIdConteudo()){
                    perguntasDoConteudo++;
                    if (perguntasDoConteudo >= quantidadePerguntas){
                        break; // ja achou perguntas suficientes para o conteudo e não é mais necessário contar
                    }
                }
            }

            if (perguntasDoConteudo < quantidadePerguntas){
                perguntasSuficientes = false; // ja identificou um conteudo com perguntas insuficientes
                break;
            }
        }
        return perguntasSuficientes;
    }
}

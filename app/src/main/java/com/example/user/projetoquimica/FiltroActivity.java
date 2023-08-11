package com.example.user.projetoquimica;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.banco.ConteudoDB;
import com.example.user.banco.InformacoesApp;
import com.example.user.classesDominio.Conteudo;
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
    InformacoesApp informacoesApp;


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


        informacoesApp = (InformacoesApp)getApplicationContext();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

                        if (rbSelecionar.isChecked()){
                            if (spMultiConteudos.getSelectedSize() > 0) {
                                // chamando o método para obter a lista de conteúdos que foram selecionados
                                listaConteudosSelecionados = spMultiConteudos.getSelectedItems();
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
                                startActivity(it);
                                Toast.makeText(FiltroActivity.this, "Conteúdos selecionados: " + msg, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FiltroActivity.this, "Selecione os conteúdos que deseja!", Toast.LENGTH_SHORT).show();
                            }
                        } else if (rbSortear.isChecked()){
                            if(!etQuantidadeConteudos.getText().toString().equals("")){
                                quantidadeConteudos = Integer.parseInt(etQuantidadeConteudos.getText().toString());
                                int tipoConteudo = informacoesApp.getTipoConteudo();
                                listaConteudosSelecionados = conteudoDB.buscaConteudosAleatorios(quantidadeConteudos, tipoConteudo);
                                Intent it = new Intent(FiltroActivity.this, QuizDiagnosticoActivity.class);
                                it.putExtra("listaConteudos", listaConteudosSelecionados);
                                it.putExtra("quantidade",quantidadePerguntas);
                                it.putExtra("tipo", 1); //1 = quiz, 2 = diagnóstico (FiltroDiagnosticoActivity)
                                startActivity(it);
                            } else {
                                etQuantidadeConteudos.setError("Informe a quantidade de conteúdos!");
                                etQuantidadeConteudos.requestFocus();
                            }
                        }
                } else {
                    etQuantidadePerguntas.setError("Informe a quantidade de perguntas!");
                    etQuantidadePerguntas.requestFocus();
                }
            }
        });

    }
}

package com.example.user.projetoquimica;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.banco.ConteudoDB;
import com.example.user.banco.InformacoesApp;
import com.example.user.classesDominio.Conteudo;
import com.example.user.componente.MultiSelectionSpinner;

import java.util.ArrayList;

public class FiltroDiagnosticoActivity extends AppCompatActivity {
    EditText etQuantidadePerguntasDiagnostico;
    MultiSelectionSpinner spMultiConteudosDiagnostico;
    Button bSalvarDiagnostico;
    ConteudoDB conteudoDB;
    InformacoesApp informacoesApp;
    ArrayList<Conteudo> listaConteudos;
    ArrayList<Conteudo> listaConteudosSelecionados;
    int quantidadePerguntas = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro_diagnostico);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etQuantidadePerguntasDiagnostico = findViewById(R.id.etQuantidadePerguntasDiagnostico);
        spMultiConteudosDiagnostico = findViewById(R.id.spMultiConteudosDiagnostico);
        bSalvarDiagnostico = findViewById(R.id.bSalvarDiagnostico);
        informacoesApp = (InformacoesApp)getApplicationContext();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listaConteudos = new ArrayList<>();

        conteudoDB = new ConteudoDB(getApplicationContext());
        listaConteudos = conteudoDB.buscaConteudos(informacoesApp.getTipoConteudo());
        spMultiConteudosDiagnostico.setItems(listaConteudos);

        bSalvarDiagnostico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etQuantidadePerguntasDiagnostico.getText().toString().equals("")){

                    quantidadePerguntas = Integer.parseInt(etQuantidadePerguntasDiagnostico.getText().toString());

                    if (spMultiConteudosDiagnostico.getSelectedSize() > 0) {
                        // chamando o método para obter a lista de conteúdos que foram selecionados
                        listaConteudosSelecionados = spMultiConteudosDiagnostico.getSelectedItems();
                        // fazer o que precisar com a lista, vou mostrar na tela
                        String msg = "";
                        for (int x = 0; x < listaConteudosSelecionados.size(); x++) {
                            Conteudo conteudo = listaConteudosSelecionados.get(x);

                            msg = msg + "\nConteúdo: " + conteudo.getNomeConteudo();
                        }
                        Intent it = new Intent(FiltroDiagnosticoActivity.this, QuizDiagnosticoActivity.class);
                        it.putExtra("listaConteudos", listaConteudosSelecionados);
                        it.putExtra("quantidade",quantidadePerguntas);
                        it.putExtra("tipo", 2); //1 = quiz(FiltroActivity), 2 = diagnóstico
                        startActivity(it);
                        Toast.makeText(FiltroDiagnosticoActivity.this, "Conteúdos selecionados: " + msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FiltroDiagnosticoActivity.this, "Selecione os conteúdos que deseja!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    etQuantidadePerguntasDiagnostico.setError("Informe a quantidade de perguntas!");
                    etQuantidadePerguntasDiagnostico.requestFocus();
                }
            }
        });
    }

}

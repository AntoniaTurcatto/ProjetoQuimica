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
import android.widget.ScrollView;

import com.example.user.classesDominio.Pergunta;
import com.example.user.componente.RelatorioPerguntaAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class RelatorioActivity extends AppCompatActivity {

    RecyclerView rvRelatorioVisualizaPerguntas;
    PieChart pcRelatorioResultados;
    ArrayList<Pergunta> listaPerguntas = new ArrayList<>();
    RelatorioPerguntaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //RecyclerView
        rvRelatorioVisualizaPerguntas = findViewById(R.id.rvRelatorioVisualizaPerguntas);
        //PieChart
        pcRelatorioResultados = findViewById(R.id.pcRelatorioResultados);

        Intent it = getIntent();

        if (it.hasExtra("listaPerguntas")) {
            listaPerguntas = (ArrayList<Pergunta>) it.getSerializableExtra("listaPerguntas");
            //Criando o gráfico das questões - Provisório
            ConfiguraGrafico(listaPerguntas);
            adapter = new RelatorioPerguntaAdapter(listaPerguntas, trataCliqueItem, RelatorioActivity.this);
            rvRelatorioVisualizaPerguntas.setLayoutManager(new LinearLayoutManager(RelatorioActivity.this));
            rvRelatorioVisualizaPerguntas.setItemAnimator(new DefaultItemAnimator());
            rvRelatorioVisualizaPerguntas.setAdapter(adapter);
        }
    }

    public void ConfiguraGrafico (ArrayList<Pergunta> listaPerguntas){
        ArrayList<PieEntry> entradasChartResultados = new ArrayList<>();
        float contAcertos = 0;
        float contErros = 0;
        for (int index = 0; index < listaPerguntas.size(); index++){
            if (listaPerguntas.get(index).getAlternativaCorreta() == listaPerguntas.get(index).getOpcaoEscolhida()){
                contAcertos++;
            } else {
                contErros++;
            }
        }
        //Adicionando os acetos
        PieEntry entradaAcertos = new PieEntry(contAcertos, "Acertos");
        entradasChartResultados.add(entradaAcertos);
        //Adicionando os erros
        PieEntry entradaErros = new PieEntry(contErros,"Erros");
        entradasChartResultados.add(entradaErros);
        //Adicionando os dados no DataSet
        PieDataSet dadosTorta = new PieDataSet(entradasChartResultados,"Conteúdo");
        //Configurando os elementos do DataSet

        //Inserindo o DataSet no gráfico
        pcRelatorioResultados.setData(new PieData(dadosTorta));
        //Personalizando o gráfico
        dadosTorta.setColors(ColorTemplate.JOYFUL_COLORS);
        dadosTorta.setValueTextSize(12f);
        pcRelatorioResultados.animateXY(2000,2000);
        pcRelatorioResultados.getDescription().setEnabled(false);
    }

    RelatorioPerguntaAdapter.PerguntaOnClickListener trataCliqueItem = new RelatorioPerguntaAdapter.PerguntaOnClickListener() {
        @Override
        public void onClickPergunta(View view, int position) {
            Pergunta pergunta = listaPerguntas.get(position);
            Intent it = new Intent(RelatorioActivity.this, VisualizacaoDetalhadaaquiActivity.class);
            it.putExtra("pergunta", pergunta);
            startActivity(it);
            finish();
        }
    };

}

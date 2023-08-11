package com.example.user.projetoquimica;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.user.banco.ConteudoDB;
import com.example.user.banco.DesempenhoConteudoDB;
import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.DesempenhoConteudo;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class GraficoDesempenhoConteudo extends AppCompatActivity {
    LineChart linechartDesempenhoConteudo;
    Conteudo conteudo;
    DesempenhoConteudoDB desempenhoConteudoDB = new DesempenhoConteudoDB(GraficoDesempenhoConteudo.this);
    //ConteudoDB conteudoDB = new ConteudoDB(GraficoDesempenhoConteudo.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_desempenho_conteudo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        linechartDesempenhoConteudo = findViewById(R.id.linechartDesempenhoConteudo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent it = getIntent(); //pegando o conteudo selecionado na activity ListaConteudosActivity
        conteudo = (Conteudo)it.getSerializableExtra("conteudo"); //atribuindo ao conteudo

        LineDataSet lineDataSet = new LineDataSet(carregaLineChart(),"teste");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        linechartDesempenhoConteudo.setData(data);
        linechartDesempenhoConteudo.invalidate();

    }

    private ArrayList<Entry> carregaLineChart(){
        ArrayList<Entry> listaEntradas = new ArrayList<Entry>();
        ArrayList<DesempenhoConteudo> listaDesempenhoConteudo = desempenhoConteudoDB.buscaDesempenhoConteudo();

        float cont=0;
        for(int x = 0; x < listaDesempenhoConteudo.size(); x++){
            if(conteudo.getIdConteudo() == listaDesempenhoConteudo.get(x).getConteudo().getIdConteudo()){
                DesempenhoConteudo desempenhoConteudo = listaDesempenhoConteudo.get(x);
                listaEntradas.add(new Entry(cont, desempenhoConteudo.getPontuacaoConteudo()));
                cont++;
            }
        }
        return listaEntradas;
    }
}

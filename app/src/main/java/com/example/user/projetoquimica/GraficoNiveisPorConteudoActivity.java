package com.example.user.projetoquimica;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.user.banco.ConteudoDB;
import com.example.user.banco.InformacoesApp;
import com.example.user.banco.NivelConteudoDB;
import com.example.user.classesDominio.NivelConteudo;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraficoNiveisPorConteudoActivity extends AppCompatActivity {

    BarChart bcGraficoNiveisPorConteudo;
    ConteudoDB conteudoDB;
    NivelConteudoDB nivelConteudoDB;
    InformacoesApp informacoesApp;
    ArrayList<NivelConteudo> listaNiveisConteudos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_niveis_por_conteudo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Barchart
        bcGraficoNiveisPorConteudo = findViewById(R.id.bcGraficoNiveisPorConteudo);
        //ConteudoDB
        conteudoDB = new ConteudoDB(this);
        //NivelConteudoDB
        nivelConteudoDB = new NivelConteudoDB(this);
        //InformacoesApp
        informacoesApp = (InformacoesApp) getApplicationContext();

        listaNiveisConteudos = nivelConteudoDB.buscaConteudosComNivel(conteudoDB.buscaConteudos(informacoesApp.getTipoConteudo()), informacoesApp.getMeuUsuario());

        ConfiguraGrafico();
    }

    public void ConfiguraGrafico(){
        ArrayList<BarEntry> listaEntradas = new ArrayList<>();
        for (int x = 0; x < listaNiveisConteudos.size(); x++){
            NivelConteudo meuNivelConteudo = listaNiveisConteudos.get(x);
            listaEntradas.add(new BarEntry(x , (float)meuNivelConteudo.getNivel().getValor(), meuNivelConteudo.getImagemNivel(this)));
        }

        BarDataSet setNiveisPorConteudo = new BarDataSet(listaEntradas, "Níveis por Conteúdo");
        setNiveisPorConteudo.setDrawValues(false);
        setNiveisPorConteudo.setColors(ColorTemplate.JOYFUL_COLORS);
        BarData data = new BarData(setNiveisPorConteudo);
        data.setDrawValues(false);
        bcGraficoNiveisPorConteudo.setData(data);
        bcGraficoNiveisPorConteudo.animateY(2000);
        bcGraficoNiveisPorConteudo.getDescription().setText("Níveis por conteúdo");
        bcGraficoNiveisPorConteudo.getDescription().setTextColor(Color.BLACK);
        bcGraficoNiveisPorConteudo.setPinchZoom(false);
        System.out.println("Terminei");
    }

}

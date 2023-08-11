package com.example.user.projetoquimica;

import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.user.banco.DesempenhoQuestionarioDB;
import com.example.user.banco.InformacoesApp;
import com.example.user.classesDominio.DesempenhoQuestionario;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraficoDezQuestionariosActivity extends AppCompatActivity {

    LineChart lcGraficoDezQuestionariosLinha;
    InformacoesApp informacoesApp;
    DesempenhoQuestionarioDB desempenhoQuestionarioDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_dez_questionarios);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //LineChart
        lcGraficoDezQuestionariosLinha = findViewById(R.id.lcGraficoDezQuestionariosLinha);
        //InformacoesApp
        informacoesApp = (InformacoesApp) getApplicationContext();
        //DesempenhoQuestionarioDB
        desempenhoQuestionarioDB = new  DesempenhoQuestionarioDB (getApplicationContext());
        ConfiguraGrafico();
    }

    public void ConfiguraGrafico() {
        ArrayList<Entry> listaEntradas = new ArrayList<>();
        ArrayList<DesempenhoQuestionario> listaDesempenhoQuestionario = desempenhoQuestionarioDB.buscaDesempenhoQuestionario(informacoesApp.getMeuUsuario());
        final String []labels;
        if (listaDesempenhoQuestionario.size() < 10){
            labels = new String[listaDesempenhoQuestionario.size()];
        } else {
            labels = new String[10];
        }

        float cont = 0;
        System.out.println(listaDesempenhoQuestionario.size());
        for (int x = listaDesempenhoQuestionario.size(); x >= (listaDesempenhoQuestionario.size()-9); x--){
            DesempenhoQuestionario meuDesempenhoQuestionario = listaDesempenhoQuestionario.get(x-1);

            System.out.println(meuDesempenhoQuestionario.getPontuacaoFinal());

           labels[(int)cont] = String.valueOf(meuDesempenhoQuestionario.getData());

           listaEntradas.add(new Entry(cont, meuDesempenhoQuestionario.getPontuacaoFinal()));
           cont++;
        }

        LineDataSet setDesempenhoQuestionarios = new LineDataSet(listaEntradas, "Dez");
        setDesempenhoQuestionarios.setAxisDependency(YAxis.AxisDependency.LEFT);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setDesempenhoQuestionarios);
        LineData data = new LineData(dataSets);
        lcGraficoDezQuestionariosLinha.setData(data);
        lcGraficoDezQuestionariosLinha.invalidate();




    }

}

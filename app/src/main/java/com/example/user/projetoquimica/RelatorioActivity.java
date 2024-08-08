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
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.user.banco.InformacoesApp;
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
    InformacoesApp informacoesApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //RecyclerView
        rvRelatorioVisualizaPerguntas = findViewById(R.id.rvRelatorioVisualizaPerguntas);
        //PieChart
        pcRelatorioResultados = findViewById(R.id.pcRelatorioResultados);
        informacoesApp = (InformacoesApp)getApplicationContext();

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


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
            Intent it = new Intent(RelatorioActivity.this, RelatorioDetalhadoActivity2.class);
            it.putExtra("pergunta", pergunta);
            startActivity(it);
            finish();
        }
    };

}

package com.example.user.projetoquimica;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.banco.ConteudoDB;
import com.example.user.banco.InformacoesApp;
import com.example.user.banco.NivelConteudoDB;
import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.Feedback;
import com.example.user.classesDominio.NivelConteudo;
import com.example.user.componente.NivelConteudoAdapter;
import com.example.user.componente.NivelConteudoEnum;

import java.util.ArrayList;

public class VisualizacaoNiveisActivity extends AppCompatActivity {
    RecyclerView rvNiveis;
    NivelConteudoAdapter nivelConteudoAdapter;
    InformacoesApp informacoesApp;
    NivelConteudoDB nivelConteudoDB;
    ArrayList<NivelConteudo> listaNiveisCompleta, listaNivelConteudos;
    ImageView ivNivelConteudo, imAtomoVidas;
    TextView tvMostrarVidas;
    int tipoDesempenho;
    Context context;
    ConteudoDB conteudoDB;
    ArrayList<Conteudo> listaConteudos;
    Button bVisulizaTudo;
    NivelConteudo nivelConteudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizacao_niveis);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvNiveis = (RecyclerView) findViewById(R.id.rvNiveis);
        ivNivelConteudo = findViewById(R.id.imImagemNivel);

        imAtomoVidas = findViewById(R.id.imAtomoVidas);
        tvMostrarVidas = findViewById(R.id.tvMostrarQuantidaVidas);

        Intent it = getIntent();
        context = getApplicationContext();
        //obtendo o contexto
        informacoesApp = (InformacoesApp)getApplicationContext();
        //Log.d("Teste", "Id de usuário: " + informacoesApp.getMeuUsuario().getIdUsuario());
        conteudoDB = new ConteudoDB(getApplicationContext());
        listaConteudos = conteudoDB.buscaConteudos(informacoesApp.getTipoConteudo());
        nivelConteudoDB = new NivelConteudoDB(getApplicationContext());
        listaNiveisCompleta = nivelConteudoDB.buscaConteudosComNivel(listaConteudos, informacoesApp.getMeuUsuario());
        listaNivelConteudos = (ArrayList<NivelConteudo>) it.getSerializableExtra("listaNivelConteudos");
        tipoDesempenho = it.getIntExtra("tipoDesempenho", tipoDesempenho);
        bVisulizaTudo = findViewById(R.id.bVisualizaTudo);



        //Adaptar
        if (tipoDesempenho == 2){
            nivelConteudoAdapter = new NivelConteudoAdapter(listaNivelConteudos, trataCliqueItem, context);
            bVisulizaTudo.setEnabled(true); ;
        } else {
            nivelConteudoAdapter = new NivelConteudoAdapter(listaNiveisCompleta, trataCliqueItem, context); //NivelConteudoOnClickListener
        }
        bVisulizaTudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(VisualizacaoNiveisActivity.this, VisualizacaoNiveisActivity.class);
                startActivity(it);
            }
        });

        //Carregando Recycle CardView
        rvNiveis.setLayoutManager(new LinearLayoutManager(VisualizacaoNiveisActivity.this));
        rvNiveis.setItemAnimator(new DefaultItemAnimator());
        rvNiveis.setAdapter(nivelConteudoAdapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Obter a lista de conteúdos cadastradas no Banco - FiltroActivity
        //Tendo os conteúdos, consultar quais são os níveis - QuizDiagnósticoActivity
        //Passar a lista de nível conteúdo p/ o Adapter
    }
    NivelConteudoAdapter.NivelConteudoOnClickListener trataCliqueItem = new NivelConteudoAdapter.NivelConteudoOnClickListener() {
        @Override
        public void onClickNivelConteudo(View view, int position) {
            NivelConteudo nivelConteudo = listaNiveisCompleta.get(position);
            Intent it = new Intent(VisualizacaoNiveisActivity.this, VisualizaUpgradeActivity.class);
            it.putExtra("nivel", nivelConteudo);
            startActivity(it);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_informacoes) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
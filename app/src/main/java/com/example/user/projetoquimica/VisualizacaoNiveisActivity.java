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
import android.widget.Toast;

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
            bVisulizaTudo.setEnabled(true);
            bVisulizaTudo.setClickable(true);
        } else {
            nivelConteudoAdapter = new NivelConteudoAdapter(listaNiveisCompleta, trataCliqueItem, context); //NivelConteudoOnClickListener
            bVisulizaTudo.setEnabled(false);
            bVisulizaTudo.setVisibility(View.INVISIBLE);
            bVisulizaTudo.setClickable(false);
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
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
}
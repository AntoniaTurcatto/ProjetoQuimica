package com.example.user.projetoquimica;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.banco.InformacoesApp;
import com.example.user.banco.NivelConteudoDB;
import com.example.user.classesDominio.ClasseIntermediaria;
import com.example.user.classesDominio.Feedback;
import com.example.user.classesDominio.NivelConteudo;

public class VisualizaUpgradeActivity extends AppCompatActivity {
    TextView tvVisualizaTitulo, tvVisualizaSaudacao, tvMostrarQuantidadeVidas;
    ImageView ivVisualizaUpgrade, imAtomoVidas;
    Button bVisualizaUpgradeVoltar, bVisualizaPerguntas, bVisualizaHistorico, bVisualizaProgresso;
    Context context;

    NivelConteudo meuNivel;

    InformacoesApp informacoesApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_upgrade);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvVisualizaTitulo = findViewById(R.id.tvVisualizaTitulo);
        tvVisualizaSaudacao = findViewById(R.id.tvVisualizaSaudacao);
        tvMostrarQuantidadeVidas = findViewById(R.id.tvMostrarQuantidaVidas);
        imAtomoVidas = findViewById(R.id.imAtomoVidas);
        ivVisualizaUpgrade = findViewById(R.id.ivVisualizaUpgrade);
        //bVisualizaUpgradeVoltar = findViewById(R.id.bVisualizaUpgradeVoltar);
        bVisualizaPerguntas = findViewById(R.id.bVisualizaPerguntas);
        bVisualizaHistorico = findViewById(R.id.bVisualizaHistorico);
        bVisualizaProgresso = findViewById(R.id.bVisualizaProgresso);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent it = getIntent();

        context = getApplicationContext();

        informacoesApp = (InformacoesApp)getApplicationContext();


        if(it != null){
            meuNivel = (NivelConteudo) it.getSerializableExtra("nivel"); //aqui n seria
            ivVisualizaUpgrade.setImageDrawable(meuNivel.getImagemNivelCaminho(context));
            tvVisualizaTitulo.setText(informacoesApp.getMeuUsuario().getNomeUsuario().substring(0,1).toUpperCase() + informacoesApp.getMeuUsuario().getNomeUsuario().substring(1).toLowerCase() + ", confira o seu percurso, e a quantidade de vidas, no conteúdo " + meuNivel.getConteudo().getNomeConteudo() + ":");

            imAtomoVidas.setImageDrawable(meuNivel.getImagemVidasConteudo(context));
            tvMostrarQuantidadeVidas.setText(String.valueOf(meuNivel.getVidas()) + "x");

            if (it.hasExtra("feedback")) { // sinal que está sendo chamado através da tela de desempenho
                Feedback meuFeedback = (Feedback) it.getSerializableExtra("feedback");
                if (meuFeedback.getNiveisAvancados() > 0) {
                    tvVisualizaSaudacao.setText("Parabéns, você avançou de nível!");
                }
                // desabilitando os botões
                bVisualizaPerguntas.setVisibility(View.INVISIBLE);
                bVisualizaHistorico.setVisibility(View.INVISIBLE);
                bVisualizaHistorico.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(VisualizaUpgradeActivity.this, RelatorioDetalhadoActivity2.class);
                        startActivity(it);
                    }
                });
            }

        }
        bVisualizaProgresso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(VisualizaUpgradeActivity.this, VisualizaProgressoActivity.class);
                it.putExtra("listaConteudo", meuNivel);
                startActivity(it);
            }
        });

//        bVisualizaUpgradeVoltar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//
//        });
    }

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
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

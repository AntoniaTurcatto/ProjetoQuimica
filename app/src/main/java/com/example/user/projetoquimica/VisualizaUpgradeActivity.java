package com.example.user.projetoquimica;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.example.user.banco.InformacoesApp;
import com.example.user.banco.NivelConteudoDB;
import com.example.user.classesDominio.ClasseIntermediaria;
import com.example.user.classesDominio.Feedback;
import com.example.user.classesDominio.NivelConteudo;

public class VisualizaUpgradeActivity extends AppCompatActivity {
    TextView tvVisualizaTitulo, tvVisualizaSaudacao, tvMostrarQuantidadeVidas, tvNivelConteudoVisualizaUpgradeActivity;
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

        tvNivelConteudoVisualizaUpgradeActivity = findViewById(R.id.tvNivelConteudoVisualizaUpgradeActivity);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent it = getIntent();

        context = getApplicationContext();

        informacoesApp = (InformacoesApp)getApplicationContext();


        if(it != null){
            meuNivel = (NivelConteudo) it.getSerializableExtra("nivel"); //aqui n seria
            ivVisualizaUpgrade.setImageDrawable(meuNivel.getImagemNivelCaminho(context));
            tvVisualizaTitulo.setText(informacoesApp.getMeuUsuario().getNomeUsuario().substring(0,1).toUpperCase() + informacoesApp.getMeuUsuario().getNomeUsuario().substring(1).toLowerCase() + ", confira o seu percurso, e a quantidade de vidas, no conteúdo " + meuNivel.getConteudo().getNomeConteudo() + ":");
            switch (meuNivel.getNivel()){
                case COBRE:
                    tvNivelConteudoVisualizaUpgradeActivity.setText("COBRE");
                    tvNivelConteudoVisualizaUpgradeActivity.setTextColor(Color.parseColor("#8c4b27"));
                    break;
                case BRONZE:
                    tvNivelConteudoVisualizaUpgradeActivity.setText("BRONZE");
                    tvNivelConteudoVisualizaUpgradeActivity.setTextColor(Color.parseColor("#8c6727"));
                    break;
                case PRATA:
                    tvNivelConteudoVisualizaUpgradeActivity.setText("PRATA");
                    tvNivelConteudoVisualizaUpgradeActivity.setTextColor(Color.parseColor("#808080"));
                    break;
                case OURO:
                    tvNivelConteudoVisualizaUpgradeActivity.setText("OURO");
                    tvNivelConteudoVisualizaUpgradeActivity.setTextColor(Color.parseColor("#bda102"));
                    break;
                case DIAMANTE:
                    tvNivelConteudoVisualizaUpgradeActivity.setText("DIAMANTE");
                    tvNivelConteudoVisualizaUpgradeActivity.setTextColor(Color.parseColor("#0293a6"));
                    break;
            }
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

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
}

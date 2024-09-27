package com.example.user.projetoquimica;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.banco.InformacoesApp;
import com.example.user.classesDominio.NivelConteudo;

public class TelaFeedbackDetalhada extends AppCompatActivity {

    InformacoesApp informacoesApp;
    Intent it;
    TextView tvNomeConteudoTelaFeedbackDetalhada, tvNivelTelaFeedbackDetalhada, tvExplicacaoTelaFeedbackDetalhada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_feedback_detalhada);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        informacoesApp = (InformacoesApp)getApplicationContext();
        tvNomeConteudoTelaFeedbackDetalhada = findViewById(R.id.tvNomeConteudoTelaFeedbackDetalhada);
        tvNivelTelaFeedbackDetalhada = findViewById(R.id.tvNivelTelaFeedbackDetalhada);
        tvExplicacaoTelaFeedbackDetalhada = findViewById(R.id.tvExplicacaoTelaFeedbackDetalhada);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        it = getIntent();
        if (it != null){
            NivelConteudo nivelConteudo = (NivelConteudo) it.getSerializableExtra("nivelConteudo");
            String explicacao = it.getStringExtra("explicacao");
            tvExplicacaoTelaFeedbackDetalhada.setText(explicacao);
            tvNomeConteudoTelaFeedbackDetalhada.setText(nivelConteudo.getConteudo().getNomeConteudo());
            tvNivelTelaFeedbackDetalhada.setText(nivelConteudo.getNivel().toString());
            switch (nivelConteudo.getNivel()){
                case COBRE:
                    tvNivelTelaFeedbackDetalhada.setTextColor(Color.parseColor("#8c4b27"));
                    break;
                case BRONZE:
                    tvNivelTelaFeedbackDetalhada.setTextColor(Color.parseColor("#8c6727"));
                    break;
                case PRATA:
                    tvNivelTelaFeedbackDetalhada.setTextColor(Color.parseColor("#808080"));
                    break;
                case OURO:
                    tvNivelTelaFeedbackDetalhada.setTextColor(Color.parseColor("#bda102"));
                    break;
                case DIAMANTE:
                    tvNivelTelaFeedbackDetalhada.setTextColor(Color.parseColor("#0293a6"));
                    break;
            }

        }



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

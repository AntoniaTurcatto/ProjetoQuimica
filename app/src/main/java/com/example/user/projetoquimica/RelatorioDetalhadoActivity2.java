package com.example.user.projetoquimica;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.user.banco.DesempenhoQuestionarioDB;
import com.example.user.banco.InformacoesApp;
import com.example.user.banco.NivelConteudoDB;
import com.example.user.classesDominio.ClasseIntermediaria;
import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.NivelConteudo;
import com.example.user.classesDominio.Pergunta;
import com.example.user.componente.RelatorioPerguntaAdapter;
import com.github.mikephil.charting.charts.PieChart;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class RelatorioDetalhadoActivity2 extends AppCompatActivity {


    TextView tvPerguntaEnunciadoRetalorioDetalhado, tvConteudo;
    TextView tvOpcaoARetalorioDetalhado, tvOpcaoBRetalorioDetalhado, tvOpcaoCRetalorioDetalhado,
            tvOpcaoDRetalorioDetalhado, tvOpcaoERetalorioDetalhado, tvOpcaoEscolhidaDetalhado,
            tvOpcaoCertaRetalorioDetalhado;
    ImageView ivImagemPerguntaDetalhada;
    Pergunta pergunta;

    TextView tvMostrarVidas2;
    NivelConteudo nivelConteudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_detalhado2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvMostrarVidas2 = findViewById(R.id.tvMostrarVidas2);

        tvPerguntaEnunciadoRetalorioDetalhado = findViewById(R.id.tvPerguntaEnunciadoRetalorioDetalhado);
        tvOpcaoARetalorioDetalhado = findViewById(R.id.tvOpcaoARetalorioDetalhado);
        tvOpcaoBRetalorioDetalhado = findViewById(R.id.tvOpcaoBRetalorioDetalhado);
        tvOpcaoCRetalorioDetalhado = findViewById(R.id.tvOpcaoCRetalorioDetalhado);
        tvOpcaoDRetalorioDetalhado = findViewById(R.id.tvOpcaoDRetalorioDetalhado);
        tvOpcaoERetalorioDetalhado = findViewById(R.id.tvOpcaoERetalorioDetalhado);
        tvOpcaoEscolhidaDetalhado = findViewById(R.id.tvOpcaoEscolhidaRetalorioDetalhado);
        tvOpcaoCertaRetalorioDetalhado = findViewById(R.id.tvOpcaoCertaRetalorioDetalhado);
        tvConteudo = findViewById(R.id.tvConteudo);
        ivImagemPerguntaDetalhada = findViewById(R.id.ivImagemPerguntaDetalhada);

        Intent it = getIntent();

            if (nivelConteudo != null){
            tvMostrarVidas2.setText(nivelConteudo.getVidas());
        }
        else{
            tvMostrarVidas2.setText("Não Peguei as vidas");
        }
        if (it != null && it.hasExtra("pergunta")) {
            pergunta = (Pergunta) getIntent().getSerializableExtra("pergunta");
            tvPerguntaEnunciadoRetalorioDetalhado.setText(pergunta.getEnunciado());
            tvConteudo.setText(pergunta.getConteudo().getNomeConteudo());
            tvOpcaoARetalorioDetalhado.setText("Opção A: " + pergunta.getOpcaoA());
            tvOpcaoBRetalorioDetalhado.setText("Opção B: " + pergunta.getOpcaoB());
            tvOpcaoCRetalorioDetalhado.setText("Opção C: " + pergunta.getOpcaoC());
            tvOpcaoDRetalorioDetalhado.setText("Opção D: " + pergunta.getOpcaoD());
            tvOpcaoERetalorioDetalhado.setText("Opção E: " + pergunta.getOpcaoE());
            tvOpcaoEscolhidaDetalhado.setText("Opção Escolhida: " + Character.toString(pergunta.getOpcaoEscolhida()));
            tvOpcaoCertaRetalorioDetalhado.setText("Opção Certa: " + Character.toString(pergunta.getAlternativaCorreta()));

            if (pergunta.getImagem() != null) {
                Bitmap imagem = ByteArrayToBitmap(pergunta.getImagem());
                ivImagemPerguntaDetalhada.setImageBitmap(imagem);
            }
            else {
                ivImagemPerguntaDetalhada.setImageResource(R.mipmap.ic_sem_imagem);
            }
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public Bitmap ByteArrayToBitmap(byte[] byteArray) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;

    }
}

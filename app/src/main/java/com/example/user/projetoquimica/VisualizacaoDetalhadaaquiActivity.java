package com.example.user.projetoquimica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.classesDominio.Pergunta;

public class VisualizacaoDetalhadaaquiActivity extends AppCompatActivity {
    TextView tvVisualizacaoDetalhadaEnunciado, tvVisualizacaoDetalhadaOpcaoA, tvVisualizacaoDetalhadaOpcaoB,
            tvVisualizacaoDetalhadaOpcaoC, tvVisualizacaoDetalhadaOpcaoD, tvVisualizacaoDetalhadaOpcaoE,
            tvVisualizacaoDetalhadaRespostaEscolhida, tvVisualizacaoDetalhadaRespostaCerta;
    Pergunta pergunta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizacao_detalhadaaqui);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvVisualizacaoDetalhadaEnunciado = findViewById(R.id.tvVisualizacaoDetalhadaEnunciado);
        tvVisualizacaoDetalhadaOpcaoA = findViewById(R.id.tvVisualizacaoDetalhadaOpcaoA);
        tvVisualizacaoDetalhadaOpcaoB = findViewById(R.id.tvVisualizacaoDetalhadaOpcaoB);
        tvVisualizacaoDetalhadaOpcaoC = findViewById(R.id.tvVisualizacaoDetalhadaOpcaoC);
        tvVisualizacaoDetalhadaOpcaoD = findViewById(R.id.tvVisualizacaoDetalhadaOpcaoD);
        tvVisualizacaoDetalhadaOpcaoE = findViewById(R.id.tvVisualizacaoDetalhadaOpcaoE);
        tvVisualizacaoDetalhadaRespostaCerta = findViewById(R.id.tvVisualizacaoDetalhadaRespostaCerta);
        tvVisualizacaoDetalhadaRespostaEscolhida = findViewById(R.id.tvVisualizacaoDetalhadaRespostaEscolhida);

        Intent it = getIntent();
        if(it != null && it.hasExtra("pergunta")){
            pergunta = (Pergunta) getIntent().getSerializableExtra("pergunta");
            tvVisualizacaoDetalhadaEnunciado.setText(pergunta.getEnunciado());
            tvVisualizacaoDetalhadaOpcaoA.setText(pergunta.getOpcaoA());
            tvVisualizacaoDetalhadaOpcaoB.setText(pergunta.getOpcaoB());
            tvVisualizacaoDetalhadaOpcaoC.setText(pergunta.getOpcaoC());
            tvVisualizacaoDetalhadaOpcaoD.setText(pergunta.getOpcaoD());
            tvVisualizacaoDetalhadaOpcaoE.setText(pergunta.getOpcaoE());
            tvVisualizacaoDetalhadaRespostaEscolhida.setText(Character.toString(pergunta.getOpcaoEscolhida()));
            tvVisualizacaoDetalhadaRespostaCerta.setText(Character.toString(pergunta.getAlternativaCorreta()));
        }

    }


}